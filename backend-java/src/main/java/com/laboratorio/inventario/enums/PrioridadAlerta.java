package com.laboratorio.inventario.enums;


/**
 * Enum para las prioridades de las alertas
 */
public enum PrioridadAlerta {
    CRITICA("Crítica", "#DC2626", 1),
    ALTA("Alta", "#EA580C", 2),
    MEDIA("Media", "#F59E0B", 3),
    BAJA("Baja", "#3B82F6", 4);

    private final String nombre;
    private final String color;
    private final int nivel;

    PrioridadAlerta(String nombre, String color, int nivel) {
        this.nombre = nombre;
        this.color = color;
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean esMasPrioritaria(PrioridadAlerta otra) {
        return this.nivel < otra.nivel;
    }
}