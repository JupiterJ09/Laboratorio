package com.laboratorio.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDTO {
    
    // Estadísticas generales
    private Long totalInsumos;
    private Long insumosActivos;
    private Long insumosConAlerta;
    private Long insumosBajoMinimo;
    private Long insumosCriticos;
    
    // Valores monetarios
    private Double valorTotalInventario;
    private Double valorInsumosVencidos;
    private Double valorInsumosBajoMinimo;
    
    // Caducidad
    private Long lotesVencidos;
    private Long lotesProximosVencer; // 30 días
    private Long lotesProximosVencer7Dias;
    
    // Consumo
    private Double consumoPromedioDiario;
    private Double consumoMensual;
    
    // Movimientos
    private Long entradasMes;
    private Long salidasMes;
    
    // Distribución
    private Map<String, Long> insumosPorCategoria;
    private Map<String, Long> insumosPorNivelAlerta;
    private Map<String, Double> valorPorCategoria;
    
    // Proveedores
    private Long totalProveedores;
    private Long proveedoresActivos;
    
    // Ubicaciones
    private Long totalUbicaciones;
    private Double porcentajeOcupacionAlmacen;
}