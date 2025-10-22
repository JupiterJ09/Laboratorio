package com.laboratorio.inventario.service;

import com.laboratorio.inventario.dto.LoteCaducidadDTO;
import com.laboratorio.inventario.entity.Lote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoteService {
    
    // CRUD Básico
    List<Lote> listarTodos();
    List<Lote> listarActivos();
    Optional<Lote> obtenerPorId(Long id);
    Lote crear(Lote lote);
    Lote actualizar(Long id, Lote lote);
    void eliminar(Long id);
    
    // Búsquedas
    Optional<Lote> buscarPorNumeroLote(String numeroLote);
    List<Lote> buscarPorInsumo(Long insumoId);
    List<Lote> buscarPorProveedor(String proveedor);
    List<Lote> buscarPorEstado(String estado);
    
    // Alertas de Caducidad
    List<LoteCaducidadDTO> obtenerLotesProximosCaducar(int dias);
    List<LoteCaducidadDTO> obtenerLotesVencidos();
    List<LoteCaducidadDTO> obtenerLotesPorInsumoProximosCaducar(Long insumoId, int dias);
    
    // Gestión de Stock
    List<Lote> obtenerLotesConStock();
    List<Lote> obtenerLotesAgotados();
    List<Lote> obtenerLotesPorInsumoOrdenadosPorCaducidad(Long insumoId);
    
    // Estadísticas
    Long contarLotesActivos();
    Long contarLotesVencidos();
    Long contarLotesProximosVencer(int dias);
    Double calcularValorTotalLotes();
    
    /**
     * Calcular el nivel de alerta de caducidad de un lote
     * @param fechaCaducidad Fecha de caducidad del lote
     * @return String: "vencido", "critico", "medio", "bajo"
     */
    String calcularNivelAlerta(LocalDate fechaCaducidad);
}