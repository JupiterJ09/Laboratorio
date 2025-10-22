package com.laboratorio.inventario.controller;

import com.laboratorio.inventario.dto.AlertaDTO;
import com.laboratorio.inventario.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador WebSocket para gestionar alertas en tiempo real
 * 
 * Este controlador maneja la comunicación WebSocket para enviar alertas
 * del sistema de inventario a los clientes conectados en tiempo real.
 * 
 * Utiliza STOMP sobre WebSocket para comunicación bidireccional.
 * 
 * @author José Aníbal Cabrera Rodas
 * @version 1.0
 */
@Controller
public class AlertaWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private AlertaService alertaService;

    /**
     * Envía una alerta a todos los clientes suscritos a /topic/alertas
     * 
     * Este método es llamado internamente por el servicio de alertas
     * cuando se detecta una nueva alerta en el sistema.
     * 
     * @param alerta La alerta DTO que se enviará a los clientes
     */
    public void enviarAlerta(AlertaDTO alerta) {
        // Envía la alerta a todos los clientes suscritos al canal /topic/alertas
        messagingTemplate.convertAndSend("/topic/alertas", alerta);
        
        System.out.println("🔔 Alerta enviada por WebSocket: " + 
                          alerta.getTipo() + " - " + 
                          alerta.getTitulo());
    }

    /**
     * Maneja mensajes entrantes del cliente en /app/alertas
     * y retransmite a todos los clientes en /topic/alertas
     * 
     * Útil para testing y para que clientes puedan disparar alertas
     * 
     * @param alerta Alerta DTO recibida del cliente
     * @return La misma alerta que se enviará a todos los suscritos
     */
    @MessageMapping("/alertas")
    @SendTo("/topic/alertas")
    public AlertaDTO recibirAlerta(AlertaDTO alerta) {
        System.out.println("📨 Mensaje recibido del cliente: " + alerta.getTitulo());
        return alerta;
    }

    /**
     * Endpoint REST para enviar una alerta de prueba manualmente
     * 
     * Útil para testing sin necesitar el cliente WebSocket
     * 
     * POST http://localhost:8080/api/websocket/test
     * 
     * @param titulo Título de la alerta de prueba
     * @param tipo Tipo de alerta (STOCK_BAJO, CADUCIDAD, etc.)
     * @return Mensaje de confirmación
     */
    @PostMapping("/api/websocket/test")
    @ResponseBody
    public String enviarAlertaPrueba(
            @RequestParam(defaultValue = "Alerta de prueba WebSocket") String titulo,
            @RequestParam(defaultValue = "STOCK_BAJO") String tipo,
            @RequestParam(defaultValue = "MEDIA") String prioridad) {
        
        // Crea una alerta personalizada usando el servicio
        AlertaDTO alertaPrueba = alertaService.crearAlertaPersonalizada(
            tipo,
            prioridad,
            titulo,
            "Esta es una alerta de prueba enviada desde el servidor para verificar WebSocket",
            null,  // Sin insumo
            null   // Sin lote
        );
        
        // Envía la alerta por WebSocket
        enviarAlerta(alertaPrueba);
        
        return "✅ Alerta de prueba enviada por WebSocket: " + titulo;
    }

    /**
     * Obtiene todas las alertas no leídas y las envía por WebSocket
     * 
     * Útil para sincronizar clientes que acaban de conectarse
     * 
     * GET http://localhost:8080/api/websocket/alertas/sync
     * 
     * @return Mensaje de confirmación con número de alertas enviadas
     */
    @GetMapping("/api/websocket/alertas/sync")
    @ResponseBody
    public String sincronizarAlertas() {
        List<AlertaDTO> alertasNoLeidas = alertaService.listarNoLeidas();
        
        for (AlertaDTO alerta : alertasNoLeidas) {
            enviarAlerta(alerta);
        }
        
        return "✅ " + alertasNoLeidas.size() + " alertas no leídas enviadas por WebSocket";
    }

    /**
     * Envía un broadcast de prueba a todos los clientes conectados
     * 
     * GET http://localhost:8080/api/websocket/broadcast
     * 
     * @param mensaje Mensaje personalizado para el broadcast
     * @param prioridad Prioridad de la alerta (CRITICA, ALTA, MEDIA, BAJA)
     * @return Confirmación del envío
     */
    @GetMapping("/api/websocket/broadcast")
    @ResponseBody
    public String enviarBroadcast(
            @RequestParam(defaultValue = "Sistema funcionando correctamente") String mensaje,
            @RequestParam(defaultValue = "BAJA") String prioridad) {
        
        AlertaDTO broadcast = alertaService.crearAlertaPersonalizada(
            "SISTEMA",
            prioridad,
            "Notificación del Sistema",
            mensaje,
            null,
            null
        );
        
        messagingTemplate.convertAndSend("/topic/alertas", broadcast);
        
        return "📡 Broadcast enviado a todos los clientes: " + mensaje;
    }

    /**
     * Envía una alerta crítica de emergencia
     * 
     * POST http://localhost:8080/api/websocket/emergencia
     * 
     * @param titulo Título de la emergencia
     * @param mensaje Descripción de la emergencia
     * @return Confirmación del envío
     */
    @PostMapping("/api/websocket/emergencia")
    @ResponseBody
    public String enviarEmergencia(
            @RequestParam String titulo,
            @RequestParam String mensaje) {
        
        AlertaDTO emergencia = alertaService.crearAlertaPersonalizada(
            "AGOTAMIENTO_PROXIMO",
            "CRITICA",
            titulo,
            mensaje,
            null,
            null
        );
        
        // Envía a todos los clientes
        enviarAlerta(emergencia);
        
        return "🚨 EMERGENCIA ENVIADA: " + titulo;
    }
}