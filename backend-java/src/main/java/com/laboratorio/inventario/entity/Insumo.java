package com.laboratorio.inventario.entity;
//arhivos de importaciones 
//jakarta para la bd, lombok para generar codigo automaticamente
//java math y time es para numeros decimales precisos y para la hora
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "codigo_catalogo", unique = true, length = 50)
    private String codigoCatalogo;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "cantidad_actual", precision = 10, scale = 2)
    private BigDecimal cantidadActual;

    @Column(name = "cantidad_minima", precision = 10, scale = 2)
    private BigDecimal cantidadMinima;

    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(length = 50)
    private String proveedor;

    @Column(name = "ubicacion_almacen", length = 100)
    private String ubicacionAlmacen;

    @Column(name = "fecha_caducidad")
    private LocalDate fechaCaducidad;

    @Column(name = "lote", length = 50)
    private String lote;

    @Column(length = 20)
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'activo'")
    private String estado;

    @Column(name = "consumo_promedio_diario", precision = 10, scale = 2)
    private BigDecimal consumoPromedioDiario;

    @Column(name = "dias_stock_restante")
    private Integer diasStockRestante;

    @Column(name = "nivel_alerta", length = 20)
    private String nivelAlerta;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //Se ejecuta antes de insertar en la bd
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (estado == null) {
            estado = "activo";
        }
    }

    //Este metodo se lleva acabo antes de cada actualizacion
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Método auxiliar para calcular el nivel de alerta
    public void calcularNivelAlerta() {
        if (cantidadActual == null || cantidadMinima == null) {
            this.nivelAlerta = "normal";
            return;
        }

        BigDecimal porcentaje = cantidadActual
        .divide(cantidadMinima, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0));

        if (porcentaje.compareTo(BigDecimal.valueOf(25)) <= 0) {
            this.nivelAlerta = "critico";
        } else if (porcentaje.compareTo(BigDecimal.valueOf(50)) <= 0) {
            this.nivelAlerta = "bajo";
        } else {
            this.nivelAlerta = "normal";
        }
    }

    // Método para verificar si está vencido o próximo a vencer
    public boolean estaProximoAVencer(int diasUmbral) {
        if (fechaCaducidad == null) {
            return false;
        }
        LocalDate fechaUmbral = LocalDate.now().plusDays(diasUmbral);
        return fechaCaducidad.isBefore(fechaUmbral);
    }

    public boolean estaVencido() {
        if (fechaCaducidad == null) {
            return false;
        }
        return fechaCaducidad.isBefore(LocalDate.now());
    }
}
