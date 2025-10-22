package com.laboratorio.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoDTO {
    
    private Long id;
    private String nombre;
    private String codigoCatalogo;
    private String unidadMedida;
    private BigDecimal cantidadActual;
    private BigDecimal cantidadMinima;
    private BigDecimal precioUnitario;
    private String proveedor;
    private String ubicacionAlmacen;
    private LocalDate fechaCaducidad;
    private String lote;
    private String categoria;
    private String descripcion;
    private String estado;
    private BigDecimal consumoPromedioDiario;
    private Integer diasStockRestante;
    private String nivelAlerta;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Campos calculados adicionales para el frontend
    private Boolean estaVencido;
    private Boolean estaProximoAVencer;
    private Double porcentajeStock; // Porcentaje respecto al mínimo
    private BigDecimal valorTotal; // cantidadActual * precioUnitario
}