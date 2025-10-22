package com.laboratorio.inventario.service.impl;

import com.laboratorio.inventario.dto.InsumoDTO;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.repository.InsumoRepository;
import com.laboratorio.inventario.service.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsumoServiceImpl implements InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    // ==========================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> listarTodos() {
        return insumoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> listarActivos() {
        return insumoRepository.findByEstadoOrderByNombreAsc("activo")
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsumoDTO> obtenerPorId(Long id) {
        return insumoRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public InsumoDTO crear(Insumo insumo) {
        // Calcular nivel de alerta antes de guardar
        insumo.calcularNivelAlerta();
        
        Insumo insumoGuardado = insumoRepository.save(insumo);
        return convertirADTO(insumoGuardado);
    }

    @Override
    public InsumoDTO actualizar(Long id, Insumo insumoActualizado) {
        return insumoRepository.findById(id)
                .map(insumo -> {
                    // Actualizar campos
                    insumo.setNombre(insumoActualizado.getNombre());
                    insumo.setCodigoCatalogo(insumoActualizado.getCodigoCatalogo());
                    insumo.setUnidadMedida(insumoActualizado.getUnidadMedida());
                    insumo.setCantidadActual(insumoActualizado.getCantidadActual());
                    insumo.setCantidadMinima(insumoActualizado.getCantidadMinima());
                    insumo.setPrecioUnitario(insumoActualizado.getPrecioUnitario());
                    insumo.setProveedor(insumoActualizado.getProveedor());
                    insumo.setUbicacionAlmacen(insumoActualizado.getUbicacionAlmacen());
                    insumo.setFechaCaducidad(insumoActualizado.getFechaCaducidad());
                    insumo.setLote(insumoActualizado.getLote());
                    insumo.setCategoria(insumoActualizado.getCategoria());
                    insumo.setDescripcion(insumoActualizado.getDescripcion());
                    insumo.setEstado(insumoActualizado.getEstado());
                    insumo.setConsumoPromedioDiario(insumoActualizado.getConsumoPromedioDiario());
                    insumo.setDiasStockRestante(insumoActualizado.getDiasStockRestante());
                    
                    // Recalcular nivel de alerta
                    insumo.calcularNivelAlerta();
                    
                    Insumo guardado = insumoRepository.save(insumo);
                    return convertirADTO(guardado);
                })
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con id: " + id));
    }

    @Override
    public void eliminar(Long id) {
        insumoRepository.findById(id)
                .ifPresent(insumo -> {
                    insumo.setEstado("inactivo");
                    insumoRepository.save(insumo);
                });
    }

    // ==========================================
    // MÉTODOS DE BÚSQUEDA
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> buscarPorNombre(String nombre) {
        return insumoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> buscarPorCategoria(String categoria) {
        return insumoRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> buscarPorProveedor(String proveedor) {
        return insumoRepository.findByProveedor(proveedor)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsumoDTO> buscarPorCodigoCatalogo(String codigo) {
        return insumoRepository.findByCodigoCatalogo(codigo)
                .map(this::convertirADTO);
    }

    // ==========================================
    // MÉTODOS DE ALERTAS Y MONITOREO
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> obtenerInsumosConAlerta() {
        return insumoRepository.findInsumosConAlerta()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> obtenerInsumosBajoMinimo() {
        return insumoRepository.findInsumosBajoMinimo()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> obtenerInsumosProximosAVencer(int dias) {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = fechaInicio.plusDays(dias);
        return insumoRepository.findInsumosProximosAVencer(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> obtenerInsumosVencidos() {
        return insumoRepository.findInsumosVencidos(LocalDate.now())
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsumoDTO> obtenerInsumosPorStockRestante(int dias) {
        return insumoRepository.findInsumosPorDiasStockRestante(dias)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // MÉTODOS DE ESTADÍSTICAS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public Long contarInsumosActivos() {
        return (long) insumoRepository.findByEstado("activo").size();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarInsumosConAlerta() {
        return (long) insumoRepository.findInsumosConAlerta().size();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarInsumosBajoMinimo() {
        return (long) insumoRepository.findInsumosBajoMinimo().size();
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularValorTotalInventario() {
        Double valor = insumoRepository.calcularValorTotalInventario();
        return valor != null ? valor : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> calcularValorPorCategoria() {
        List<Object[]> valores = insumoRepository.calcularValorInventarioPorCategoria();
        Map<String, Double> resultado = new java.util.HashMap<>();
        
        for (Object[] row : valores) {
            String categoria = (String) row[0];
            Double valor = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            resultado.put(categoria, valor);
        }
        
        return resultado;
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE CONVERSIÓN
    // ==========================================

    private InsumoDTO convertirADTO(Insumo insumo) {
        InsumoDTO dto = new InsumoDTO();
        
        // Copiar campos básicos
        dto.setId(insumo.getId());
        dto.setNombre(insumo.getNombre());
        dto.setCodigoCatalogo(insumo.getCodigoCatalogo());
        dto.setUnidadMedida(insumo.getUnidadMedida());
        dto.setCantidadActual(insumo.getCantidadActual());
        dto.setCantidadMinima(insumo.getCantidadMinima());
        dto.setPrecioUnitario(insumo.getPrecioUnitario());
        dto.setProveedor(insumo.getProveedor());
        dto.setUbicacionAlmacen(insumo.getUbicacionAlmacen());
        dto.setFechaCaducidad(insumo.getFechaCaducidad());
        dto.setLote(insumo.getLote());
        dto.setCategoria(insumo.getCategoria());
        dto.setDescripcion(insumo.getDescripcion());
        dto.setEstado(insumo.getEstado());
        dto.setConsumoPromedioDiario(insumo.getConsumoPromedioDiario());
        dto.setDiasStockRestante(insumo.getDiasStockRestante());
        dto.setNivelAlerta(insumo.getNivelAlerta());
        dto.setCreatedAt(insumo.getCreatedAt());
        dto.setUpdatedAt(insumo.getUpdatedAt());
        
        // Campos calculados
        dto.setEstaVencido(insumo.estaVencido());
        dto.setEstaProximoAVencer(insumo.estaProximoAVencer(30));
        
        // Calcular porcentaje de stock
        if (insumo.getCantidadActual() != null && insumo.getCantidadMinima() != null 
            && insumo.getCantidadMinima().doubleValue() > 0) {
            double porcentaje = (insumo.getCantidadActual().doubleValue() / 
                               insumo.getCantidadMinima().doubleValue()) * 100;
            dto.setPorcentajeStock(porcentaje);
        }
        
        // Calcular valor total
        if (insumo.getCantidadActual() != null && insumo.getPrecioUnitario() != null) {
            dto.setValorTotal(insumo.getCantidadActual().multiply(insumo.getPrecioUnitario()));
        }
        
        return dto;
    }
}