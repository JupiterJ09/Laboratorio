/**
 * @file api-mock.service.ts
 * @description Servicio que simula las respuestas del backend usando datos mock
 * @description para mantener la aplicación funcional sin depender del backend real
 * @author AniAra
 * @date 2025-11-21
 */

import { Injectable } from '@angular/core';
import { Observable, of, delay, map } from 'rxjs';
import { Insumo } from '../models/insumo.interface';
import { Alerta, PrioridadAlerta, TipoAlerta } from '../models/alerta.interface';
import { LoteCaducidadDTO } from '../models/lote.interface';
import { SolicitudPrueba } from '../models/solicitud-prueba.interface';
import { RespuestaPrediccion, PrediccionDia } from '../models/prediccion.interface';

@Injectable({
  providedIn: 'root'
})
export class ApiMockService {

  // --- DATOS MOCK PARA INSUMOS ---
  private insumosData: Insumo[] = [
    {
      id: 1,
      nombre: 'Paracetamol 500mg',
      codigoCatalogo: 'MED-001',
      unidadMedida: 'Tabletas',
      cantidadActual: 45,
      cantidadMinima: 100,
      precioUnitario: 0.50,
      proveedor: 'Farmacéutica del Sur',
      ubicacionAlmacen: 'A-01',
      fechaCaducidad: this.getFechaFutura(180),
      lote: 'LT-2024-001',
      categoria: 'Medicamentos',
      descripcion: 'Analgésico y antipirético',
      estado: 'ACTIVO',
      consumoPromedioDiario: 2.5,
      diasStockRestante: 18,
      nivelAlerta: 'CRITICO',
      createdAt: '2024-01-15T10:00:00',
      updatedAt: '2024-11-20T14:30:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 45,
      valorTotal: 22.50
    },
    {
      id: 2,
      nombre: 'Gasas Estériles 10x10cm',
      codigoCatalogo: 'MAT-045',
      unidadMedida: 'Paquetes',
      cantidadActual: 350,
      cantidadMinima: 200,
      precioUnitario: 3.25,
      proveedor: 'Suministros Médicos SA',
      ubicacionAlmacen: 'B-12',
      fechaCaducidad: this.getFechaFutura(365),
      lote: 'LT-2024-089',
      categoria: 'Material de curación',
      descripcion: 'Gasas estériles para curación',
      estado: 'ACTIVO',
      consumoPromedioDiario: 5.2,
      diasStockRestante: 67,
      nivelAlerta: 'NORMAL',
      createdAt: '2024-02-10T08:00:00',
      updatedAt: '2024-11-21T09:15:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 175,
      valorTotal: 1137.50
    },
    {
      id: 3,
      nombre: 'Suero Fisiológico 1000ml',
      codigoCatalogo: 'SOL-012',
      unidadMedida: 'Bolsas',
      cantidadActual: 180,
      cantidadMinima: 150,
      precioUnitario: 4.80,
      proveedor: 'Laboratorios Unidos',
      ubicacionAlmacen: 'C-05',
      fechaCaducidad: this.getFechaFutura(240),
      lote: 'LT-2024-156',
      categoria: 'Soluciones',
      descripcion: 'Solución salina para administración IV',
      estado: 'ACTIVO',
      consumoPromedioDiario: 8.5,
      diasStockRestante: 21,
      nivelAlerta: 'MEDIO',
      createdAt: '2024-03-05T11:20:00',
      updatedAt: '2024-11-21T10:00:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 120,
      valorTotal: 864.00
    },
    {
      id: 4,
      nombre: 'Jeringas Desechables 5ml',
      codigoCatalogo: 'INS-023',
      unidadMedida: 'Unidades',
      cantidadActual: 520,
      cantidadMinima: 300,
      precioUnitario: 0.85,
      proveedor: 'Distribuidora Médica',
      ubicacionAlmacen: 'D-08',
      fechaCaducidad: this.getFechaFutura(540),
      lote: 'LT-2024-234',
      categoria: 'Instrumental',
      descripcion: 'Jeringas estériles de un solo uso',
      estado: 'ACTIVO',
      consumoPromedioDiario: 12.3,
      diasStockRestante: 42,
      nivelAlerta: 'NORMAL',
      createdAt: '2024-04-12T13:45:00',
      updatedAt: '2024-11-21T11:30:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 173,
      valorTotal: 442.00
    },
    {
      id: 5,
      nombre: 'Antibiótico Amoxicilina 500mg',
      codigoCatalogo: 'MED-078',
      unidadMedida: 'Cápsulas',
      cantidadActual: 85,
      cantidadMinima: 200,
      precioUnitario: 1.20,
      proveedor: 'Farmacéutica del Sur',
      ubicacionAlmacen: 'A-03',
      fechaCaducidad: this.getFechaFutura(90),
      lote: 'LT-2024-167',
      categoria: 'Medicamentos',
      descripcion: 'Antibiótico de amplio espectro',
      estado: 'ACTIVO',
      consumoPromedioDiario: 3.8,
      diasStockRestante: 22,
      nivelAlerta: 'CRITICO',
      createdAt: '2024-05-20T09:00:00',
      updatedAt: '2024-11-21T12:00:00',
      estaVencido: false,
      estaProximoAVencer: true,
      porcentajeStock: 42.5,
      valorTotal: 102.00
    },
    {
      id: 6,
      nombre: 'Guantes de Látex Talla M',
      codigoCatalogo: 'PRO-034',
      unidadMedida: 'Cajas',
      cantidadActual: 120,
      cantidadMinima: 80,
      precioUnitario: 15.50,
      proveedor: 'Equipos de Protección SA',
      ubicacionAlmacen: 'E-02',
      fechaCaducidad: this.getFechaFutura(450),
      lote: 'LT-2024-289',
      categoria: 'Protección',
      descripcion: 'Guantes estériles de látex',
      estado: 'ACTIVO',
      consumoPromedioDiario: 4.2,
      diasStockRestante: 28,
      nivelAlerta: 'NORMAL',
      createdAt: '2024-06-08T10:30:00',
      updatedAt: '2024-11-21T13:15:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 150,
      valorTotal: 1860.00
    },
    {
      id: 7,
      nombre: 'Alcohol Gel 70% 500ml',
      codigoCatalogo: 'HIG-011',
      unidadMedida: 'Frascos',
      cantidadActual: 65,
      cantidadMinima: 100,
      precioUnitario: 2.80,
      proveedor: 'Productos de Higiene',
      ubicacionAlmacen: 'F-01',
      fechaCaducidad: this.getFechaFutura(300),
      lote: 'LT-2024-312',
      categoria: 'Higiene',
      descripcion: 'Gel desinfectante para manos',
      estado: 'ACTIVO',
      consumoPromedioDiario: 6.5,
      diasStockRestante: 10,
      nivelAlerta: 'ALTO',
      createdAt: '2024-07-15T11:00:00',
      updatedAt: '2024-11-21T14:00:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 65,
      valorTotal: 182.00
    },
    {
      id: 8,
      nombre: 'Termómetro Digital',
      codigoCatalogo: 'EQU-056',
      unidadMedida: 'Unidades',
      cantidadActual: 28,
      cantidadMinima: 15,
      precioUnitario: 12.00,
      proveedor: 'Equipamiento Médico',
      ubicacionAlmacen: 'G-04',
      fechaCaducidad: null,
      lote: 'LT-2024-401',
      categoria: 'Equipos',
      descripcion: 'Termómetro digital para uso clínico',
      estado: 'ACTIVO',
      consumoPromedioDiario: 0.8,
      diasStockRestante: 35,
      nivelAlerta: 'NORMAL',
      createdAt: '2024-08-22T12:30:00',
      updatedAt: '2024-11-21T15:00:00',
      estaVencido: false,
      estaProximoAVencer: false,
      porcentajeStock: 186,
      valorTotal: 336.00
    }
  ];

