package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

    // Buscar por código
    Optional<Ubicacion> findByCodigo(String codigo);

    // Buscar por nombre
    Optional<Ubicacion> findByNombre(String nombre);

    // Buscar por tipo
    List<Ubicacion> findByTipo(String tipo);

    // Buscar ubicaciones activas
    List<Ubicacion> findByActiva(Boolean activa);

    // Buscar ubicaciones activas ordenadas por código
    List<Ubicacion> findByActivaOrderByCodigoAsc(Boolean activa);

    // Buscar por pasillo
    List<Ubicacion> findByPasillo(String pasillo);

    // Buscar por estante
    List<Ubicacion> findByEstante(String estante);

    // Buscar por nivel
    List<Ubicacion> findByNivel(String nivel);

    // Buscar por sección
    List<Ubicacion> findBySeccion(String seccion);

    // Buscar por pasillo y estante
    List<Ubicacion> findByPasilloAndEstante(String pasillo, String estante);

    // Buscar ubicaciones que requieren refrigeración
    List<Ubicacion> findByRequiereRefrigeracion(Boolean requiereRefrigeracion);

    // Buscar ubicaciones con refrigeración activas
    @Query("SELECT u FROM Ubicacion u WHERE u.requiereRefrigeracion = true AND u.activa = true")
    List<Ubicacion> findUbicacionesConRefrigeracionActivas();

    // Buscar ubicaciones llenas
    @Query("SELECT u FROM Ubicacion u WHERE u.ocupacionActual >= u.capacidadMaxima AND u.activa = true")
    List<Ubicacion> findUbicacionesLlenas();

    // Buscar ubicaciones con espacio disponible
    @Query("SELECT u FROM Ubicacion u WHERE u.ocupacionActual < u.capacidadMaxima AND u.activa = true ORDER BY u.ocupacionActual ASC")
    List<Ubicacion> findUbicacionesConEspacio();

    // Buscar ubicaciones por porcentaje de ocupación
    @Query("SELECT u FROM Ubicacion u WHERE (u.ocupacionActual * 100.0 / u.capacidadMaxima) >= :porcentaje AND u.activa = true ORDER BY u.ocupacionActual DESC")
    List<Ubicacion> findByOcupacionMayorIgual(@Param("porcentaje") Double porcentaje);

    // Verificar si existe código
    boolean existsByCodigo(String codigo);

    // Contar ubicaciones activas
    @Query("SELECT COUNT(u) FROM Ubicacion u WHERE u.activa = true")
    Long contarUbicacionesActivas();

    // Contar ubicaciones por tipo
    @Query("SELECT u.tipo, COUNT(u) FROM Ubicacion u WHERE u.activa = true GROUP BY u.tipo")
    List<Object[]> contarUbicacionesPorTipo();

    // Calcular capacidad total del almacén
    @Query("SELECT SUM(u.capacidadMaxima) FROM Ubicacion u WHERE u.activa = true")
    Long calcularCapacidadTotal();

    // Calcular ocupación total del almacén
    @Query("SELECT SUM(u.ocupacionActual) FROM Ubicacion u WHERE u.activa = true")
    Long calcularOcupacionTotal();

    // Buscar por nombre parcial
    List<Ubicacion> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por código parcial
    List<Ubicacion> findByCodigoContainingIgnoreCase(String codigo);

    // Top ubicaciones más llenas
    @Query("SELECT u FROM Ubicacion u WHERE u.activa = true ORDER BY (u.ocupacionActual * 100.0 / u.capacidadMaxima) DESC")
    List<Ubicacion> findUbicacionesMasLlenas();
}