package com.laboratorio.inventario.service;

import com.laboratorio.inventario.dto.ConsumoHistoricoDTO;

import java.time.LocalDate;
import java.util.List;

public interface ConsumoService {
    
    /**
     * Obtener el historial de consumo de un insumo
     * @param insumoId ID del insumo
     * @param dias Número de días hacia atrás (ej: 30, 60, 90)
     * @return Lista de consumos históricos
     */
    List<ConsumoHistoricoDTO> obtenerConsumoHistorico(Long insumoId, int dias);
    
    /**
     * Calcular el promedio de consumo diario de un insumo
     * @param insumoId ID del insumo
     * @param dias Período de días a considerar
     * @return Promedio de consumo diario
     */
    Double calcularPromedioConsumo(Long insumoId, int dias);
    
    /**
     * Obtener la tendencia de consumo (creciente, decreciente, estable)
     * @param insumoId ID del insumo
     * @return String con la tendencia: "creciente", "decreciente", "estable"
     */
    String obtenerTendencia(Long insumoId);
    
    /**
     * Predecir en cuántos días se agotará el stock actual
     * @param insumoId ID del insumo
     * @return Número de días estimados hasta agotar el stock
     */
    Integer predecirDiasHastaAgotamiento(Long insumoId);
    
    /**
     * Obtener consumo por rango de fechas
     * @param insumoId ID del insumo
     * @param fechaInicio Fecha inicial
     * @param fechaFin Fecha final
     * @return Lista de consumos en el rango
     */
    List<ConsumoHistoricoDTO> obtenerConsumoPorRango(Long insumoId, LocalDate fechaInicio, LocalDate fechaFin);
    
    /**
     * Calcular el consumo total de un insumo en un período
     * @param insumoId ID del insumo
     * @param dias Número de días
     * @return Total consumido
     */
    Double calcularConsumoTotal(Long insumoId, int dias);
}