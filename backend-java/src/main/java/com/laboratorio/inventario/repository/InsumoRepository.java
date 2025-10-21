package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    // Buscar por código de catálogo
    Optional<Insumo> findByCodigoCatalogo(String codigoCatalogo);

    // Buscar por nombre (ignora mayúsculas/minúsculas)
    List<Insumo> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por categoría
    List<Insumo> findByCategoria(String categoria);

    // Buscar por proveedor
    List<Insumo> findByProveedor(String proveedor);

    // Buscar por estado
    List<Insumo> findByEstado(String estado);

    // Buscar insumos activos
    List<Insumo> findByEstadoOrderByNombreAsc(String estado);

    // Buscar por nivel de alerta
    List<Insumo> findByNivelAlerta(String nivelAlerta);

    // Buscar insumos con nivel crítico o bajo
    @Query("SELECT i FROM Insumo i WHERE i.nivelAlerta IN ('critico', 'bajo') AND i.estado = 'activo' ORDER BY i.nivelAlerta DESC, i.cantidadActual ASC")
    List<Insumo> findInsumosConAlerta();

    // Buscar insumos por debajo del mínimo
    @Query("SELECT i FROM Insumo i WHERE i.cantidadActual < i.cantidadMinima AND i.estado = 'activo'")
    List<Insumo> findInsumosBajoMinimo();

    // Buscar insumos próximos a vencer
    @Query("SELECT i FROM Insumo i WHERE i.fechaCaducidad BETWEEN :fechaInicio AND :fechaFin AND i.estado = 'activo' ORDER BY i.fechaCaducidad ASC")
    List<Insumo> findInsumosProximosAVencer(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar insumos vencidos
    @Query("SELECT i FROM Insumo i WHERE i.fechaCaducidad < :fecha AND i.estado = 'activo'")
    List<Insumo> findInsumosVencidos(@Param("fecha") LocalDate fecha);

    // Buscar insumos por ubicación
    List<Insumo> findByUbicacionAlmacen(String ubicacion);

    // Buscar por lote
    List<Insumo> findByLote(String lote);

    // Contar insumos por categoría
    @Query("SELECT i.categoria, COUNT(i) FROM Insumo i WHERE i.estado = 'activo' GROUP BY i.categoria")
    List<Object[]> contarInsumosPorCategoria();

    // Contar insumos por nivel de alerta
    @Query("SELECT i.nivelAlerta, COUNT(i) FROM Insumo i WHERE i.estado = 'activo' GROUP BY i.nivelAlerta")
    List<Object[]> contarInsumosPorNivelAlerta();

    // Buscar insumos con stock para X días o menos
    @Query("SELECT i FROM Insumo i WHERE i.diasStockRestante <= :dias AND i.estado = 'activo' ORDER BY i.diasStockRestante ASC")
    List<Insumo> findInsumosPorDiasStockRestante(@Param("dias") Integer dias);

    // Obtener valor total del inventario
    @Query("SELECT SUM(i.cantidadActual * i.precioUnitario) FROM Insumo i WHERE i.estado = 'activo'")
    Double calcularValorTotalInventario();

    // Obtener valor del inventario por categoría
    @Query("SELECT i.categoria, SUM(i.cantidadActual * i.precioUnitario) FROM Insumo i WHERE i.estado = 'activo' GROUP BY i.categoria")
    List<Object[]> calcularValorInventarioPorCategoria();

    // Verificar si existe código de catálogo
    boolean existsByCodigoCatalogo(String codigoCatalogo);

    // Buscar insumos por múltiples criterios
    @Query("SELECT i FROM Insumo i WHERE " +
           "(:nombre IS NULL OR LOWER(i.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:categoria IS NULL OR i.categoria = :categoria) AND " +
           "(:proveedor IS NULL OR i.proveedor = :proveedor) AND " +
           "(:nivelAlerta IS NULL OR i.nivelAlerta = :nivelAlerta) AND " +
           "i.estado = 'activo'")
    List<Insumo> buscarConFiltros(
            @Param("nombre") String nombre,
            @Param("categoria") String categoria,
            @Param("proveedor") String proveedor,
            @Param("nivelAlerta") String nivelAlerta
    );

    // Top 10 insumos más consumidos
    @Query("SELECT i FROM Insumo i WHERE i.estado = 'activo' ORDER BY i.consumoPromedioDiario DESC")
    List<Insumo> findTop10ByOrderByConsumoPromedioDiarioDesc();
}