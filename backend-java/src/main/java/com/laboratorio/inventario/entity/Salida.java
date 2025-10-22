package com.laboratorio.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "motivo", length = 100)
    private String motivo; // Uso, Vencimiento, Dañado, etc.

    @Column(name = "responsable", length = 100)
    private String responsable; // Persona que retiró el insumo

    @Column(name = "area_destino", length = 100)
    private String areaDestino; // Área del laboratorio que lo usará

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "numero_documento", length = 50)
    private String numeroDocumento; // Número de orden, requisición, etc.

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (fechaSalida == null) {
            fechaSalida = LocalDate.now();
        }
    }
}