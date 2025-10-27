package com.laboratorio.inventario.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;

@Service
public class IAClientService {

    private final String IA_API_URL = "http://127.0.0.1:5000"; // URL del backend Flask

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
}

