package com.laboratorio.inventario.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;

@Service
public class IAClientService {

    // ✅ URL del backend Flask
    private final String IA_API_URL = "http://localhost:5000/api/prediccion";

    /**
     * Obtiene la predicción de un insumo específico desde Flask.
     * Llama a: GET http://localhost:5000/api/prediccion/predecir/{insumoId}
     */
    public Map<String, Object> obtenerPrediccion(Long insumoId) {
        try {
            String url = IA_API_URL + "/predecir/" + insumoId;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return Map.of("error", "Error al comunicarse con Flask");
            }

        } catch (Exception e) {
            return Map.of("error", "No se pudo conectar al servicio Flask: " + e.getMessage());
        }
    }

    /**
     * Obtiene la precisión del modelo de IA desde Flask.
     * Llama a: GET http://localhost:5000/api/prediccion/precision
     */
    public Map<String, Object> obtenerPrecisionIA() {
        try {
            String url = IA_API_URL + "/precision"; // ✅ CORREGIDO
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> data = response.getBody();
                Object valor = data.get("precision") != null ? data.get("precision") : 0;
                return Map.of("precision", valor);
            } else {
                return Map.of("error", "Error al obtener precisión desde Flask");
            }

        } catch (Exception e) {
            return Map.of("error", "No se pudo conectar al servicio Flask: " + e.getMessage());
        }
    }
}