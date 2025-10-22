package com.laboratorio.inventario.service.impl;

import com.laboratorio.inventario.dto.ConsumoHistoricoDTO;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.entity.Salida;
import com.laboratorio.inventario.repository.InsumoRepository;
import com.laboratorio.inventario.repository.SalidaRepository;
import com.laboratorio.inventario.service.ConsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsumoServiceImpl implements ConsumoService {

    @Autowired
    private SalidaRepository salidaRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoHistoricoDTO> obtenerConsumoHistorico(Long insumoId, int dias) {
        LocalDate fechaInicio = LocalDate.now().minusDays(dias);
        LocalDate fechaFin = LocalDate.now();
        
        List<Salida> salidas = salidaRepository.findByInsumoIdAndFechaSalidaBetween(
            insumoId, fechaInicio, fechaFin
        );
        
        return salidas.stream()
                .map(this::convertirSalidaADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioConsumo(Long insumoId, int dias) {
        LocalDate fechaInicio = LocalDate.now().minusDays(dias);
        LocalDate fechaFin = LocalDate.now();
        
        Double totalConsumido = salidaRepository.calcularTotalSalidasPorInsumoYFecha(
            insumoId, fechaInicio, fechaFin
        );
        
        if (totalConsumido == null || totalConsumido == 0) {
            return 0.0;
        }
        
        // Calcular promedio diario
        return totalConsumido / dias;
    }

    @Override
    @Transactional(readOnly = true)
    public String obtenerTendencia(Long insumoId) {
        // Comparar consumo de los últimos 30 días vs los 30 días anteriores
        Double consumoReciente = calcularConsumoTotal(insumoId, 30);
        
        LocalDate fecha30DiasAtras = LocalDate.now().minusDays(30);
        LocalDate fecha60DiasAtras = LocalDate.now().minusDays(60);
        
        Double consumoAnterior = salidaRepository.calcularTotalSalidasPorInsumoYFecha(
            insumoId, fecha60DiasAtras, fecha30DiasAtras
        );
        
        if (consumoAnterior == null || consumoAnterior == 0) {
            return "estable";
        }
        
        double diferencia = ((consumoReciente - consumoAnterior) / consumoAnterior) * 100;
        
        if (diferencia > 10) {
            return "creciente";
        } else if (diferencia < -10) {
            return "decreciente";
        } else {
            return "estable";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer predecirDiasHastaAgotamiento(Long insumoId) {
        Insumo insumo = insumoRepository.findById(insumoId).orElse(null);
        
        if (insumo == null || insumo.getCantidadActual() == null) {
            return null;
        }
        
        // Si no hay cantidad, ya está agotado
        if (insumo.getCantidadActual().compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        
        // Calcular promedio de consumo diario de los últimos 30 días
        Double promedioConsumo = calcularPromedioConsumo(insumoId, 30);
        
        if (promedioConsumo == null || promedioConsumo <= 0) {
            return null; // No hay datos suficientes o no hay consumo
        }
        
        // Calcular días hasta agotamiento
        double cantidadActual = insumo.getCantidadActual().doubleValue();
        int diasEstimados = (int) Math.ceil(cantidadActual / promedioConsumo);
        
        return diasEstimados;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoHistoricoDTO> obtenerConsumoPorRango(Long insumoId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Salida> salidas = salidaRepository.findByInsumoIdAndFechaSalidaBetween(
            insumoId, fechaInicio, fechaFin
        );
        
        return salidas.stream()
                .map(this::convertirSalidaADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularConsumoTotal(Long insumoId, int dias) {
        LocalDate fechaInicio = LocalDate.now().minusDays(dias);
        LocalDate fechaFin = LocalDate.now();
        
        Double total = salidaRepository.calcularTotalSalidasPorInsumoYFecha(
            insumoId, fechaInicio, fechaFin
        );
        
        return total != null ? total : 0.0;
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE CONVERSIÓN
    // ==========================================

    private ConsumoHistoricoDTO convertirSalidaADTO(Salida salida) {
        ConsumoHistoricoDTO dto = new ConsumoHistoricoDTO();
        
        dto.setInsumoId(salida.getInsumo().getId());
        dto.setInsumoNombre(salida.getInsumo().getNombre());
        dto.setFecha(salida.getFechaSalida());
        dto.setCantidadConsumida(salida.getCantidad());
        dto.setMotivo(salida.getMotivo());
        dto.setResponsable(salida.getResponsable());
        dto.setAreaDestino(salida.getAreaDestino());
        
        return dto;
    }
}