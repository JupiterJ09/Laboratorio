package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/insumos")
@CrossOrigin(origins = "*") // Permitir peticiones desde Angular
public class InsumoController {

    @Autowired
    private InsumoRepository insumoRepository;

    // ==========================================
    // OPERACIONES CRUD BÁSICAS
    // ==========================================

    /**
     * GET /api/insumos
     * Obtener todos los insumos
     */
    @GetMapping
    public ResponseEntity<List<Insumo>> getAllInsumos() {
        try {
            List<Insumo> insumos = insumoRepository.findAll();
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
    public ResponseEntity<Insumo> getInsumoById(@PathVariable Long id) {
        Optional<Insumo> insumo = insumoRepository.findById(id);
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
            // Verificar si el código de catálogo ya existe
            if (insumo.getCodigoCatalogo() != null && 
                insumoRepository.existsByCodigoCatalogo(insumo.getCodigoCatalogo())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El código de catálogo ya existe");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Calcular nivel de alerta antes de guardar
            insumo.calcularNivelAlerta();
            
            Insumo nuevoInsumo = insumoRepository.save(insumo);
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
            Optional<Insumo> insumoExistente = insumoRepository.findById(id);
            
            if (insumoExistente.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Insumo no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Insumo insumo = insumoExistente.get();
            
            // Actualizar campos
            insumo.setNombre(insumoActualizado.getNombre());
            insumo.setCodigoCatalogo(insumoActualizado.getCodigoCatalogo());
            insumo.setUnidadMedida(insumoActualizado.getUnidadMedida());
            insumo.setCantidadActual(insumoActualizado.getCantidadActual());
            insumo.setCantidadMinima(insumoActualizado.getCantidadMinima());
            insumo.setPrecioUnitario(insumoActualizado.getPrecioUnitario());
            insumo.setProveedor(insumoActualizado.getProveedor());
            insumo.setUbicacionAlmacen(insumoActualizado.getUbicacionAlmacen());
            insumo.setFechaCaducidad(insumoActualizado.getFechaCaducidad());
            insumo.setLote(insumoActualizado.getLote());
            insumo.setCategoria(insumoActualizado.getCategoria());
            insumo.setDescripcion(insumoActualizado.getDescripcion());
            insumo.setEstado(insumoActualizado.getEstado());
            insumo.setConsumoPromedioDiario(insumoActualizado.getConsumoPromedioDiario());
            insumo.setDiasStockRestante(insumoActualizado.getDiasStockRestante());
            
            // Recalcular nivel de alerta
            insumo.calcularNivelAlerta();
            
            Insumo insumoGuardado = insumoRepository.save(insumo);
            return ResponseEntity.ok(insumoGuardado);
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
            Optional<Insumo> insumoExistente = insumoRepository.findById(id);
            
            if (insumoExistente.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Insumo no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Insumo insumo = insumoExistente.get();
            insumo.setEstado("inactivo");
            insumoRepository.save(insumo);
            
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
    public ResponseEntity<List<Insumo>> getInsumosActivos() {
        List<Insumo> insumos = insumoRepository.findByEstadoOrderByNombreAsc("activo");
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/buscar?nombre=texto
     * Buscar insumos por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Insumo>> buscarPorNombre(@RequestParam String nombre) {
        List<Insumo> insumos = insumoRepository.findByNombreContainingIgnoreCase(nombre);
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/categoria/{categoria}
     * Obtener insumos por categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Insumo>> getInsumosPorCategoria(@PathVariable String categoria) {
        List<Insumo> insumos = insumoRepository.findByCategoria(categoria);
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/proveedor/{proveedor}
     * Obtener insumos por proveedor
     */
    @GetMapping("/proveedor/{proveedor}")
    public ResponseEntity<List<Insumo>> getInsumosPorProveedor(@PathVariable String proveedor) {
        List<Insumo> insumos = insumoRepository.findByProveedor(proveedor);
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
    public ResponseEntity<List<Insumo>> getInsumosConAlerta() {
        List<Insumo> insumos = insumoRepository.findInsumosConAlerta();
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/bajo-minimo
     * Obtener insumos por debajo del mínimo
     */
    @GetMapping("/bajo-minimo")
    public ResponseEntity<List<Insumo>> getInsumosBajoMinimo() {
        List<Insumo> insumos = insumoRepository.findInsumosBajoMinimo();
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/proximos-vencer?dias=30
     * Obtener insumos próximos a vencer
     */
    @GetMapping("/proximos-vencer")
    public ResponseEntity<List<Insumo>> getInsumosProximosAVencer(
            @RequestParam(defaultValue = "30") int dias) {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(dias);
        List<Insumo> insumos = insumoRepository.findInsumosProximosAVencer(fechaInicio, fechaFin);
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/vencidos
     * Obtener insumos vencidos
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<Insumo>> getInsumosVencidos() {
        List<Insumo> insumos = insumoRepository.findInsumosVencidos(LocalDate.now());
        return ResponseEntity.ok(insumos);
    }

    /**
     * GET /api/insumos/stock-restante?dias=7
     * Obtener insumos con stock para X días o menos
     */
    @GetMapping("/stock-restante")
    public ResponseEntity<List<Insumo>> getInsumosPorStockRestante(
            @RequestParam(defaultValue = "7") int dias) {
        List<Insumo> insumos = insumoRepository.findInsumosPorDiasStockRestante(dias);
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
        long totalActivos = insumoRepository.findByEstado("activo").size();
        estadisticas.put("totalActivos", totalActivos);
        
        // Insumos con alertas
        long insumosConAlerta = insumoRepository.findInsumosConAlerta().size();
        estadisticas.put("insumosConAlerta", insumosConAlerta);
        
        // Insumos bajo mínimo
        long insumosBajoMinimo = insumoRepository.findInsumosBajoMinimo().size();
        estadisticas.put("insumosBajoMinimo", insumosBajoMinimo);
        
        // Valor total del inventario
        Double valorTotal = insumoRepository.calcularValorTotalInventario();
        estadisticas.put("valorTotalInventario", valorTotal != null ? valorTotal : 0.0);
        
        // Insumos próximos a vencer (30 días)
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(30);
        long insumosProximosVencer = insumoRepository.findInsumosProximosAVencer(fechaInicio, fechaFin).size();
        estadisticas.put("insumosProximosVencer", insumosProximosVencer);
        
        // Conteo por nivel de alerta
        List<Object[]> conteoPorAlerta = insumoRepository.contarInsumosPorNivelAlerta();
        Map<String, Long> alertas = new HashMap<>();
        for (Object[] row : conteoPorAlerta) {
            alertas.put((String) row[0], ((Number) row[1]).longValue());
        }
        estadisticas.put("conteoPorNivelAlerta", alertas);
        
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * GET /api/insumos/valor-por-categoria
     * Obtener valor del inventario por categoría
     */
    @GetMapping("/valor-por-categoria")
    public ResponseEntity<Map<String, Double>> getValorPorCategoria() {
        List<Object[]> valores = insumoRepository.calcularValorInventarioPorCategoria();
        Map<String, Double> resultado = new HashMap<>();
        
        for (Object[] row : valores) {
            String categoria = (String) row[0];
            Double valor = ((Number) row[1]).doubleValue();
            resultado.put(categoria, valor);
        }
        
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
        response.put("timestamp", LocalDate.now().toString());
        return ResponseEntity.ok(response);
    }
}