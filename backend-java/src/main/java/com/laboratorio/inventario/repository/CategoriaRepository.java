package com.laboratorio.inventario.repository;

import com.laboratorio.inventario.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Buscar por nombre exacto
    Optional<Categoria> findByNombre(String nombre);

    // Buscar por nombre (ignora mayúsculas/minúsculas)
    Optional<Categoria> findByNombreIgnoreCase(String nombre);

    // Buscar categorías activas
    List<Categoria> findByEstado(String estado);

    // Buscar categorías activas ordenadas por nombre
    List<Categoria> findByEstadoOrderByNombreAsc(String estado);

    // Verificar si existe una categoría por nombre
    boolean existsByNombre(String nombre);

    // Verificar si existe una categoría por nombre (ignorando mayúsculas)
    boolean existsByNombreIgnoreCase(String nombre);

    // Buscar categorías por color
    List<Categoria> findByColorHex(String colorHex);

    // Contar categorías activas
    @Query("SELECT COUNT(c) FROM Categoria c WHERE c.estado = 'activa'")
    Long contarCategoriasActivas();

    // Obtener todas las categorías activas
    @Query("SELECT c FROM Categoria c WHERE c.estado = 'activa' ORDER BY c.nombre ASC")
    List<Categoria> findAllActivas();

    // Buscar por nombre parcial
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}