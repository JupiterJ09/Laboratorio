package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Lote;
import com.laboratorio.inventario.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {

    // Buscar por número de lote
    Optional<Lote> findByNumeroLote(String numeroLote);

    // Buscar lotes por insumo
    List<Lote> findByInsumo(Insumo insumo);

    // Buscar lotes por ID de insumo
    List<Lote> findByInsumoId(Long insumoId);

    // Buscar lotes por estado
    List<Lote> findByEstado(String estado);

    // Buscar lotes activos de un insumo
    List<Lote> findByInsumoIdAndEstado(Long insumoId, String estado);

    // Buscar lotes por proveedor
    List<Lote> findByProveedor(String proveedor);

    // Buscar lotes vencidos
    @Query("SELECT l FROM Lote l WHERE l.fechaCaducidad < :fecha AND l.estado = 'activo' ORDER BY l.fechaCaducidad ASC")
    List<Lote> findLotesVencidos(@Param("fecha") LocalDate fecha);

    // Buscar lotes próximos a vencer
    @Query("SELECT l FROM Lote l WHERE l.fechaCaducidad BETWEEN :fechaInicio AND :fechaFin AND l.estado = 'activo' ORDER BY l.fechaCaducidad ASC")
    List<Lote> findLotesProximosAVencer(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar lotes de un insumo próximos a vencer
    @Query("SELECT l FROM Lote l WHERE l.insumo.id = :insumoId AND l.fechaCaducidad BETWEEN :fechaInicio AND :fechaFin AND l.estado = 'activo' ORDER BY l.fechaCaducidad ASC")
    List<Lote> findLotesPorInsumoProximosAVencer(
            @Param("insumoId") Long insumoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar lotes con stock disponible
    @Query("SELECT l FROM Lote l WHERE l.cantidadActual > 0 AND l.estado = 'activo'")
    List<Lote> findLotesConStock();

    // Buscar lotes agotados
    @Query("SELECT l FROM Lote l WHERE l.cantidadActual = 0 OR l.estado = 'agotado'")
    List<Lote> findLotesAgotados();

    // Buscar lotes por rango de fechas de ingreso
    List<Lote> findByFechaIngresoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Calcular valor total de un lote
    @Query("SELECT SUM(l.cantidadActual * l.precioCompra) FROM Lote l WHERE l.id = :loteId")
    Double calcularValorLote(@Param("loteId") Long loteId);

    // Calcular valor total del inventario por lotes
    @Query("SELECT SUM(l.cantidadActual * l.precioCompra) FROM Lote l WHERE l.estado = 'activo'")
    Double calcularValorTotalLotes();

    // Obtener lotes ordenados por fecha de caducidad (FIFO/FEFO)
    @Query("SELECT l FROM Lote l WHERE l.insumo.id = :insumoId AND l.estado = 'activo' AND l.cantidadActual > 0 ORDER BY l.fechaCaducidad ASC")
    List<Lote> findLotesPorInsumoOrdenadosPorCaducidad(@Param("insumoId") Long insumoId);

    // Verificar si existe un número de lote
    boolean existsByNumeroLote(String numeroLote);

    // Contar lotes por estado
    @Query("SELECT l.estado, COUNT(l) FROM Lote l GROUP BY l.estado")
    List<Object[]> contarLotesPorEstado();

    // Top 10 lotes próximos a vencer
    @Query("SELECT l FROM Lote l WHERE l.estado = 'activo' ORDER BY l.fechaCaducidad ASC")
    List<Lote> findTop10ByOrderByFechaCaducidadAsc();
}