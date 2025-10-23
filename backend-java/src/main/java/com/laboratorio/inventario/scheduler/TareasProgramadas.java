package com.laboratorio.inventario.scheduler;

import com.laboratorio.inventario.dto.AlertaDTO;
import com.laboratorio.inventario.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tareas programadas para verificación automática de alertas
 * 
 * Esta clase ejecuta periódicamente verificaciones del sistema
 * para detectar y generar alertas automáticamente.
 * 
 * @author José Aníbal Cabrera Rodas
 * @version 1.0
 */
@Component
public class TareasProgramadas {

    @Autowired
    private AlertaService alertaService;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Verifica y genera alertas cada 1 minuto (60000 ms)
     * 
     * Esta tarea ejecuta todas las verificaciones:
     * - Stock bajo
     * - Lotes próximos a caducar
     * - Lotes vencidos
     * - Predicción de agotamiento
     * 
     * Las alertas detectadas se crean automáticamente y se envían por WebSocket
     */
    @Scheduled(fixedRate = 10000) // Cada 1 minuto
    public void verificarAlertasAutomaticamente() {
        String horaEjecucion = LocalDateTime.now().format(formatter);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("⏰ TAREA PROGRAMADA - Verificación de Alertas");
        System.out.println("📅 Fecha/Hora: " + horaEjecucion);
        System.out.println("=".repeat(80));

        try {
            // Ejecutar verificación completa
            List<AlertaDTO> alertasGeneradas = alertaService.verificarYGenerarAlertas();
            
            if (alertasGeneradas.isEmpty()) {
                System.out.println("✅ No se detectaron nuevos problemas");
            } else {
                System.out.println("🔔 Nuevas alertas generadas: " + alertasGeneradas.size());
                
                // Mostrar resumen de alertas generadas
                alertasGeneradas.forEach(alerta -> {
                    System.out.println(String.format("  %s [%s] %s", 
                        alerta.getIcono(), 
                        alerta.getPrioridad(), 
                        alerta.getTitulo()
                    ));
                });
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error en tarea programada: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Verifica solo stock bajo cada 30 segundos (para pruebas rápidas)
     * OPCIONAL: Comentar esta tarea si no se necesita verificación tan frecuente
     */
    // @Scheduled(fixedRate = 30000) // Cada 30 segundos
    public void verificarStockBajoRapido() {
        try {
            List<AlertaDTO> alertas = alertaService.verificarAlertasStockBajo();
            if (!alertas.isEmpty()) {
                System.out.println("⚡ Verificación rápida - Stock bajo detectado: " + alertas.size() + " alertas");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en verificación rápida: " + e.getMessage());
        }
    }

    /**
     * Verifica lotes próximos a vencer cada 5 minutos
     * OPCIONAL: Puedes activar esta tarea si quieres verificaciones específicas más frecuentes
     */
    // @Scheduled(fixedRate = 300000) // Cada 5 minutos
    public void verificarCaducidadFrecuente() {
        try {
            List<AlertaDTO> alertas = alertaService.verificarAlertasCaducidad();
            if (!alertas.isEmpty()) {
                System.out.println("⏰ Verificación de caducidad - Lotes próximos: " + alertas.size() + " alertas");
            }
        } catch (Exception e) {
            System.err.println("❌ Error en verificación de caducidad: " + e.getMessage());
        }
    }

    /**
     * Limpia alertas antiguas cada día a las 2:00 AM
     * Elimina alertas leídas con más de 30 días de antigüedad
     */
    @Scheduled(cron = "0 0 2 * * *") // Todos los días a las 2:00 AM
    public void limpiarAlertasAntiguas() {
        String horaEjecucion = LocalDateTime.now().format(formatter);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🧹 LIMPIEZA AUTOMÁTICA - Alertas Antiguas");
        System.out.println("📅 Fecha/Hora: " + horaEjecucion);
        System.out.println("=".repeat(80));

        try {
            alertaService.limpiarAlertasAntiguas(30); // Eliminar alertas de más de 30 días
            System.out.println("✅ Limpieza completada - Alertas antiguas eliminadas");
        } catch (Exception e) {
            System.err.println("❌ Error en limpieza automática: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * Genera un reporte de estadísticas cada lunes a las 8:00 AM
     * OPCIONAL: Para tener un resumen semanal del sistema
     */
    @Scheduled(cron = "0 0 8 * * MON") // Cada lunes a las 8:00 AM
    public void generarReporteSemanal() {
        String horaEjecucion = LocalDateTime.now().format(formatter);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 REPORTE SEMANAL - Estadísticas de Alertas");
        System.out.println("📅 Fecha/Hora: " + horaEjecucion);
        System.out.println("=".repeat(80));

        try {
            Long totalNoLeidas = alertaService.contarNoLeidas();
            Long criticas = alertaService.contarPorPrioridad("CRITICA");
            Long altas = alertaService.contarPorPrioridad("ALTA");
            Long medias = alertaService.contarPorPrioridad("MEDIA");
            Long bajas = alertaService.contarPorPrioridad("BAJA");
            
            System.out.println("📈 Alertas no leídas: " + totalNoLeidas);
            System.out.println("  🔴 Críticas: " + criticas);
            System.out.println("  🟠 Altas: " + altas);
            System.out.println("  🟡 Medias: " + medias);
            System.out.println("  🔵 Bajas: " + bajas);
            
        } catch (Exception e) {
            System.err.println("❌ Error generando reporte: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=".repeat(80) + "\n");
    }
}