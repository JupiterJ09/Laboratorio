package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Entrada;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    // Buscar entradas por insumo
    List<Entrada> findByInsumo(Insumo insumo);

    // Buscar entradas por ID de insumo
    List<Entrada> findByInsumoId(Long insumoId);

    // Buscar entradas por proveedor
    List<Entrada> findByProveedor(Proveedor proveedor);

    // Buscar entradas por ID de proveedor
    List<Entrada> findByProveedorId(Long proveedorId);

    // Buscar entradas por rango de fechas
    List<Entrada> findByFechaEntradaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar entradas por número de factura
    List<Entrada> findByNumeroFactura(String numeroFactura);

    // Buscar entradas por número de remisión
    List<Entrada> findByNumeroRemision(String numeroRemision);

    // Buscar entradas por número de orden de compra
    List<Entrada> findByNumeroOrdenCompra(String numeroOrdenCompra);

    // Buscar entradas por número de lote
    List<Entrada> findByNumeroLote(String numeroLote);

    // Buscar entradas por responsable de recepción
    List<Entrada> findByResponsableRecepcion(String responsableRecepcion);

    // Buscar entradas por estado de calidad
    List<Entrada> findByEstadoCalidad(String estadoCalidad);

    // Buscar entradas de un insumo en un rango de fechas
    @Query("SELECT e FROM Entrada e WHERE e.insumo.id = :insumoId AND e.fechaEntrada BETWEEN :fechaInicio AND :fechaFin ORDER BY e.fechaEntrada DESC")
    List<Entrada> findByInsumoIdAndFechaEntradaBetween(
            @Param("insumoId") Long insumoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Obtener entradas recientes (últimos N días)
    @Query("SELECT e FROM Entrada e WHERE e.fechaEntrada >= :fechaDesde ORDER BY e.fechaEntrada DESC, e.createdAt DESC")
    List<Entrada> findEntradasRecientes(@Param("fechaDesde") LocalDate fechaDesde);

    // Calcular total de entradas por insumo
    @Query("SELECT SUM(e.cantidad) FROM Entrada e WHERE e.insumo.id = :insumoId")
    Double calcularTotalEntradasPorInsumo(@Param("insumoId") Long insumoId);

    // Calcular total de entradas por insumo en un rango de fechas
    @Query("SELECT SUM(e.cantidad) FROM Entrada e WHERE e.insumo.id = :insumoId AND e.fechaEntrada BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalEntradasPorInsumoYFecha(
            @Param("insumoId") Long insumoId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Calcular valor total de entradas
    @Query("SELECT SUM(e.precioTotal) FROM Entrada e")
    Double calcularValorTotalEntradas();

    // Calcular valor total de entradas en un rango de fechas
    @Query("SELECT SUM(e.precioTotal) FROM Entrada e WHERE e.fechaEntrada BETWEEN :fechaInicio AND :fechaFin")
    Double calcularValorEntradasPorFecha(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Calcular valor total de entradas por proveedor
    @Query("SELECT SUM(e.precioTotal) FROM Entrada e WHERE e.proveedor.id = :proveedorId")
    Double calcularValorEntradasPorProveedor(@Param("proveedorId") Long proveedorId);

    // Obtener entradas ordenadas por fecha descendente
    List<Entrada> findAllByOrderByFechaEntradaDesc();

    // Top 10 entradas más recientes
    @Query("SELECT e FROM Entrada e ORDER BY e.fechaEntrada DESC, e.createdAt DESC")
    List<Entrada> findTop10ByOrderByFechaEntradaDesc();

    // Contar entradas por proveedor
    @Query("SELECT e.proveedor.nombre, COUNT(e) FROM Entrada e GROUP BY e.proveedor.nombre")
    List<Object[]> contarEntradasPorProveedor();

    // Contar entradas por estado de calidad
    @Query("SELECT e.estadoCalidad, COUNT(e) FROM Entrada e GROUP BY e.estadoCalidad")
    List<Object[]> contarEntradasPorEstadoCalidad();

    // Buscar entradas con lotes próximos a vencer
    @Query("SELECT e FROM Entrada e WHERE e.fechaCaducidad BETWEEN :fechaInicio AND :fechaFin ORDER BY e.fechaCaducidad ASC")
    List<Entrada> findEntradasConLotesProximosAVencer(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar entradas rechazadas
    @Query("SELECT e FROM Entrada e WHERE e.estadoCalidad = 'Rechazado' ORDER BY e.fechaEntrada DESC")
    List<Entrada> findEntradasRechazadas();
}