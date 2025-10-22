package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Salida;
import com.laboratorio.inventario.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalidaRepository extends JpaRepository<Salida, Long> {

    // Buscar salidas por insumo
    List<Salida> findByInsumo(Insumo insumo);

    // Buscar salidas por ID de insumo
    List<Salida> findByInsumoId(Long insumoId);

    // Buscar salidas por rango de fechas
    List<Salida> findByFechaSalidaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar salidas por responsable
    List<Salida> findByResponsable(String responsable);

    // Buscar salidas por área destino
    List<Salida> findByAreaDestino(String areaDestino);

    // Buscar salidas por motivo
    List<Salida> findByMotivo(String motivo);

    // Buscar salidas de un insumo en un rango de fechas
    @Query("SELECT s FROM Salida s WHERE s.insumo.id = :insumoId AND s.fechaSalida BETWEEN :fechaInicio AND :fechaFin ORDER BY s.fechaSalida DESC")
    List<Salida> findByInsumoIdAndFechaSalidaBetween(
            @Param("insumoId") Long insumoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Obtener salidas recientes (últimos N días)
    @Query("SELECT s FROM Salida s WHERE s.fechaSalida >= :fechaDesde ORDER BY s.fechaSalida DESC, s.createdAt DESC")
    List<Salida> findSalidasRecientes(@Param("fechaDesde") LocalDate fechaDesde);

    // Calcular total de salidas por insumo
    @Query("SELECT SUM(s.cantidad) FROM Salida s WHERE s.insumo.id = :insumoId")
    Double calcularTotalSalidasPorInsumo(@Param("insumoId") Long insumoId);

    // Calcular total de salidas por insumo en un rango de fechas
    @Query("SELECT SUM(s.cantidad) FROM Salida s WHERE s.insumo.id = :insumoId AND s.fechaSalida BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalSalidasPorInsumoYFecha(
            @Param("insumoId") Long insumoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Obtener salidas ordenadas por fecha descendente
    List<Salida> findAllByOrderByFechaSalidaDesc();

    // Top 10 salidas más recientes
    @Query("SELECT s FROM Salida s ORDER BY s.fechaSalida DESC, s.createdAt DESC")
    List<Salida> findTop10ByOrderByFechaSalidaDesc();

    // Buscar por número de documento
    List<Salida> findByNumeroDocumento(String numeroDocumento);

    // Contar salidas por área destino
    @Query("SELECT s.areaDestino, COUNT(s) FROM Salida s GROUP BY s.areaDestino")
    List<Object[]> contarSalidasPorArea();

    // Contar salidas por motivo
    @Query("SELECT s.motivo, COUNT(s) FROM Salida s GROUP BY s.motivo")
    List<Object[]> contarSalidasPorMotivo();
}