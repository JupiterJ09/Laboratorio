package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Alerta;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    // Buscar por estado de lectura
    List<Alerta> findByLeida(Boolean leida);

    // Buscar alertas no leídas
    List<Alerta> findByLeidaFalseOrderByFechaCreacionDesc();

    // Buscar por tipo
    List<Alerta> findByTipo(String tipo);

    // Buscar por prioridad
    List<Alerta> findByPrioridad(String prioridad);

    // Buscar por insumo
    List<Alerta> findByInsumo(Insumo insumo);

    // Buscar por lote
    List<Alerta> findByLote(Lote lote);

    // Buscar alertas de un insumo específico
    List<Alerta> findByInsumoId(Long insumoId);

    // Buscar alertas de un lote específico
    List<Alerta> findByLoteId(Long loteId);

    // Buscar por usuario destinatario
    List<Alerta> findByUsuarioDestinatario(String usuario);

    // Buscar alertas no leídas de un usuario
    List<Alerta> findByUsuarioDestinatarioAndLeidaFalseOrderByFechaCreacionDesc(String usuario);

    // Buscar alertas por rango de fechas
    List<Alerta> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Buscar alertas urgentes (críticas o altas) no leídas
    @Query("SELECT a FROM Alerta a WHERE a.leida = false AND a.prioridad IN ('CRITICA', 'ALTA') ORDER BY a.fechaCreacion DESC")
    List<Alerta> findAlertasUrgentes();

    // Contar alertas no leídas
    Long countByLeidaFalse();

    // Contar alertas no leídas por prioridad
    @Query("SELECT COUNT(a) FROM Alerta a WHERE a.leida = false AND a.prioridad = :prioridad")
    Long countByLeidaFalseAndPrioridad(@Param("prioridad") String prioridad);

    // Buscar alertas de hoy
    @Query("SELECT a FROM Alerta a WHERE DATE(a.fechaCreacion) = CURRENT_DATE ORDER BY a.fechaCreacion DESC")
    List<Alerta> findAlertasDeHoy();

    // Buscar alertas críticas no leídas
    List<Alerta> findByPrioridadAndLeidaFalseOrderByFechaCreacionDesc(String prioridad);

    // Verificar si existe una alerta similar reciente (para evitar duplicados)
    @Query("SELECT COUNT(a) > 0 FROM Alerta a WHERE a.tipo = :tipo AND a.insumo.id = :insumoId AND a.fechaCreacion > :desde")
    boolean existeAlertaReciente(
            @Param("tipo") String tipo,
            @Param("insumoId") Long insumoId,
            @Param("desde") LocalDateTime desde
    );

    // Eliminar alertas antiguas leídas
    @Query("DELETE FROM Alerta a WHERE a.leida = true AND a.fechaCreacion < :fecha")
    void eliminarAlertasAntiguasLeidas(@Param("fecha") LocalDateTime fecha);
}