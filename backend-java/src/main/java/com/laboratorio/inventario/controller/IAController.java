package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.service.IAClientService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/prediccion")
@CrossOrigin(origins = "*")
public class IAController {

    private final IAClientService iaClientService;

    public IAController(IAClientService iaClientService) {
        this.iaClientService = iaClientService;
    }

    @GetMapping("/{insumoId}")
    public ResponseEntity<Map<String, Object>> obtenerPrediccion(@PathVariable Long insumoId) {
        Map<String, Object> respuesta = iaClientService.obtenerPrediccion(insumoId);
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Endpoint para obtener la precisión actual del modelo de IA.
     * Este dato lo puede devolver directamente el servicio Flask.
     */
    @GetMapping("/precision")
    public ResponseEntity<Map<String, Object>> obtenerPrecisionIA() {
        try {
            Map<String, Object> precisionData = iaClientService.obtenerPrecisionIA();

            if (precisionData.containsKey("error")) {
                return ResponseEntity.badRequest().body(precisionData);
            }

            // Estandarizamos el nombre de la clave a "precision"
            Map<String, Object> response = new HashMap<>();
            response.put("precision", precisionData.get("precision"));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "No se pudo obtener la precisión del modelo: " + e.getMessage())
            );
        }
    }
}
