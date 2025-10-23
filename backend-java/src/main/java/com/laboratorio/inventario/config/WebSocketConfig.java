package com.laboratorio.inventario.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket con STOMP
 * 
 * Esta clase configura el servidor WebSocket para permitir comunicación
 * bidireccional en tiempo real entre el servidor y los clientes.
 * 
 * @author José Aníbal Cabrera Rodas
 * @version 2.0 - CORS habilitado para todos los orígenes
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el broker de mensajes
     * 
     * - /topic: Para mensajes broadcast (uno a muchos)
     * - /app: Prefijo para mensajes de clientes al servidor
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitar un broker simple en memoria para mensajes del tipo "pub-sub"
        config.enableSimpleBroker("/topic");
        
        // Prefijo de destino de aplicación para mapear mensajes desde el cliente
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra endpoints STOMP sobre WebSocket
     * 
     * Endpoint: ws://localhost:8081/ws
     * - Usa SockJS como fallback para navegadores sin soporte WebSocket nativo
     * - CORS habilitado para permitir conexiones desde cualquier origen (desarrollo)
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // ⭐ PERMITIR TODOS LOS ORÍGENES (para desarrollo)
                .withSockJS();
        
        // Endpoint adicional sin SockJS (para clientes que soporten WebSocket nativo)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}