package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.dto.ConsumoHistoricoDTO;
import com.laboratorio.inventario.service.ConsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consumo")
@CrossOrigin(origins = "*")
public class ConsumoController {

    @Autowired
    private ConsumoService consumoService;

    /**
     * GET /api/consumo/historico/{insumoId}?dias=30
     * Obtener historial de consumo de un insumo
     */
    @GetMapping("/historico/{insumoId}")
    public ResponseEntity<List<ConsumoHistoricoDTO>> obtenerHistorico(
            @PathVariable Long insumoId,
            @RequestParam(defaultValue = "30") int dias) {
        
        List<ConsumoHistoricoDTO> historico = consumoService.obtenerConsumoHistorico(insumoId, dias);
        return ResponseEntity.ok(historico);
    }

    /**
     * GET /api/consumo/promedio/{insumoId}?dias=30
     * Calcular promedio de consumo diario
     */
    @GetMapping("/promedio/{insumoId}")
    public ResponseEntity<Map<String, Object>> calcularPromedio(
            @PathVariable Long insumoId,
            @RequestParam(defaultValue = "30") int dias) {
        
        Double promedio = consumoService.calcularPromedioConsumo(insumoId, dias);
        
        Map<String, Object> response = new HashMap<>();
        response.put("insumoId", insumoId);
        response.put("dias", dias);
        response.put("promedioDiario", promedio);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/consumo/tendencia/{insumoId}
     * Obtener tendencia de consumo
     */
    @GetMapping("/tendencia/{insumoId}")
    public ResponseEntity<Map<String, String>> obtenerTendencia(@PathVariable Long insumoId) {
        String tendencia = consumoService.obtenerTendencia(insumoId);
        
        Map<String, String> response = new HashMap<>();
        response.put("insumoId", String.valueOf(insumoId));
        response.put("tendencia", tendencia);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/consumo/prediccion/{insumoId}
     * Predecir días hasta agotamiento
     */
    @GetMapping("/prediccion/{insumoId}")
    public ResponseEntity<Map<String, Object>> predecirAgotamiento(@PathVariable Long insumoId) {
        Integer diasEstimados = consumoService.predecirDiasHastaAgotamiento(insumoId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("insumoId", insumoId);
        response.put("diasHastaAgotamiento", diasEstimados);
        
        if (diasEstimados != null) {
            LocalDate fechaEstimada = LocalDate.now().plusDays(diasEstimados);
            response.put("fechaEstimadaAgotamiento", fechaEstimada);
            
            // Determinar nivel de alerta
            String nivelAlerta;
            if (diasEstimados <= 7) {
                nivelAlerta = "critico";
            } else if (diasEstimados <= 30) {
                nivelAlerta = "medio";
            } else {
                nivelAlerta = "normal";
            }
            response.put("nivelAlerta", nivelAlerta);
        }
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/consumo/rango/{insumoId}?fechaInicio=2025-01-01&fechaFin=2025-01-31
     * Obtener consumo por rango de fechas
     */
    @GetMapping("/rango/{insumoId}")
    public ResponseEntity<List<ConsumoHistoricoDTO>> obtenerConsumoPorRango(
            @PathVariable Long insumoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        List<ConsumoHistoricoDTO> consumo = consumoService.obtenerConsumoPorRango(insumoId, fechaInicio, fechaFin);
        return ResponseEntity.ok(consumo);
    }

    /**
     * GET /api/consumo/total/{insumoId}?dias=30
     * Calcular consumo total en un período
     */
    @GetMapping("/total/{insumoId}")
    public ResponseEntity<Map<String, Object>> calcularConsumoTotal(
            @PathVariable Long insumoId,
            @RequestParam(defaultValue = "30") int dias) {
        
        Double total = consumoService.calcularConsumoTotal(insumoId, dias);
        
        Map<String, Object> response = new HashMap<>();
        response.put("insumoId", insumoId);
        response.put("dias", dias);
        response.put("consumoTotal", total);
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/consumo/health
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servicio de consumo funcionando correctamente");
        return ResponseEntity.ok(response);
    }
}