package com.laboratorio.inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket para el sistema de alertas en tiempo real
 * 
 * Esta clase configura Spring WebSocket con STOMP (Simple Text Oriented Messaging Protocol)
 * para permitir comunicación bidireccional en tiempo real entre el servidor y los clientes.
 * 
 * @author José Aníbal Cabrera Rodas
 * @version 1.0
 */
@Configuration
@EnableWebSocketMessageBroker  // Habilita el broker de mensajes WebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el broker de mensajes
     * 
     * Define dos prefijos importantes:
     * - /topic: Para mensajes broadcast (uno a muchos) - usado para alertas
     * - /app: Para mensajes que van a métodos @MessageMapping en controladores
     * 
     * @param config Registro del broker de mensajes
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un broker simple en memoria para enviar mensajes a los clientes
        // Los clientes se suscriben a /topic/alertas para recibir notificaciones
        config.enableSimpleBroker("/topic");
        
        // Los mensajes que vengan del cliente con destino /app serán
        // enrutados a métodos @MessageMapping en los controladores
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra los endpoints de WebSocket
     * 
     * Define el punto de conexión /ws donde los clientes se conectarán
     * Incluye SockJS como fallback para navegadores que no soporten WebSocket
     * 
     * @param registry Registro de endpoints STOMP
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra el endpoint /ws para conexiones WebSocket
        registry.addEndpoint("/ws")
                // Permite conexiones desde el frontend Angular (localhost:4200)
                .setAllowedOrigins("http://localhost:4200")
                // Habilita SockJS como fallback para navegadores sin soporte WebSocket
                .withSockJS();
    }
}