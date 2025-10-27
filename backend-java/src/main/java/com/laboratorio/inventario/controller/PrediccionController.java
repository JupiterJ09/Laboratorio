package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.service.IAClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prediccion")
@CrossOrigin(origins = "*") // Permitir peticiones desde Angular
public class PrediccionController {

    @Autowired
    private IAClientService iaClientService;

    /**
     * Endpoint para obtener la precisión del modelo IA
     * GET http://localhost:8081/api/prediccion/precision
     */
    @GetMapping("/precision")
    public ResponseEntity<Map<String, Object>> obtenerPrecision() {
        try {
            Map<String, Object> resultado = iaClientService.obtenerPrecisionIA();
            
            if (resultado.containsKey("error")) {
                return ResponseEntity.status(500).body(resultado);
            }
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "Error al obtener precisión: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener la predicción de un insumo específico
     * GET http://localhost:8081/api/prediccion/{insumoId}
     */
    @GetMapping("/{insumoId}")
    public ResponseEntity<Map<String, Object>> obtenerPrediccion(@PathVariable Long insumoId) {
        try {
            Map<String, Object> resultado = iaClientService.obtenerPrediccion(insumoId);
            
            if (resultado.containsKey("error")) {
                return ResponseEntity.status(500).body(resultado);
            }
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "Error al obtener predicción: " + e.getMessage()));
        }
    }
}