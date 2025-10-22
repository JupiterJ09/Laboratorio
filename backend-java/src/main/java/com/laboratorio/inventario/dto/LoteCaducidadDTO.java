package com.laboratorio.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteCaducidadDTO {
    
    private Long loteId;
    private String numeroLote;
    private Long insumoId;
    private String insumoNombre;
    private LocalDate fechaCaducidad;
    private BigDecimal cantidadActual;
    private String ubicacion;
    private Integer diasRestantes;
    private String nivelAlerta; // critico, medio, bajo
    
    // Constructor para consultas personalizadas
    public LoteCaducidadDTO(Long loteId, String numeroLote, String insumoNombre, 
                           LocalDate fechaCaducidad, BigDecimal cantidadActual) {
        this.loteId = loteId;
        this.numeroLote = numeroLote;
        this.insumoNombre = insumoNombre;
        this.fechaCaducidad = fechaCaducidad;
        this.cantidadActual = cantidadActual;
        this.diasRestantes = calcularDiasRestantes(fechaCaducidad);
        this.nivelAlerta = calcularNivelAlerta(this.diasRestantes);
    }
    
    private Integer calcularDiasRestantes(LocalDate fechaCaducidad) {
        if (fechaCaducidad == null) return null;
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
    }
    
    private String calcularNivelAlerta(Integer dias) {
        if (dias == null || dias < 0) return "vencido";
        if (dias <= 7) return "critico";
        if (dias <= 30) return "medio";
        return "bajo";
    }
}