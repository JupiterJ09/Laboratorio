package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.service.IAClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ia")  // Cambiado de /api/prediccion a /api/ia
@CrossOrigin(origins = "*") // Permitir peticiones desde Angular
public class IAController {

    private final IAClientService iaClientService;

    public IAController(IAClientService iaClientService) {
        this.iaClientService = iaClientService;
    }

    /**
     * Endpoint para obtener la precisión actual del modelo de IA
     * GET http://localhost:8081/api/ia/precision
     */
    @GetMapping("/precision")
    public ResponseEntity<Map<String, Object>> obtenerPrecisionIA() {
        try {
            Map<String, Object> precisionData = iaClientService.obtenerPrecisionIA();

            if (precisionData.containsKey("error")) {
                return ResponseEntity.badRequest().body(precisionData);
            }

            // Estandarizamos la clave a "precision"
            Map<String, Object> response = new HashMap<>();
            response.put("precision", precisionData.get("precision"));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "No se pudo obtener la precisión del modelo: " + e.getMessage())
            );
        }
    }

    /**
     * Endpoint para obtener la predicción de un insumo específico
     * GET http://localhost:8081/api/ia/{insumoId}
     */
    @GetMapping("/{insumoId}")
    public ResponseEntity<Map<String, Object>> obtenerPrediccion(@PathVariable Long insumoId) {
        try {
            Map<String, Object> respuesta = iaClientService.obtenerPrediccion(insumoId);
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "No se pudo obtener la predicción: " + e.getMessage())
            );
        }
    }
}
