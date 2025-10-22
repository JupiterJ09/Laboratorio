package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.dto.InsumoDTO;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/insumos")
@CrossOrigin(origins = "*") // Permitir peticiones desde Angular
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    // ==========================================
    // OPERACIONES CRUD BÁSICAS
    // ==========================================

    /**
     * GET /api/insumos
     * Obtener todos los insumos
     */
    @GetMapping
    public ResponseEntity<List<InsumoDTO>> getAllInsumos() {
        try {
            List<InsumoDTO> insumos = insumoService.listarTodos();
            return ResponseEntity.ok(insumos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/insumos/{id}
     * Obtener un insumo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsumoDTO> getInsumoById(@PathVariable Long id) {
        Optional<InsumoDTO> insumo = insumoService.obtenerPorId(id);
        return insumo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/insumos
     * Crear un nuevo insumo
     */
    @PostMapping
    public ResponseEntity<?> createInsumo(@RequestBody Insumo insumo) {
        try {
            InsumoDTO nuevoInsumo = insumoService.crear(insumo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInsumo);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el insumo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT /api/insumos/{id}
     * Actualizar un insumo existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInsumo(@PathVariable Long id, @RequestBody Insumo insumoActualizado) {
        try {
            InsumoDTO insumoGuardado = insumoService.actualizar(id, insumoActualizado);
            return ResponseEntity.ok(insumoGuardado);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el insumo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE /api/insumos/{id}
     * Eliminar un insumo (soft delete - cambiar estado a 'inactivo')
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInsumo(@PathVariable Long id) {
        try {
            insumoService.eliminar(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Insumo eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el insumo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ==========================================
    // ENDPOINTS DE BÚSQUEDA Y FILTRADO
    // ==========================================

    /**
     * GET /api/insumos/activos
     * Obtener solo insumos activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<InsumoDTO>> getInsumosActivos() {
        List<InsumoDTO> insumos = insumoService.listarActivos();
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/buscar?nombre=texto
     * Buscar insumos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<InsumoDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<InsumoDTO> insumos = insumoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/categoria/{categoria}
     * Obtener insumos por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<InsumoDTO>> getInsumosPorCategoria(@PathVariable String categoria) {
        List<InsumoDTO> insumos = insumoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/proveedor/{proveedor}
     * Obtener insumos por proveedor
     */
    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<InsumoDTO>> getInsumosPorProveedor(@PathVariable String proveedor) {
        List<InsumoDTO> insumos = insumoService.buscarPorProveedor(proveedor);
        return ResponseEntity.ok(insumos);
    }

    // ==========================================
    // ENDPOINTS DE ALERTAS Y MONITOREO
    // ==========================================

    /**
     * GET /api/insumos/alertas
     * Obtener insumos con alertas (crítico o bajo)
     */
    @GetMapping("/alertas")
    public ResponseEntity<List<InsumoDTO>> getInsumosConAlerta() {
        List<InsumoDTO> insumos = insumoService.obtenerInsumosConAlerta();
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/bajo-minimo
     * Obtener insumos por debajo del mínimo
     */
    @GetMapping("/bajo-minimo")
    public ResponseEntity<List<InsumoDTO>> getInsumosBajoMinimo() {
        List<InsumoDTO> insumos = insumoService.obtenerInsumosBajoMinimo();
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/proximos-vencer?dias=30
     * Obtener insumos próximos a vencer
     */
    @GetMapping("/proximos-vencer")
    public ResponseEntity<List<InsumoDTO>> getInsumosProximosAVencer(
            @RequestParam(defaultValue = "30") int dias) {
        List<InsumoDTO> insumos = insumoService.obtenerInsumosProximosAVencer(dias);
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/vencidos
     * Obtener insumos vencidos
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<InsumoDTO>> getInsumosVencidos() {
        List<InsumoDTO> insumos = insumoService.obtenerInsumosVencidos();
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/stock-restante?dias=7
     * Obtener insumos con stock para X días o menos
     */
    @GetMapping("/stock-restante")
    public ResponseEntity<List<InsumoDTO>> getInsumosPorStockRestante(
            @RequestParam(defaultValue = "7") int dias) {
        List<InsumoDTO> insumos = insumoService.obtenerInsumosPorStockRestante(dias);
        return ResponseEntity.ok(insumos);
    }

    // ==========================================
    // ENDPOINTS DE ESTADÍSTICAS
    // ==========================================

    /**
     * GET /api/insumos/estadisticas
     * Obtener estadísticas generales del inventario
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Total de insumos activos
        estadisticas.put("totalActivos", insumoService.contarInsumosActivos());
        
        // Insumos con alertas
        estadisticas.put("insumosConAlerta", insumoService.contarInsumosConAlerta());
        
        // Insumos bajo mínimo
        estadisticas.put("insumosBajoMinimo", insumoService.contarInsumosBajoMinimo());
        
        // Valor total del inventario
        estadisticas.put("valorTotalInventario", insumoService.calcularValorTotalInventario());
        
        // Insumos próximos a vencer (30 días)
        estadisticas.put("insumosProximosVencer", insumoService.obtenerInsumosProximosAVencer(30).size());
        
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * GET /api/insumos/valor-por-categoria
     * Obtener valor del inventario por categoría
     */
    @GetMapping("/valor-por-categoria")
    public ResponseEntity<Map<String, Double>> getValorPorCategoria() {
        Map<String, Double> resultado = insumoService.calcularValorPorCategoria();
        return ResponseEntity.ok(resultado);
    }

    // ==========================================
    // ENDPOINT DE HEALTH CHECK
    // ==========================================

    /**
     * GET /api/insumos/health
     * Verificar que el servicio está funcionando
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servicio de insumos funcionando correctamente");
        response.put("timestamp", java.time.LocalDate.now().toString());
        return ResponseEntity.ok(response);
    }
}