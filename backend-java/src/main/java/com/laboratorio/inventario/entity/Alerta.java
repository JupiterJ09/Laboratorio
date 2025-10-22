package com.laboratorio.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo; // STOCK_BAJO, CADUCIDAD, VENCIDO, AGOTAMIENTO_PROXIMO

    @Column(name = "prioridad", nullable = false, length = 20)
    private String prioridad; // CRITICA, ALTA, MEDIA, BAJA

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "mensaje", columnDefinition = "TEXT")
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @Column(name = "leida", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean leida;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @Column(name = "usuario_destinatario", length = 100)
    private String usuarioDestinatario;

    @Column(name = "datos_adicionales", columnDefinition = "TEXT")
    private String datosAdicionales; // JSON con información extra

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (leida == null) {
            leida = false;
        }
    }

    // Método para marcar como leída
    public void marcarComoLeida() {
        this.leida = true;
        this.fechaLectura = LocalDateTime.now();
    }

    // Método para determinar si es urgente
    public boolean esUrgente() {
        return "CRITICA".equals(prioridad) || "ALTA".equals(prioridad);
    }

    // Método para obtener el ícono según el tipo
    public String getIcono() {
        switch (tipo) {
            case "STOCK_BAJO":
                return "📦";
            case "CADUCIDAD":
                return "⏰";
            case "VENCIDO":
                return "❌";
            case "AGOTAMIENTO_PROXIMO":
                return "⚠️";
            default:
                return "ℹ️";
        }
    }

    // Método para obtener el color según la prioridad
    public String getColor() {
        switch (prioridad) {
            case "CRITICA":
                return "#DC2626"; // Rojo
            case "ALTA":
                return "#EA580C"; // Naranja
            case "MEDIA":
                return "#F59E0B"; // Amarillo
            case "BAJA":
                return "#3B82F6"; // Azul
            default:
                return "#6B7280"; // Gris
        }
    }
}