  // --- DATOS MOCK PARA ALERTAS ---
  private alertasData: Alerta[] = [
    {
      id: 1,
      tipo: 'STOCK_BAJO',
      prioridad: 'CRITICA',
      titulo: 'Stock Crítico',
      mensaje: 'Paracetamol 500mg ha alcanzado nivel crítico (45 unidades)',
      insumoId: 1,
      insumoNombre: 'Paracetamol 500mg',
      insumoCodigoCatalogo: 'MED-001',
      loteId: null,
      loteNumero: null,
      leida: false,
      fechaCreacion: this.getFechaReciente(2),
      fechaLectura: null,
      usuarioDestinatario: 'admin',
      datosAdicionales: null,
      icono: '⚠️',
      color: 'red',
      esUrgente: true,
      minutosDesdeCreacion: 120
    },
    {
      id: 2,
      tipo: 'CADUCIDAD',
      prioridad: 'ALTA',
      titulo: 'Próximo a Caducar',
      mensaje: 'Antibiótico Amoxicilina 500mg caduca en 90 días',
      insumoId: 5,
      insumoNombre: 'Antibiótico Amoxicilina 500mg',
      insumoCodigoCatalogo: 'MED-078',
      loteId: 5,
      loteNumero: 'LT-2024-167',
      leida: false,
      fechaCreacion: this.getFechaReciente(1),
      fechaLectura: null,
      usuarioDestinatario: 'admin',
      datosAdicionales: null,
      icono: '📅',
      color: 'orange',
      esUrgente: false,
      minutosDesdeCreacion: 1440
    },
    {
      id: 3,
      tipo: 'STOCK_BAJO',
      prioridad: 'ALTA',
      titulo: 'Stock Bajo',
      mensaje: 'Alcohol Gel 70% 500ml está por debajo del mínimo (65/100)',
      insumoId: 7,
      insumoNombre: 'Alcohol Gel 70% 500ml',
      insumoCodigoCatalogo: 'HIG-011',
      loteId: null,
      loteNumero: null,
      leida: false,
      fechaCreacion: this.getFechaReciente(0.5),
      fechaLectura: null,
      usuarioDestinatario: 'admin',
      datosAdicionales: null,
      icono: '📦',
      color: 'orange',
      esUrgente: false,
      minutosDesdeCreacion: 720
    },
    {
      id: 4,
      tipo: 'AGOTAMIENTO_PROXIMO',
      prioridad: 'MEDIA',
      titulo: 'Agotamiento Próximo',
      mensaje: 'Suero Fisiológico 1000ml se agotará en 21 días',
      insumoId: 3,
      insumoNombre: 'Suero Fisiológico 1000ml',
      insumoCodigoCatalogo: 'SOL-012',
      loteId: null,
      loteNumero: null,
      leida: true,
      fechaCreacion: this.getFechaReciente(3),
      fechaLectura: this.getFechaReciente(2),
      usuarioDestinatario: 'admin',
      datosAdicionales: null,
      icono: '⏰',
      color: 'yellow',
      esUrgente: false,
      minutosDesdeCreacion: 4320
    }
  ];

