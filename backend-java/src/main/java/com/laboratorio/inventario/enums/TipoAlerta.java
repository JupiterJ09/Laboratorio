package com.laboratorio.inventario.enums;

/**
 * Enum para los tipos de alertas del sistema
 */
public enum TipoAlerta {
    STOCK_BAJO("Stock Bajo", "El insumo está por debajo del nivel mínimo"),
    CADUCIDAD("Próximo a Vencer", "El lote está próximo a su fecha de caducidad"),
    VENCIDO("Vencido", "El lote ha superado su fecha de caducidad"),
    AGOTAMIENTO_PROXIMO("Agotamiento Inminente", "El insumo se agotará pronto según el consumo actual"),
    REORDEN_SUGERIDO("Reorden Sugerido", "Se sugiere realizar un pedido de reposición"),
    INVENTARIO_CRITICO("Inventario Crítico", "Múltiples insumos en estado crítico");

    private final String nombre;
    private final String descripcion;

    TipoAlerta(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}