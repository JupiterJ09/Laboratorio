package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.dto.EstadisticasDTO;
import com.laboratorio.inventario.service.InsumoService;
import com.laboratorio.inventario.service.LoteService;
//import com.laboratorio.inventario.service.ConsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = "*")
public class EstadisticasController {

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private LoteService loteService;

    @Autowired
    //private ConsumoService consumoService;

    /**
     * GET /api/estadisticas/dashboard
     * Obtener todas las estadísticas para el dashboard principal
     */
    @GetMapping("/dashboard")
    public ResponseEntity<EstadisticasDTO> getDashboardEstadisticas() {
        EstadisticasDTO stats = new EstadisticasDTO();
        
        // Estadísticas de Insumos
        stats.setTotalInsumos((long) insumoService.listarTodos().size());
        stats.setInsumosActivos(insumoService.contarInsumosActivos());
        stats.setInsumosConAlerta(insumoService.contarInsumosConAlerta());
        stats.setInsumosBajoMinimo(insumoService.contarInsumosBajoMinimo());
        stats.setInsumosCriticos(insumoService.obtenerInsumosConAlerta()
                .stream()
                .filter(i -> "critico".equals(i.getNivelAlerta()))
                .count());
        
        // Valores Monetarios
        stats.setValorTotalInventario(insumoService.calcularValorTotalInventario());
        stats.setValorInsumosVencidos(0.0); // Implementar si es necesario
        stats.setValorInsumosBajoMinimo(calcularValorInsumosBajoMinimo());
        
        // Estadísticas de Lotes
        stats.setLotesVencidos(loteService.contarLotesVencidos());
        stats.setLotesProximosVencer(loteService.contarLotesProximosVencer(30));
        stats.setLotesProximosVencer7Dias(loteService.contarLotesProximosVencer(7));
        
        // Consumo
        stats.setConsumoPromedioDiario(0.0); // Implementar promedio general si es necesario
        stats.setConsumoMensual(0.0); // Implementar si es necesario
        
        // Movimientos (Entradas y Salidas del mes)
        stats.setEntradasMes(0L); // Implementar cuando tengamos el servicio de entradas
        stats.setSalidasMes(0L); // Implementar cuando tengamos el servicio de salidas
        
        // Distribución por categoría
        stats.setInsumosPorCategoria(contarPorCategoria());
        stats.setValorPorCategoria(insumoService.calcularValorPorCategoria());
        
        // Proveedores
        stats.setTotalProveedores(0L); // Implementar cuando tengamos el servicio de proveedores
        stats.setProveedoresActivos(0L);
        
        // Ubicaciones
        stats.setTotalUbicaciones(0L); // Implementar cuando tengamos el servicio de ubicaciones
        stats.setPorcentajeOcupacionAlmacen(0.0);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/estadisticas/resumen
     * Obtener un resumen rápido de estadísticas clave
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> getResumen() {
        Map<String, Object> resumen = new HashMap<>();
        
        resumen.put("insumosActivos", insumoService.contarInsumosActivos());
        resumen.put("insumosAlerta", insumoService.contarInsumosConAlerta());
        resumen.put("insumosCriticos", insumoService.obtenerInsumosConAlerta()
                .stream()
                .filter(i -> "critico".equals(i.getNivelAlerta()))
                .count());
        resumen.put("valorInventario", insumoService.calcularValorTotalInventario());
        resumen.put("lotesVencidos", loteService.contarLotesVencidos());
        resumen.put("lotesProximosVencer", loteService.contarLotesProximosVencer(30));
        
        return ResponseEntity.ok(resumen);
    }

    /**
     * GET /api/estadisticas/alertas
     * Obtener resumen de alertas críticas
     */
    @GetMapping("/alertas")
    public ResponseEntity<Map<String, Object>> getAlertas() {
        Map<String, Object> alertas = new HashMap<>();
        
        // Alertas de stock
        alertas.put("insumosCriticos", insumoService.obtenerInsumosConAlerta()
                .stream()
                .filter(i -> "critico".equals(i.getNivelAlerta()))
                .count());
        alertas.put("insumosBajoMinimo", insumoService.contarInsumosBajoMinimo());
        
        // Alertas de caducidad
        alertas.put("lotesVencidos", loteService.contarLotesVencidos());
        alertas.put("lotesVencenEn7Dias", loteService.contarLotesProximosVencer(7));
        alertas.put("lotesVencenEn30Dias", loteService.contarLotesProximosVencer(30));
        
        // Insumos que se agotarán pronto
        long insumosAgotamientoProximo = insumoService.obtenerInsumosPorStockRestante(7).size();
        alertas.put("insumosAgotamientoProximo", insumosAgotamientoProximo);
        
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/estadisticas/inventario
     * Obtener estadísticas detalladas del inventario
     */
    @GetMapping("/inventario")
    public ResponseEntity<Map<String, Object>> getInventarioStats() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalInsumos", (long) insumoService.listarTodos().size());
        stats.put("insumosActivos", insumoService.contarInsumosActivos());
        stats.put("valorTotal", insumoService.calcularValorTotalInventario());
        stats.put("valorPorCategoria", insumoService.calcularValorPorCategoria());
        stats.put("distribucionPorCategoria", contarPorCategoria());
        
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/estadisticas/health
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servicio de estadísticas funcionando correctamente");
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // MÉTODOS PRIVADOS AUXILIARES
    // ==========================================

    private Double calcularValorInsumosBajoMinimo() {
        return insumoService.obtenerInsumosBajoMinimo()
                .stream()
                .mapToDouble(insumo -> {
                    if (insumo.getValorTotal() != null) {
                        return insumo.getValorTotal().doubleValue();
                    }
                    return 0.0;
                })
                .sum();
    }

    private Map<String, Long> contarPorCategoria() {
        Map<String, Long> conteo = new HashMap<>();
        
        insumoService.listarActivos().forEach(insumo -> {
            String categoria = insumo.getCategoria() != null ? insumo.getCategoria() : "Sin categoría";
            conteo.put(categoria, conteo.getOrDefault(categoria, 0L) + 1);
        });
        
        return conteo;
    }
}