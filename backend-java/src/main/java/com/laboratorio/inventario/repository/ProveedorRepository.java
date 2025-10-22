package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    // Buscar por nombre
    Optional<Proveedor> findByNombre(String nombre);

    // Buscar por nombre (ignora mayúsculas)
    Optional<Proveedor> findByNombreIgnoreCase(String nombre);

    // Buscar por RFC
    Optional<Proveedor> findByRfc(String rfc);

    // Buscar por razón social
    Optional<Proveedor> findByRazonSocial(String razonSocial);

    // Buscar por email
    Optional<Proveedor> findByEmail(String email);

    // Buscar proveedores activos
    List<Proveedor> findByActivo(Boolean activo);

    // Buscar proveedores activos ordenados por nombre
    List<Proveedor> findByActivoOrderByNombreAsc(Boolean activo);

    // Buscar por ciudad
    List<Proveedor> findByCiudad(String ciudad);

    // Buscar por estado
    List<Proveedor> findByEstado(String estado);

    // Buscar por país
    List<Proveedor> findByPais(String pais);

    // Buscar por calificación
    List<Proveedor> findByCalificacion(Integer calificacion);

    // Buscar proveedores con calificación mayor o igual a X
    @Query("SELECT p FROM Proveedor p WHERE p.calificacion >= :calificacion AND p.activo = true ORDER BY p.calificacion DESC")
    List<Proveedor> findByCalificacionMayorIgual(@Param("calificacion") Integer calificacion);

    // Buscar por nombre parcial
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por razón social parcial
    List<Proveedor> findByRazonSocialContainingIgnoreCase(String razonSocial);

    // Verificar si existe por RFC
    boolean existsByRfc(String rfc);

    // Verificar si existe por email
    boolean existsByEmail(String email);

    // Obtener proveedores con mejor calificación
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true ORDER BY p.calificacion DESC, p.nombre ASC")
    List<Proveedor> findProveedoresMejorCalificados();

    // Obtener proveedores por tiempo de entrega
    @Query("SELECT p FROM Proveedor p WHERE p.tiempoEntregaDias <= :dias AND p.activo = true ORDER BY p.tiempoEntregaDias ASC")
    List<Proveedor> findByTiempoEntregaMenorIgual(@Param("dias") Integer dias);

    // Contar proveedores activos
    @Query("SELECT COUNT(p) FROM Proveedor p WHERE p.activo = true")
    Long contarProveedoresActivos();

    // Contar proveedores por ciudad
    @Query("SELECT p.ciudad, COUNT(p) FROM Proveedor p WHERE p.activo = true GROUP BY p.ciudad")
    List<Object[]> contarProveedoresPorCiudad();

    // Contar proveedores por calificación
    @Query("SELECT p.calificacion, COUNT(p) FROM Proveedor p WHERE p.activo = true GROUP BY p.calificacion ORDER BY p.calificacion DESC")
    List<Object[]> contarProveedoresPorCalificacion();

    // Top 10 proveedores mejor calificados
    @Query("SELECT p FROM Proveedor p WHERE p.activo = true ORDER BY p.calificacion DESC, p.nombre ASC")
    List<Proveedor> findTop10ByOrderByCalificacionDesc();
}