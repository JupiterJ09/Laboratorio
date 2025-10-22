package com.laboratorio.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaDTO {
    
    private Long id;
    private String tipo;
    private String prioridad;
    private String titulo;
    private String mensaje;
    
    // Información del insumo relacionado
    private Long insumoId;
    private String insumoNombre;
    private String insumoCodigoCatalogo;
    
    // Información del lote relacionado
    private Long loteId;
    private String loteNumero;
    
    // Estado de la alerta
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLectura;
    
    // Usuario
    private String usuarioDestinatario;
    
    // Datos adicionales
    private String datosAdicionales;
    
    // Campos calculados para el frontend
    private String icono;
    private String color;
    private Boolean esUrgente;
    private Long minutosDesdeCreacion;
    
    // Constructor simplificado para alertas básicas
    public AlertaDTO(String tipo, String prioridad, String titulo, String mensaje) {
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now();
        this.leida = false;
    }
}