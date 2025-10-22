package com.laboratorio.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @Column(name = "fecha_fabricacion")
    private LocalDate fechaFabricacion;

    @Column(name = "fecha_caducidad", nullable = false)
    private LocalDate fechaCaducidad;

    @Column(name = "cantidad_inicial", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadInicial;

    @Column(name = "cantidad_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidadActual;

    @Column(name = "proveedor", length = 100)
    private String proveedor;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "numero_factura", length = 50)
    private String numeroFactura;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "estado", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'activo'")
    private String estado; // activo, agotado, vencido, retirado

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (estado == null) {
            estado = "activo";
        }
        if (fechaIngreso == null) {
            fechaIngreso = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Método para verificar si el lote está vencido
    public boolean estaVencido() {
        return fechaCaducidad != null && fechaCaducidad.isBefore(LocalDate.now());
    }

    // Método para verificar si está próximo a vencer
    public boolean estaProximoAVencer(int diasUmbral) {
        if (fechaCaducidad == null) {
            return false;
        }
        LocalDate fechaUmbral = LocalDate.now().plusDays(diasUmbral);
        return fechaCaducidad.isBefore(fechaUmbral);
    }
}