  // --- DATOS MOCK PARA LOTES ---
  private lotesData: LoteCaducidadDTO[] = [
    {
      id: 1,
      numeroLote: 'LT-2024-001',
      insumoNombre: 'Paracetamol 500mg',
      fechaCaducidad: this.getFechaFutura(180),
      cantidadActual: 45,
      diasParaCaducar: 180,
      estaVencido: false,
      proveedor: 'Farmacéutica del Sur',
      nivelAlerta: 'MEDIA'
    },
    {
      id: 2,
      numeroLote: 'LT-2024-089',
      insumoNombre: 'Gasas Estériles 10x10cm',
      fechaCaducidad: this.getFechaFutura(365),
      cantidadActual: 350,
      diasParaCaducar: 365,
      estaVencido: false,
      proveedor: 'Suministros Médicos SA',
      nivelAlerta: 'BAJA'
    },
    {
      id: 3,
      numeroLote: 'LT-2024-156',
      insumoNombre: 'Suero Fisiológico 1000ml',
      fechaCaducidad: this.getFechaFutura(240),
      cantidadActual: 180,
      diasParaCaducar: 240,
      estaVencido: false,
      proveedor: 'Laboratorios Unidos',
      nivelAlerta: 'MEDIA'
    },
    {
      id: 4,
      numeroLote: 'LT-2024-234',
      insumoNombre: 'Jeringas Desechables 5ml',
      fechaCaducidad: this.getFechaFutura(540),
      cantidadActual: 520,
      diasParaCaducar: 540,
      estaVencido: false,
      proveedor: 'Distribuidora Médica',
      nivelAlerta: 'BAJA'
    },
    {
      id: 5,
      numeroLote: 'LT-2024-167',
      insumoNombre: 'Antibiótico Amoxicilina 500mg',
      fechaCaducidad: this.getFechaFutura(90),
      cantidadActual: 85,
      diasParaCaducar: 90,
      estaVencido: false,
      proveedor: 'Farmacéutica del Sur',
      nivelAlerta: 'ALTA'
    },
    {
      id: 6,
      numeroLote: 'LT-2024-289',
      insumoNombre: 'Guantes de Látex Talla M',
      fechaCaducidad: this.getFechaFutura(450),
      cantidadActual: 120,
      diasParaCaducar: 450,
      estaVencido: false,
      proveedor: 'Equipos de Protección SA',
      nivelAlerta: 'BAJA'
    },
    {
      id: 7,
      numeroLote: 'LT-2024-312',
      insumoNombre: 'Alcohol Gel 70% 500ml',
      fechaCaducidad: this.getFechaFutura(300),
      cantidadActual: 65,
      diasParaCaducar: 300,
      estaVencido: false,
      proveedor: 'Productos de Higiene',
      nivelAlerta: 'MEDIA'
    },
    {
      id: 8,
      numeroLote: 'LT-2023-455',
      insumoNombre: 'Vendas Elásticas 10cm',
      fechaCaducidad: this.getFechaFutura(15),
      cantidadActual: 12,
      diasParaCaducar: 15,
      estaVencido: false,
      proveedor: 'Material Médico SA',
      nivelAlerta: 'CRITICA'
    }
  ];

