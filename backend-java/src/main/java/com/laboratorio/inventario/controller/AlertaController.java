package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.dto.AlertaDTO;
import com.laboratorio.inventario.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin(origins = "*")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    // ==========================================
    // OPERACIONES BÁSICAS
    // ==========================================

    /**
     * GET /api/alertas
     * Obtener todas las alertas
     */
    @GetMapping
    public ResponseEntity<List<AlertaDTO>> getAllAlertas() {
        List<AlertaDTO> alertas = alertaService.listarTodas();
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/alertas/no-leidas
     * Obtener solo alertas no leídas
     */
    @GetMapping("/no-leidas")
    public ResponseEntity<List<AlertaDTO>> getAlertasNoLeidas() {
        List<AlertaDTO> alertas = alertaService.listarNoLeidas();
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/alertas/urgentes
     * Obtener alertas urgentes (críticas y altas) no leídas
     */
    @GetMapping("/urgentes")
    public ResponseEntity<List<AlertaDTO>> getAlertasUrgentes() {
        List<AlertaDTO> alertas = alertaService.buscarAlertasUrgentes();
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/alertas/hoy
     * Obtener alertas de hoy
     */
    @GetMapping("/hoy")
    public ResponseEntity<List<AlertaDTO>> getAlertasDeHoy() {
        List<AlertaDTO> alertas = alertaService.buscarAlertasDeHoy();
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/alertas/{id}
     * Obtener una alerta por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlertaDTO> getAlertaById(@PathVariable Long id) {
        return alertaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/alertas/{id}/marcar-leida
     * Marcar una alerta como leída
     */
    @PutMapping("/{id}/marcar-leida")
    public ResponseEntity<AlertaDTO> marcarComoLeida(@PathVariable Long id) {
        AlertaDTO alerta = alertaService.marcarComoLeida(id);
        if (alerta != null) {
            return ResponseEntity.ok(alerta);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/alertas/{id}
     * Eliminar una alerta
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarAlerta(@PathVariable Long id) {
        alertaService.eliminar(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Alerta eliminada correctamente");
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // BÚSQUEDAS Y FILTROS
    // ==========================================

    /**
     * GET /api/alertas/tipo/{tipo}
     * Buscar alertas por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<AlertaDTO>> getAlertasPorTipo(@PathVariable String tipo) {
        List<AlertaDTO> alertas = alertaService.buscarPorTipo(tipo);
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/alertas/prioridad/{prioridad}
     * Buscar alertas por prioridad
     */
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<AlertaDTO>> getAlertasPorPrioridad(@PathVariable String prioridad) {
        List<AlertaDTO> alertas = alertaService.buscarPorPrioridad(prioridad);
        return ResponseEntity.ok(alertas);
    }

    /**
     * GET /api/alertas/insumo/{insumoId}
     * Buscar alertas de un insumo específico
     */
    @GetMapping("/insumo/{insumoId}")
    public ResponseEntity<List<AlertaDTO>> getAlertasPorInsumo(@PathVariable Long insumoId) {
        List<AlertaDTO> alertas = alertaService.buscarPorInsumo(insumoId);
        return ResponseEntity.ok(alertas);
    }

    // ==========================================
    // GENERACIÓN AUTOMÁTICA DE ALERTAS
    // ==========================================

    /**
     * POST /api/alertas/verificar-y-generar
     * Verificar y generar todas las alertas necesarias
     */
    @PostMapping("/verificar-y-generar")
    public ResponseEntity<Map<String, Object>> verificarYGenerarAlertas() {
        List<AlertaDTO> alertasGeneradas = alertaService.verificarYGenerarAlertas();
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Verificación completada");
        response.put("alertasGeneradas", alertasGeneradas.size());
        response.put("alertas", alertasGeneradas);
        
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/alertas/verificar-stock-bajo
     * Verificar y generar alertas de stock bajo
     */
    @PostMapping("/verificar-stock-bajo")
    public ResponseEntity<List<AlertaDTO>> verificarStockBajo() {
        List<AlertaDTO> alertas = alertaService.verificarAlertasStockBajo();
        return ResponseEntity.ok(alertas);
    }

    /**
     * POST /api/alertas/verificar-caducidad
     * Verificar y generar alertas de caducidad
     */
    @PostMapping("/verificar-caducidad")
    public ResponseEntity<List<AlertaDTO>> verificarCaducidad() {
        List<AlertaDTO> alertas = alertaService.verificarAlertasCaducidad();
        return ResponseEntity.ok(alertas);
    }

    /**
     * POST /api/alertas/verificar-vencidos
     * Verificar y generar alertas de lotes vencidos
     */
    @PostMapping("/verificar-vencidos")
    public ResponseEntity<List<AlertaDTO>> verificarVencidos() {
        List<AlertaDTO> alertas = alertaService.verificarAlertasVencidos();
        return ResponseEntity.ok(alertas);
    }

    /**
     * POST /api/alertas/verificar-agotamiento
     * Verificar y generar alertas de agotamiento próximo
     */
    @PostMapping("/verificar-agotamiento")
    public ResponseEntity<List<AlertaDTO>> verificarAgotamiento() {
        List<AlertaDTO> alertas = alertaService.verificarAlertasAgotamiento();
        return ResponseEntity.ok(alertas);
    }

    // ==========================================
    // CREAR ALERTAS MANUALMENTE
    // ==========================================

    /**
     * POST /api/alertas/crear-personalizada
     * Crear una alerta personalizada
     */
    @PostMapping("/crear-personalizada")
    public ResponseEntity<AlertaDTO> crearAlertaPersonalizada(
            @RequestBody Map<String, Object> request) {
        
        String tipo = (String) request.get("tipo");
        String prioridad = (String) request.get("prioridad");
        String titulo = (String) request.get("titulo");
        String mensaje = (String) request.get("mensaje");
        Long insumoId = request.get("insumoId") != null ? 
                        Long.valueOf(request.get("insumoId").toString()) : null;
        Long loteId = request.get("loteId") != null ? 
                      Long.valueOf(request.get("loteId").toString()) : null;

        AlertaDTO alerta = alertaService.crearAlertaPersonalizada(
                tipo, prioridad, titulo, mensaje, insumoId, loteId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }

    // ==========================================
    // ESTADÍSTICAS
    // ==========================================

    /**
     * GET /api/alertas/estadisticas
     * Obtener estadísticas de alertas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalNoLeidas", alertaService.contarNoLeidas());
        stats.put("criticas", alertaService.contarPorPrioridad("CRITICA"));
        stats.put("altas", alertaService.contarPorPrioridad("ALTA"));
        stats.put("medias", alertaService.contarPorPrioridad("MEDIA"));
        stats.put("bajas", alertaService.contarPorPrioridad("BAJA"));
        
        // Resumen por tipo
        Map<String, Long> porTipo = new HashMap<>();
        porTipo.put("stockBajo", (long) alertaService.buscarPorTipo("STOCK_BAJO").size());
        porTipo.put("caducidad", (long) alertaService.buscarPorTipo("CADUCIDAD").size());
        porTipo.put("vencido", (long) alertaService.buscarPorTipo("VENCIDO").size());
        porTipo.put("agotamiento", (long) alertaService.buscarPorTipo("AGOTAMIENTO_PROXIMO").size());
        stats.put("porTipo", porTipo);
        
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/alertas/resumen
     * Obtener resumen rápido de alertas
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> getResumen() {
        Map<String, Object> resumen = new HashMap<>();
        
        Long noLeidas = alertaService.contarNoLeidas();
        Long urgentes = (long) alertaService.buscarAlertasUrgentes().size();
        Long hoy = (long) alertaService.buscarAlertasDeHoy().size();
        
        resumen.put("noLeidas", noLeidas);
        resumen.put("urgentes", urgentes);
        resumen.put("hoy", hoy);
        resumen.put("requierenAtencion", urgentes > 0);
        
        return ResponseEntity.ok(resumen);
    }

    // ==========================================
    // MANTENIMIENTO
    // ==========================================

    /**
     * DELETE /api/alertas/limpiar-antiguas?dias=30
     * Limpiar alertas antiguas leídas
     */
    @DeleteMapping("/limpiar-antiguas")
    public ResponseEntity<Map<String, String>> limpiarAlertasAntiguas(
            @RequestParam(defaultValue = "30") int dias) {
        
        alertaService.limpiarAlertasAntiguas(dias);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Alertas antiguas eliminadas correctamente");
        response.put("diasAntiguedad", String.valueOf(dias));
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/alertas/health
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servicio de alertas funcionando correctamente");
        return ResponseEntity.ok(response);
    }
}
