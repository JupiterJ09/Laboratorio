package com.laboratorio.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "entradas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(name = "cantidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "precio_total", precision = 10, scale = 2)
    private BigDecimal precioTotal;

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "numero_lote", length = 50)
    private String numeroLote;

    @Column(name = "fecha_caducidad")
    private LocalDate fechaCaducidad;

    @Column(name = "numero_factura", length = 50)
    private String numeroFactura;

    @Column(name = "numero_remision", length = 50)
    private String numeroRemision;

    @Column(name = "numero_orden_compra", length = 50)
    private String numeroOrdenCompra;

    @Column(name = "responsable_recepcion", length = 100)
    private String responsableRecepcion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "estado_calidad", length = 50)
    private String estadoCalidad; // Aprobado, Rechazado, En revisión

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (fechaEntrada == null) {
            fechaEntrada = LocalDate.now();
        }
        if (estadoCalidad == null) {
            estadoCalidad = "Aprobado";
        }
        // Calcular precio total automáticamente
        if (cantidad != null && precioUnitario != null) {
            precioTotal = cantidad.multiply(precioUnitario);
        }
    }
}