  // --- DATOS MOCK PARA SOLICITUDES DE PRUEBA ---
  private solicitudesData: SolicitudPrueba[] = [
    {
      id: 1,
      folio: 'SP-2024-001',
      expediente: 'EXP-20241115-001',
      fechaSolicitud: this.getFechaReciente(5),
      tipoPrueba: 'Hemograma Completo',
      estado: 'COMPLETADA',
      insumos: [
        { nombre: 'Tubos de ensayo', cantidadUsada: 2, unidad: 'unidades' },
        { nombre: 'Reactivos hemograma', cantidadUsada: 5, unidad: 'ml' }
      ]
    },
    {
      id: 2,
      folio: 'SP-2024-002',
      expediente: 'EXP-20241116-002',
      fechaSolicitud: this.getFechaReciente(4),
      tipoPrueba: 'Glucosa en Sangre',
      estado: 'COMPLETADA',
      insumos: [
        { nombre: 'Tiras reactivas glucosa', cantidadUsada: 1, unidad: 'unidades' },
        { nombre: 'Lancetas', cantidadUsada: 1, unidad: 'unidades' }
      ]
    },
    {
      id: 3,
      folio: 'SP-2024-003',
      expediente: 'EXP-20241117-003',
      fechaSolicitud: this.getFechaReciente(3),
      tipoPrueba: 'Química Sanguínea',
      estado: 'EN_PROCESO',
      insumos: [
        { nombre: 'Suero fisiológico', cantidadUsada: 10, unidad: 'ml' },
        { nombre: 'Jeringas 5ml', cantidadUsada: 2, unidad: 'unidades' }
      ]
    },
    {
      id: 4,
      folio: 'SP-2024-004',
      expediente: 'EXP-20241118-004',
      fechaSolicitud: this.getFechaReciente(2),
      tipoPrueba: 'Prueba de Embarazo',
      estado: 'COMPLETADA',
      insumos: [
        { nombre: 'Kit prueba embarazo', cantidadUsada: 1, unidad: 'unidades' }
      ]
    },
    {
      id: 5,
      folio: 'SP-2024-005',
      expediente: 'EXP-20241119-005',
      fechaSolicitud: this.getFechaReciente(1),
      tipoPrueba: 'Cultivo Bacteriano',
      estado: 'EN_PROCESO',
      insumos: [
        { nombre: 'Medio de cultivo', cantidadUsada: 15, unidad: 'ml' },
        { nombre: 'Hisopos estériles', cantidadUsada: 3, unidad: 'unidades' }
      ]
    },
    {
      id: 6,
      folio: 'SP-2024-006',
      expediente: 'EXP-20241120-006',
      fechaSolicitud: this.getFechaReciente(0.5),
      tipoPrueba: 'Radiografía de Tórax',
      estado: 'PENDIENTE',
      insumos: [
        { nombre: 'Placa radiográfica', cantidadUsada: 2, unidad: 'unidades' }
      ]
    }
  ];

  constructor() { }

  // --- MÉTODOS AUXILIARES ---

