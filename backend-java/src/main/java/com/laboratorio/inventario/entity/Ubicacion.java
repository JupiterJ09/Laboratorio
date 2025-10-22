package com.laboratorio.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo; // Ej: A-1-3 (Pasillo A, Estante 1, Nivel 3)

    @Column(nullable = false, length = 100)
    private String nombre; // Ej: "Estante A1 - Medicamentos"

    @Column(name = "tipo", length = 50)
    private String tipo; // refrigerador, estante, gaveta, armario, etc.

    @Column(name = "pasillo", length = 10)
    private String pasillo;

    @Column(name = "estante", length = 10)
    private String estante;

    @Column(name = "nivel", length = 10)
    private String nivel;

    @Column(name = "seccion", length = 50)
    private String seccion;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima; // Número máximo de items

    @Column(name = "ocupacion_actual")
    private Integer ocupacionActual; // Número actual de items

    @Column(name = "temperatura_minima")
    private Double temperaturaMinima; // Para refrigeradores

    @Column(name = "temperatura_maxima")
    private Double temperaturaMaxima; // Para refrigeradores

    @Column(name = "requiere_refrigeracion", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean requiereRefrigeracion;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "activa", columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean activa;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (activa == null) {
            activa = true;
        }
        if (ocupacionActual == null) {
            ocupacionActual = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Método para calcular el porcentaje de ocupación
    public Double getPorcentajeOcupacion() {
        if (capacidadMaxima == null || capacidadMaxima == 0) {
            return 0.0;
        }
        return (ocupacionActual.doubleValue() / capacidadMaxima.doubleValue()) * 100;
    }

    // Método para verificar si está llena
    public boolean estaLlena() {
        if (capacidadMaxima == null) {
            return false;
        }
        return ocupacionActual >= capacidadMaxima;
    }
}