package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.service.IAClientService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;

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
}
