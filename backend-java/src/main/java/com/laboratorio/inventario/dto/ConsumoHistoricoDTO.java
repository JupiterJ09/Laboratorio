package com.laboratorio.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumoHistoricoDTO {
    
    private Long insumoId;
    private String insumoNombre;
    private LocalDate fecha;
    private BigDecimal cantidadConsumida;
    private String motivo;
    private String responsable;
    private String areaDestino;
    
    // Constructor simplificado para consultas
    public ConsumoHistoricoDTO(LocalDate fecha, BigDecimal cantidadConsumida) {
        this.fecha = fecha;
        this.cantidadConsumida = cantidadConsumida;
    }
}