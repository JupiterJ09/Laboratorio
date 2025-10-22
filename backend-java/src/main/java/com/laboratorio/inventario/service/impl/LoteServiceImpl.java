package com.laboratorio.inventario.service.impl;

import com.laboratorio.inventario.dto.LoteCaducidadDTO;
import com.laboratorio.inventario.entity.Lote;
import com.laboratorio.inventario.repository.LoteRepository;
import com.laboratorio.inventario.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoteServiceImpl implements LoteService {

    @Autowired
    private LoteRepository loteRepository;

    // ==========================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<Lote> listarTodos() {
        return loteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> listarActivos() {
        return loteRepository.findByEstado("activo");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lote> obtenerPorId(Long id) {
        return loteRepository.findById(id);
    }

    @Override
    public Lote crear(Lote lote) {
        // Validar que la cantidad actual no sea mayor a la inicial
        if (lote.getCantidadActual().compareTo(lote.getCantidadInicial()) > 0) {
            throw new IllegalArgumentException("La cantidad actual no puede ser mayor a la cantidad inicial");
        }
        
        return loteRepository.save(lote);
    }

    @Override
    public Lote actualizar(Long id, Lote loteActualizado) {
        return loteRepository.findById(id)
                .map(lote -> {
                    lote.setNumeroLote(loteActualizado.getNumeroLote());
                    lote.setFechaFabricacion(loteActualizado.getFechaFabricacion());
                    lote.setFechaCaducidad(loteActualizado.getFechaCaducidad());
                    lote.setCantidadActual(loteActualizado.getCantidadActual());
                    lote.setProveedor(loteActualizado.getProveedor());
                    lote.setPrecioCompra(loteActualizado.getPrecioCompra());
                    lote.setNumeroFactura(loteActualizado.getNumeroFactura());
                    lote.setEstado(loteActualizado.getEstado());
                    lote.setObservaciones(loteActualizado.getObservaciones());
                    
                    return loteRepository.save(lote);
                })
                .orElseThrow(() -> new RuntimeException("Lote no encontrado con id: " + id));
    }

    @Override
    public void eliminar(Long id) {
        loteRepository.findById(id)
                .ifPresent(lote -> {
                    lote.setEstado("inactivo");
                    loteRepository.save(lote);
                });
    }

    // ==========================================
    // MÉTODOS DE BÚSQUEDA
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public Optional<Lote> buscarPorNumeroLote(String numeroLote) {
        return loteRepository.findByNumeroLote(numeroLote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> buscarPorInsumo(Long insumoId) {
        return loteRepository.findByInsumoId(insumoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> buscarPorProveedor(String proveedor) {
        return loteRepository.findByProveedor(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> buscarPorEstado(String estado) {
        return loteRepository.findByEstado(estado);
    }

    // ==========================================
    // ALERTAS DE CADUCIDAD
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<LoteCaducidadDTO> obtenerLotesProximosCaducar(int dias) {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(dias);
        
        List<Lote> lotes = loteRepository.findLotesProximosAVencer(fechaInicio, fechaFin);
        
        return lotes.stream()
                .map(this::convertirALoteCaducidadDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoteCaducidadDTO> obtenerLotesVencidos() {
        List<Lote> lotes = loteRepository.findLotesVencidos(LocalDate.now());
        
        return lotes.stream()
                .map(this::convertirALoteCaducidadDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoteCaducidadDTO> obtenerLotesPorInsumoProximosCaducar(Long insumoId, int dias) {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(dias);
        
        List<Lote> lotes = loteRepository.findLotesPorInsumoProximosAVencer(insumoId, fechaInicio, fechaFin);
        
        return lotes.stream()
                .map(this::convertirALoteCaducidadDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // GESTIÓN DE STOCK
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<Lote> obtenerLotesConStock() {
        return loteRepository.findLotesConStock();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> obtenerLotesAgotados() {
        return loteRepository.findLotesAgotados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lote> obtenerLotesPorInsumoOrdenadosPorCaducidad(Long insumoId) {
        return loteRepository.findLotesPorInsumoOrdenadosPorCaducidad(insumoId);
    }

    // ==========================================
    // ESTADÍSTICAS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public Long contarLotesActivos() {
        return (long) loteRepository.findByEstado("activo").size();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarLotesVencidos() {
        return (long) loteRepository.findLotesVencidos(LocalDate.now()).size();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarLotesProximosVencer(int dias) {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(dias);
        return (long) loteRepository.findLotesProximosAVencer(fechaInicio, fechaFin).size();
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularValorTotalLotes() {
        Double valor = loteRepository.calcularValorTotalLotes();
        return valor != null ? valor : 0.0;
    }

    @Override
    public String calcularNivelAlerta(LocalDate fechaCaducidad) {
        if (fechaCaducidad == null) {
            return "bajo";
        }
        
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
        
        if (diasRestantes < 0) {
            return "vencido";
        } else if (diasRestantes <= 7) {
            return "critico";
        } else if (diasRestantes <= 30) {
            return "medio";
        } else {
            return "bajo";
        }
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE CONVERSIÓN
    // ==========================================

    private LoteCaducidadDTO convertirALoteCaducidadDTO(Lote lote) {
        LoteCaducidadDTO dto = new LoteCaducidadDTO();
        
        dto.setLoteId(lote.getId());
        dto.setNumeroLote(lote.getNumeroLote());
        dto.setInsumoId(lote.getInsumo().getId());
        dto.setInsumoNombre(lote.getInsumo().getNombre());
        dto.setFechaCaducidad(lote.getFechaCaducidad());
        dto.setCantidadActual(lote.getCantidadActual());
        dto.setUbicacion(lote.getInsumo().getUbicacionAlmacen());
        
        // Calcular días restantes
        if (lote.getFechaCaducidad() != null) {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), lote.getFechaCaducidad());
            dto.setDiasRestantes((int) dias);
        }
        
        // Calcular nivel de alerta
        dto.setNivelAlerta(calcularNivelAlerta(lote.getFechaCaducidad()));
        
        return dto;
    }
}