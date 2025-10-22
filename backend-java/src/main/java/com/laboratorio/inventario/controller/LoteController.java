package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.dto.LoteCaducidadDTO;
import com.laboratorio.inventario.entity.Lote;
import com.laboratorio.inventario.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lotes")
@CrossOrigin(origins = "*")
public class LoteController {

    @Autowired
    private LoteService loteService;

    // ==========================================
    // OPERACIONES CRUD BÁSICAS
    // ==========================================

    /**
     * GET /api/lotes
     * Obtener todos los lotes
     */
    @GetMapping
    public ResponseEntity<List<Lote>> getAllLotes() {
        List<Lote> lotes = loteService.listarTodos();
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/activos
     * Obtener solo lotes activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Lote>> getLotesActivos() {
        List<Lote> lotes = loteService.listarActivos();
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/{id}
     * Obtener un lote por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lote> getLoteById(@PathVariable Long id) {
        return loteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/lotes
     * Crear un nuevo lote
     */
    @PostMapping
    public ResponseEntity<?> createLote(@RequestBody Lote lote) {
        try {
            Lote nuevoLote = loteService.crear(lote);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLote);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el lote: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT /api/lotes/{id}
     * Actualizar un lote existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLote(@PathVariable Long id, @RequestBody Lote loteActualizado) {
        try {
            Lote lote = loteService.actualizar(id, loteActualizado);
            return ResponseEntity.ok(lote);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * DELETE /api/lotes/{id}
     * Eliminar un lote (soft delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLote(@PathVariable Long id) {
        loteService.eliminar(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Lote eliminado correctamente");
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // ENDPOINTS DE BÚSQUEDA
    // ==========================================

    /**
     * GET /api/lotes/numero/{numeroLote}
     * Buscar lote por número
     */
    @GetMapping("/numero/{numeroLote}")
    public ResponseEntity<Lote> buscarPorNumero(@PathVariable String numeroLote) {
        return loteService.buscarPorNumeroLote(numeroLote)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/lotes/insumo/{insumoId}
     * Obtener todos los lotes de un insumo
     */
    @GetMapping("/insumo/{insumoId}")
    public ResponseEntity<List<Lote>> getLotesPorInsumo(@PathVariable Long insumoId) {
        List<Lote> lotes = loteService.buscarPorInsumo(insumoId);
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/proveedor/{proveedor}
     * Obtener lotes por proveedor
     */
    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<Lote>> getLotesPorProveedor(@PathVariable String proveedor) {
        List<Lote> lotes = loteService.buscarPorProveedor(proveedor);
        return ResponseEntity.ok(lotes);
    }

    // ==========================================
    // ENDPOINTS DE ALERTAS DE CADUCIDAD
    // ==========================================

    /**
     * GET /api/lotes/proximos-caducar?dias=30
     * Obtener lotes próximos a caducar
     */
    @GetMapping("/proximos-caducar")
    public ResponseEntity<List<LoteCaducidadDTO>> getLotesProximosCaducar(
            @RequestParam(defaultValue = "30") int dias) {
        List<LoteCaducidadDTO> lotes = loteService.obtenerLotesProximosCaducar(dias);
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/vencidos
     * Obtener lotes vencidos
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<LoteCaducidadDTO>> getLotesVencidos() {
        List<LoteCaducidadDTO> lotes = loteService.obtenerLotesVencidos();
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/caducidad/insumo/{insumoId}?dias=30
     * Obtener lotes de un insumo próximos a caducar
     */
    @GetMapping("/caducidad/insumo/{insumoId}")
    public ResponseEntity<List<LoteCaducidadDTO>> getLotesPorInsumoProximosCaducar(
            @PathVariable Long insumoId,
            @RequestParam(defaultValue = "30") int dias) {
        List<LoteCaducidadDTO> lotes = loteService.obtenerLotesPorInsumoProximosCaducar(insumoId, dias);
        return ResponseEntity.ok(lotes);
    }

    // ==========================================
    // ENDPOINTS DE GESTIÓN DE STOCK
    // ==========================================

    /**
     * GET /api/lotes/con-stock
     * Obtener lotes con stock disponible
     */
    @GetMapping("/con-stock")
    public ResponseEntity<List<Lote>> getLotesConStock() {
        List<Lote> lotes = loteService.obtenerLotesConStock();
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/agotados
     * Obtener lotes agotados
     */
    @GetMapping("/agotados")
    public ResponseEntity<List<Lote>> getLotesAgotados() {
        List<Lote> lotes = loteService.obtenerLotesAgotados();
        return ResponseEntity.ok(lotes);
    }

    /**
     * GET /api/lotes/insumo/{insumoId}/ordenados
     * Obtener lotes de un insumo ordenados por fecha de caducidad (FEFO)
     */
    @GetMapping("/insumo/{insumoId}/ordenados")
    public ResponseEntity<List<Lote>> getLotesOrdenadosPorCaducidad(@PathVariable Long insumoId) {
        List<Lote> lotes = loteService.obtenerLotesPorInsumoOrdenadosPorCaducidad(insumoId);
        return ResponseEntity.ok(lotes);
    }

    // ==========================================
    // ENDPOINTS DE ESTADÍSTICAS
    // ==========================================

    /**
     * GET /api/lotes/estadisticas
     * Obtener estadísticas de lotes
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("lotesActivos", loteService.contarLotesActivos());
        stats.put("lotesVencidos", loteService.contarLotesVencidos());
        stats.put("lotesProximosVencer7Dias", loteService.contarLotesProximosVencer(7));
        stats.put("lotesProximosVencer30Dias", loteService.contarLotesProximosVencer(30));
        stats.put("valorTotalLotes", loteService.calcularValorTotalLotes());
        
        return ResponseEntity.ok(stats);
    }

    /**
     * GET /api/lotes/health
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servicio de lotes funcionando correctamente");
        return ResponseEntity.ok(response);
    }
}