  /**
   * Genera una fecha futura a partir de hoy
   * @param dias Número de días a agregar
   */
  private getFechaFutura(dias: number): string {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() + dias);
    return fecha.toISOString().split('T')[0];
  }

  /**
   * Genera una fecha reciente (en el pasado)
   * @param dias Número de días a restar
   */
  private getFechaReciente(dias: number): string {
    const fecha = new Date();
    fecha.setDate(fecha.getDate() - dias);
    return fecha.toISOString();
  }

  // --- MÉTODOS PÚBLICOS PARA INSUMOS ---

  public getInsumosActivos(): Observable<Insumo[]> {
    return of(this.insumosData.filter(i => i.estado === 'ACTIVO')).pipe(
      delay(300) // Simula latencia de red
    );
  }

  public getInsumosStockBajo(): Observable<Insumo[]> {
    return of(this.insumosData.filter(i => 
      (i.cantidadActual ?? 0) <= (i.cantidadMinima ?? 0)
    )).pipe(delay(300));
  }

  public getInsumoById(id: number): Observable<Insumo | undefined> {
    return of(this.insumosData.find(i => i.id === id)).pipe(delay(200));
  }

  // --- MÉTODOS PÚBLICOS PARA ALERTAS ---

  public getAlertas(): Observable<Alerta[]> {
    return of([...this.alertasData]).pipe(delay(300));
  }

  public getAlertasActivas(): Observable<Alerta[]> {
    return of(this.alertasData.filter(a => !a.leida)).pipe(delay(200));
  }

  public marcarAlertaComoLeida(id: number): Observable<Alerta | null> {
    const alerta = this.alertasData.find(a => a.id === id);
    if (alerta) {
      alerta.leida = true;
      alerta.fechaLectura = new Date().toISOString();
      return of({ ...alerta }).pipe(delay(150));
    }
    return of(null).pipe(delay(150));
  }

  public agregarAlerta(alerta: Partial<Alerta>): void {
    const nuevaAlerta: Alerta = {
      id: this.alertasData.length + 1,
      tipo: alerta.tipo || 'SISTEMA',
      prioridad: alerta.prioridad || 'MEDIA',
      titulo: alerta.titulo || 'Nueva Alerta',
      mensaje: alerta.mensaje || '',
      insumoId: alerta.insumoId || null,
      insumoNombre: alerta.insumoNombre || null,
      insumoCodigoCatalogo: alerta.insumoCodigoCatalogo || null,
      loteId: alerta.loteId || null,
      loteNumero: alerta.loteNumero || null,
      leida: false,
      fechaCreacion: new Date().toISOString(),
      fechaLectura: null,
      usuarioDestinatario: alerta.usuarioDestinatario || 'admin',
      datosAdicionales: alerta.datosAdicionales || null,
      icono: alerta.icono || '🔔',
      color: alerta.color || 'blue',
      esUrgente: alerta.esUrgente || false,
      minutosDesdeCreacion: 0
    };
    this.alertasData.unshift(nuevaAlerta);
  }

  // --- MÉTODOS PÚBLICOS PARA LOTES ---

  public getLotesProximosACaducar(dias: number): Observable<LoteCaducidadDTO[]> {
    return of(this.lotesData.filter(l => 
      l.diasParaCaducar <= dias && !l.estaVencido
    )).pipe(delay(300));
  }

  // --- MÉTODOS PÚBLICOS PARA SOLICITUDES ---

  public getSolicitudes(): Observable<SolicitudPrueba[]> {
    return of([...this.solicitudesData]).pipe(delay(300));
  }

  // --- MÉTODOS PÚBLICOS PARA PREDICCIONES ---

  /**
   * Genera una predicción mock para un insumo
   * Simula el comportamiento del servicio Flask
   */
  public getPrediccion(insumoId: number): Observable<RespuestaPrediccion> {
    const insumo = this.insumosData.find(i => i.id === insumoId);
    
    if (!insumo) {
      return of({
        insumo_id: insumoId,
        nombre_insumo: 'Insumo Desconocido',
        proyeccion_30_dias: []
      }).pipe(delay(500));
    }

    // Generar proyección de 30 días
    const proyeccion: PrediccionDia[] = [];
    let stockActual = insumo.cantidadActual ?? 100;
    const consumoDiario = insumo.consumoPromedioDiario ?? 2.5;
    
    for (let dia = 1; dia <= 30; dia++) {
      // Simula variación en el consumo (±20%)
      const variacion = 1 + (Math.random() * 0.4 - 0.2);
      stockActual = Math.max(0, stockActual - (consumoDiario * variacion));
      
      const fecha = new Date();
      fecha.setDate(fecha.getDate() + dia);
      
      proyeccion.push({
        dia: dia,
        fecha: fecha.toISOString().split('T')[0],
        stock_estimado: Math.round(stockActual * 10) / 10
      });
    }

    const respuesta: RespuestaPrediccion = {
      insumo_id: insumoId,
      nombre_insumo: insumo.nombre,
      proyeccion_30_dias: proyeccion
    };

    return of(respuesta).pipe(delay(800)); // Simula tiempo de procesamiento del modelo IA
  }

  /**
   * Devuelve la precisión mock del modelo IA
   */
  public getPrecisionIA(): Observable<{ precision: number }> {
    // Simula una precisión entre 85% y 95%
    const precision = Math.round((85 + Math.random() * 10) * 10) / 10;
    return of({ precision }).pipe(delay(400));
  }
}