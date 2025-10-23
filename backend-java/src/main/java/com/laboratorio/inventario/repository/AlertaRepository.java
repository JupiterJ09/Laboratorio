package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Alerta;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Alerta
 * 
 * Proporciona métodos CRUD y consultas personalizadas para gestionar alertas.
 * 
 * @author José Aníbal Cabrera Rodas
 * @version 1.0
 */
@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    // ==========================================
    // BÚSQUEDAS POR ESTADO DE LECTURA
    // ==========================================
    
    /**
     * Encuentra alertas por estado de lectura
     */
    List<Alerta> findByLeida(Boolean leida);
    
    /**
     * Encuentra todas las alertas no leídas
     * @return Lista de alertas no leídas
     */
    List<Alerta> findByLeidaFalse();

    /**
     * Buscar alertas no leídas ordenadas por fecha
     */
    List<Alerta> findByLeidaFalseOrderByFechaCreacionDesc();

    // ==========================================
    // BÚSQUEDAS POR TIPO Y PRIORIDAD
    // ==========================================

    /**
     * Encuentra alertas por tipo
     * @param tipo STOCK_BAJO, CADUCIDAD, VENCIDO, AGOTAMIENTO_PROXIMO
     * @return Lista de alertas del tipo especificado
     */
    List<Alerta> findByTipo(String tipo);

    /**
     * Encuentra alertas por prioridad
     * @param prioridad CRITICA, ALTA, MEDIA, BAJA
     * @return Lista de alertas con la prioridad especificada
     */
    List<Alerta> findByPrioridad(String prioridad);

    /**
     * Encuentra alertas críticas no leídas
     * @param prioridad CRITICA
     * @return Lista de alertas críticas pendientes ordenadas
     */
    List<Alerta> findByPrioridadAndLeidaFalseOrderByFechaCreacionDesc(String prioridad);

    /**
     * Encuentra alertas críticas no leídas
     * @param prioridad CRITICA
     * @param leida false
     * @return Lista de alertas críticas pendientes
     */
    List<Alerta> findByPrioridadAndLeidaFalse(String prioridad);

    // ==========================================
    // BÚSQUEDAS POR INSUMO Y LOTE
    // ==========================================

    /**
     * Buscar por insumo (objeto)
     */
    List<Alerta> findByInsumo(Insumo insumo);

    /**
     * Buscar por lote (objeto)
     */
    List<Alerta> findByLote(Lote lote);

    /**
     * Encuentra alertas de un insumo específico
     * @param insumoId ID del insumo
     * @return Lista de alertas del insumo
     */
    List<Alerta> findByInsumoId(Long insumoId);

    /**
     * Encuentra alertas de un lote específico
     * @param loteId ID del lote
     * @return Lista de alertas del lote
     */
    List<Alerta> findByLoteId(Long loteId);

    // ==========================================
    // BÚSQUEDAS POR USUARIO
    // ==========================================

    /**
     * Buscar por usuario destinatario
     */
    List<Alerta> findByUsuarioDestinatario(String usuario);

    /**
     * Buscar alertas no leídas de un usuario
     */
    List<Alerta> findByUsuarioDestinatarioAndLeidaFalseOrderByFechaCreacionDesc(String usuario);

    // ==========================================
    // BÚSQUEDAS POR FECHA
    // ==========================================

    /**
     * Buscar alertas por rango de fechas
     */
    List<Alerta> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar alertas de hoy
     */
    @Query("SELECT a FROM Alerta a WHERE DATE(a.fechaCreacion) = CURRENT_DATE ORDER BY a.fechaCreacion DESC")
    List<Alerta> findAlertasDeHoy();

    // ==========================================
    // CONSULTAS ESPECIALES
    // ==========================================

    /**
     * Buscar alertas urgentes (críticas o altas) no leídas
     */
    @Query("SELECT a FROM Alerta a WHERE a.leida = false AND a.prioridad IN ('CRITICA', 'ALTA') ORDER BY a.fechaCreacion DESC")
    List<Alerta> findAlertasUrgentes();

    // ==========================================
    // CONTADORES
    // ==========================================

    /**
     * Cuenta las alertas no leídas
     * @return Número de alertas pendientes
     */
    Long countByLeidaFalse();

    /**
     * Contar alertas no leídas por prioridad
     */
    @Query("SELECT COUNT(a) FROM Alerta a WHERE a.leida = false AND a.prioridad = :prioridad")
    Long countByLeidaFalseAndPrioridad(@Param("prioridad") String prioridad);

    // ==========================================
    // VERIFICACIÓN DE DUPLICADOS
    // ==========================================

    /**
     * Verifica si existe una alerta reciente del mismo tipo para un insumo
     * (Evita crear alertas duplicadas en las últimas 24 horas)
     * 
     * @param tipo Tipo de alerta (STOCK_BAJO, AGOTAMIENTO_PROXIMO)
     * @param insumoId ID del insumo
     * @param desde Fecha límite para considerar "reciente"
     * @return true si existe una alerta similar reciente
     */
    @Query("SELECT COUNT(a) > 0 FROM Alerta a WHERE a.tipo = :tipo AND a.insumo.id = :insumoId AND a.fechaCreacion > :desde")
    boolean existeAlertaReciente(
            @Param("tipo") String tipo,
            @Param("insumoId") Long insumoId,
            @Param("desde") LocalDateTime desde
    );

    /**
     * Verifica si existe una alerta reciente del mismo tipo para un lote específico
     * (Evita crear alertas duplicadas de caducidad/vencimiento en las últimas 24 horas)
     * 
     * @param tipo Tipo de alerta (CADUCIDAD, VENCIDO)
     * @param loteId ID del lote
     * @param fechaLimite Fecha límite para considerar "reciente"
     * @return true si existe una alerta similar reciente
     */
    @Query("SELECT COUNT(a) > 0 FROM Alerta a WHERE a.tipo = :tipo AND a.lote.id = :loteId AND a.fechaCreacion > :fechaLimite")
    boolean existeAlertaRecienteLote(
            @Param("tipo") String tipo,
            @Param("loteId") Long loteId,
            @Param("fechaLimite") LocalDateTime fechaLimite
    );

    // ==========================================
    // LIMPIEZA
    // ==========================================

    /**
     * Eliminar alertas antiguas leídas
     * (Usado por la tarea programada de limpieza)
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Alerta a WHERE a.leida = true AND a.fechaCreacion < :fecha")
    void eliminarAlertasAntiguasLeidas(@Param("fecha") LocalDateTime fecha);
}