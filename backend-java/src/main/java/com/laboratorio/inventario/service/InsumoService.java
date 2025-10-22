package com.laboratorio.inventario.service;

import com.laboratorio.inventario.dto.InsumoDTO;
import com.laboratorio.inventario.entity.Insumo;

import java.util.List;
import java.util.Optional;

public interface InsumoService {
    
    // CRUD Básico
    List<InsumoDTO> listarTodos();
    List<InsumoDTO> listarActivos();
    Optional<InsumoDTO> obtenerPorId(Long id);
    InsumoDTO crear(Insumo insumo);
    InsumoDTO actualizar(Long id, Insumo insumo);
    void eliminar(Long id);
    
    // Búsquedas
    List<InsumoDTO> buscarPorNombre(String nombre);
    List<InsumoDTO> buscarPorCategoria(String categoria);
    List<InsumoDTO> buscarPorProveedor(String proveedor);
    Optional<InsumoDTO> buscarPorCodigoCatalogo(String codigo);
    
    // Alertas y Monitoreo
    List<InsumoDTO> obtenerInsumosConAlerta();
    List<InsumoDTO> obtenerInsumosBajoMinimo();
    List<InsumoDTO> obtenerInsumosProximosAVencer(int dias);
    List<InsumoDTO> obtenerInsumosVencidos();
    List<InsumoDTO> obtenerInsumosPorStockRestante(int dias);
    
    // Estadísticas
    Long contarInsumosActivos();
    Long contarInsumosConAlerta();
    Long contarInsumosBajoMinimo();
    Double calcularValorTotalInventario();
}