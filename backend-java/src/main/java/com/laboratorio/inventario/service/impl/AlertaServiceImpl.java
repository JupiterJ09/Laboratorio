package com.laboratorio.inventario.service.impl;

import com.laboratorio.inventario.dto.AlertaDTO;
import com.laboratorio.inventario.entity.Alerta;
import com.laboratorio.inventario.entity.Insumo;
import com.laboratorio.inventario.entity.Lote;
import com.laboratorio.inventario.repository.AlertaRepository;
import com.laboratorio.inventario.repository.InsumoRepository;
import com.laboratorio.inventario.repository.LoteRepository;
import com.laboratorio.inventario.service.AlertaService;
import com.laboratorio.inventario.service.ConsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertaServiceImpl implements AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private ConsumoService consumoService;

    // ==========================================
    // MÉTODOS CRUD BÁSICOS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> listarTodas() {
        return alertaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> listarNoLeidas() {
        return alertaRepository.findByLeidaFalseOrderByFechaCreacionDesc()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlertaDTO> obtenerPorId(Long id) {
        return alertaRepository.findById(id)
                .map(this::convertirADTO);
    }

    @Override
    public AlertaDTO marcarComoLeida(Long id) {
        return alertaRepository.findById(id)
                .map(alerta -> {
                    alerta.marcarComoLeida();
                    alertaRepository.save(alerta);
                    return convertirADTO(alerta);
                })
                .orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        alertaRepository.deleteById(id);
    }

    // ==========================================
    // CREAR ALERTAS ESPECÍFICAS
    // ==========================================

    @Override
    public AlertaDTO crearAlertaStockBajo(Insumo insumo) {
        // Verificar si ya existe una alerta similar reciente (últimas 24 horas)
        LocalDateTime hace24h = LocalDateTime.now().minusHours(24);
        if (alertaRepository.existeAlertaReciente("STOCK_BAJO", insumo.getId(), hace24h)) {
            return null; // No crear duplicado
        }

        Alerta alerta = new Alerta();
        alerta.setTipo("STOCK_BAJO");
        alerta.setPrioridad("ALTA");
        alerta.setTitulo("Stock Bajo: " + insumo.getNombre());
        alerta.setMensaje(String.format(
                "El insumo '%s' está por debajo del nivel mínimo. Cantidad actual: %.2f, Mínimo requerido: %.2f",
                insumo.getNombre(),
                insumo.getCantidadActual(),
                insumo.getCantidadMinima()
        ));
        alerta.setInsumo(insumo);
        alerta.setLeida(false);

        Alerta guardada = alertaRepository.save(alerta);
        return convertirADTO(guardada);
    }

    @Override
    public AlertaDTO crearAlertaCaducidad(Lote lote, int diasRestantes) {
        String prioridad;
        if (diasRestantes <= 7) {
            prioridad = "CRITICA";
        } else if (diasRestantes <= 15) {
            prioridad = "ALTA";
        } else {
            prioridad = "MEDIA";
        }

        Alerta alerta = new Alerta();
        alerta.setTipo("CADUCIDAD");
        alerta.setPrioridad(prioridad);
        alerta.setTitulo("Lote próximo a vencer: " + lote.getNumeroLote());
        alerta.setMensaje(String.format(
                "El lote '%s' del insumo '%s' vence en %d días. Fecha de caducidad: %s",
                lote.getNumeroLote(),
                lote.getInsumo().getNombre(),
                diasRestantes,
                lote.getFechaCaducidad()
        ));
        alerta.setInsumo(lote.getInsumo());
        alerta.setLote(lote);
        alerta.setLeida(false);

        Alerta guardada = alertaRepository.save(alerta);
        return convertirADTO(guardada);
    }

    @Override
    public AlertaDTO crearAlertaVencido(Lote lote) {
        Alerta alerta = new Alerta();
        alerta.setTipo("VENCIDO");
        alerta.setPrioridad("CRITICA");
        alerta.setTitulo("Lote vencido: " + lote.getNumeroLote());
        alerta.setMensaje(String.format(
                "El lote '%s' del insumo '%s' ha vencido. Fecha de caducidad: %s. Se recomienda retirar del inventario.",
                lote.getNumeroLote(),
                lote.getInsumo().getNombre(),
                lote.getFechaCaducidad()
        ));
        alerta.setInsumo(lote.getInsumo());
        alerta.setLote(lote);
        alerta.setLeida(false);

        Alerta guardada = alertaRepository.save(alerta);
        return convertirADTO(guardada);
    }

    @Override
    public AlertaDTO crearAlertaAgotamientoProximo(Insumo insumo, int diasEstimados) {
        String prioridad;
        if (diasEstimados <= 3) {
            prioridad = "CRITICA";
        } else if (diasEstimados <= 7) {
            prioridad = "ALTA";
        } else {
            prioridad = "MEDIA";
        }

        Alerta alerta = new Alerta();
        alerta.setTipo("AGOTAMIENTO_PROXIMO");
        alerta.setPrioridad(prioridad);
        alerta.setTitulo("Agotamiento inminente: " + insumo.getNombre());
        alerta.setMensaje(String.format(
                "El insumo '%s' se agotará en aproximadamente %d días según el consumo actual. Se recomienda realizar un pedido de reposición.",
                insumo.getNombre(),
                diasEstimados
        ));
        alerta.setInsumo(insumo);
        alerta.setLeida(false);

        Alerta guardada = alertaRepository.save(alerta);
        return convertirADTO(guardada);
    }

    @Override
    public AlertaDTO crearAlertaPersonalizada(String tipo, String prioridad, String titulo, 
                                             String mensaje, Long insumoId, Long loteId) {
        Alerta alerta = new Alerta();
        alerta.setTipo(tipo);
        alerta.setPrioridad(prioridad);
        alerta.setTitulo(titulo);
        alerta.setMensaje(mensaje);
        alerta.setLeida(false);

        if (insumoId != null) {
            insumoRepository.findById(insumoId).ifPresent(alerta::setInsumo);
        }
        if (loteId != null) {
            loteRepository.findById(loteId).ifPresent(alerta::setLote);
        }

        Alerta guardada = alertaRepository.save(alerta);
        return convertirADTO(guardada);
    }

    // ==========================================
    // VERIFICACIÓN Y GENERACIÓN AUTOMÁTICA
    // ==========================================

    @Override
    public List<AlertaDTO> verificarYGenerarAlertas() {
        List<AlertaDTO> alertasGeneradas = new ArrayList<>();

        alertasGeneradas.addAll(verificarAlertasStockBajo());
        alertasGeneradas.addAll(verificarAlertasCaducidad());
        alertasGeneradas.addAll(verificarAlertasVencidos());
        alertasGeneradas.addAll(verificarAlertasAgotamiento());

        return alertasGeneradas;
    }

    @Override
    public List<AlertaDTO> verificarAlertasStockBajo() {
        List<AlertaDTO> alertas = new ArrayList<>();
        List<Insumo> insumosBajoMinimo = insumoRepository.findInsumosBajoMinimo();

        for (Insumo insumo : insumosBajoMinimo) {
            AlertaDTO alerta = crearAlertaStockBajo(insumo);
            if (alerta != null) {
                alertas.add(alerta);
            }
        }

        return alertas;
    }

    @Override
    public List<AlertaDTO> verificarAlertasCaducidad() {
        List<AlertaDTO> alertas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        LocalDate en30Dias = hoy.plusDays(30);

        List<Lote> lotesProximos = loteRepository.findLotesProximosAVencer(hoy, en30Dias);

        for (Lote lote : lotesProximos) {
            long diasRestantes = ChronoUnit.DAYS.between(hoy, lote.getFechaCaducidad());
            AlertaDTO alerta = crearAlertaCaducidad(lote, (int) diasRestantes);
            if (alerta != null) {
                alertas.add(alerta);
            }
        }

        return alertas;
    }

    @Override
    public List<AlertaDTO> verificarAlertasVencidos() {
        List<AlertaDTO> alertas = new ArrayList<>();
        List<Lote> lotesVencidos = loteRepository.findLotesVencidos(LocalDate.now());

        for (Lote lote : lotesVencidos) {
            AlertaDTO alerta = crearAlertaVencido(lote);
            if (alerta != null) {
                alertas.add(alerta);
            }
        }

        return alertas;
    }

    @Override
    public List<AlertaDTO> verificarAlertasAgotamiento() {
        List<AlertaDTO> alertas = new ArrayList<>();
        List<Insumo> insumosActivos = insumoRepository.findByEstado("activo");

        for (Insumo insumo : insumosActivos) {
            Integer diasEstimados = consumoService.predecirDiasHastaAgotamiento(insumo.getId());
            
            if (diasEstimados != null && diasEstimados <= 14) {
                AlertaDTO alerta = crearAlertaAgotamientoProximo(insumo, diasEstimados);
                if (alerta != null) {
                    alertas.add(alerta);
                }
            }
        }

        return alertas;
    }

    // ==========================================
    // BÚSQUEDAS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> buscarPorTipo(String tipo) {
        return alertaRepository.findByTipo(tipo)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> buscarPorPrioridad(String prioridad) {
        return alertaRepository.findByPrioridad(prioridad)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> buscarPorInsumo(Long insumoId) {
        return alertaRepository.findByInsumoId(insumoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> buscarAlertasUrgentes() {
        return alertaRepository.findAlertasUrgentes()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> buscarAlertasDeHoy() {
        return alertaRepository.findAlertasDeHoy()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // ESTADÍSTICAS
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public Long contarNoLeidas() {
        return alertaRepository.countByLeidaFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPorPrioridad(String prioridad) {
        return alertaRepository.countByLeidaFalseAndPrioridad(prioridad);
    }

    // ==========================================
    // LIMPIEZA
    // ==========================================

    @Override
    public void limpiarAlertasAntiguas(int diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);
        alertaRepository.eliminarAlertasAntiguasLeidas(fechaLimite);
    }

    // ==========================================
    // MÉTODOS PRIVADOS DE CONVERSIÓN
    // ==========================================

    private AlertaDTO convertirADTO(Alerta alerta) {
        AlertaDTO dto = new AlertaDTO();
        
        dto.setId(alerta.getId());
        dto.setTipo(alerta.getTipo());
        dto.setPrioridad(alerta.getPrioridad());
        dto.setTitulo(alerta.getTitulo());
        dto.setMensaje(alerta.getMensaje());
        dto.setLeida(alerta.getLeida());
        dto.setFechaCreacion(alerta.getFechaCreacion());
        dto.setFechaLectura(alerta.getFechaLectura());
        dto.setUsuarioDestinatario(alerta.getUsuarioDestinatario());
        dto.setDatosAdicionales(alerta.getDatosAdicionales());

        // Información del insumo
        if (alerta.getInsumo() != null) {
            dto.setInsumoId(alerta.getInsumo().getId());
            dto.setInsumoNombre(alerta.getInsumo().getNombre());
            dto.setInsumoCodigoCatalogo(alerta.getInsumo().getCodigoCatalogo());
        }

        // Información del lote
        if (alerta.getLote() != null) {
            dto.setLoteId(alerta.getLote().getId());
            dto.setLoteNumero(alerta.getLote().getNumeroLote());
        }

        // Campos calculados
        dto.setIcono(alerta.getIcono());
        dto.setColor(alerta.getColor());
        dto.setEsUrgente(alerta.esUrgente());
        
        // Minutos desde creación
        if (alerta.getFechaCreacion() != null) {
            long minutos = ChronoUnit.MINUTES.between(alerta.getFechaCreacion(), LocalDateTime.now());
            dto.setMinutosDesdeCreacion(minutos);
        }

        return dto;
    }
}