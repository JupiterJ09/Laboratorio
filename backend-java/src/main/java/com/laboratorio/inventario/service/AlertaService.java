package com.laboratorio.inventario.service;

import com.laboratorio.inventario.dto.AlertaDTO;
import com.laboratorio.inventario.entity.Alerta;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.entity.Lote;

import java.util.List;
import java.util.Optional;

public interface AlertaService {
    
    // CRUD Básico
    List<AlertaDTO> listarTodas();
    List<AlertaDTO> listarNoLeidas();
    Optional<AlertaDTO> obtenerPorId(Long id);
    AlertaDTO marcarComoLeida(Long id);
    void eliminar(Long id);
    
    // Crear alertas específicas
    AlertaDTO crearAlertaStockBajo(Insumo insumo);
    AlertaDTO crearAlertaCaducidad(Lote lote, int diasRestantes);
    AlertaDTO crearAlertaVencido(Lote lote);
    AlertaDTO crearAlertaAgotamientoProximo(Insumo insumo, int diasEstimados);
    AlertaDTO crearAlertaPersonalizada(String tipo, String prioridad, String titulo, String mensaje, Long insumoId, Long loteId);
    
    // Verificar y generar alertas automáticamente
    List<AlertaDTO> verificarYGenerarAlertas();
    List<AlertaDTO> verificarAlertasStockBajo();
    List<AlertaDTO> verificarAlertasCaducidad();
    List<AlertaDTO> verificarAlertasVencidos();
    List<AlertaDTO> verificarAlertasAgotamiento();
    
    // Búsquedas
    List<AlertaDTO> buscarPorTipo(String tipo);
    List<AlertaDTO> buscarPorPrioridad(String prioridad);
    List<AlertaDTO> buscarPorInsumo(Long insumoId);
    List<AlertaDTO> buscarAlertasUrgentes();
    List<AlertaDTO> buscarAlertasDeHoy();
    
    // Estadísticas
    Long contarNoLeidas();
    Long contarPorPrioridad(String prioridad);
    
    // Limpieza
    void limpiarAlertasAntiguas(int diasAntiguedad);
}