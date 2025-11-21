/**
 * @file insumo.ts (VERSIÓN MOCK ADAPTADA)
 * @description Servicio de insumos usando ApiMockService directamente
 * @author AniAra
 * @date 2025-11-21
 */

import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { Insumo } from '../models/insumo.interface';

// Datos mock inline (sin depender de ApiMockService externo)
const INSUMOS_MOCK: Insumo[] = [
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
    fechaCaducidad: getFechaFutura(180),
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
    fechaCaducidad: getFechaFutura(365),
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
    fechaCaducidad: getFechaFutura(240),
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
    fechaCaducidad: getFechaFutura(540),
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
    fechaCaducidad: getFechaFutura(90),
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
    fechaCaducidad: getFechaFutura(450),
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
    fechaCaducidad: getFechaFutura(300),
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

// Función helper para generar fechas futuras
function getFechaFutura(dias: number): string {
  const fecha = new Date();
  fecha.setDate(fecha.getDate() + dias);
  return fecha.toISOString().split('T')[0];
}

@Injectable({
  providedIn: 'root'
})
export class InsumoService {
  constructor() {
    console.log('✅ InsumoService inicializado con datos mock inline');
  }

  /**
   * Obtiene todos los insumos (activos e inactivos)
   */
  public getInsumosTodos(): Observable<Insumo[]> {
    console.log('📦 Devolviendo todos los insumos mock');
    return of([...INSUMOS_MOCK]).pipe(delay(300));
  }

  /**
   * Obtiene solo los insumos ACTIVOS
   */
  public getInsumosActivos(): Observable<Insumo[]> {
    const activos = INSUMOS_MOCK.filter(i => i.estado === 'ACTIVO');
    console.log(`✅ Devolviendo ${activos.length} insumos activos`);
    return of(activos).pipe(delay(300));
  }

  /**
   * Obtiene un insumo por su ID
   */
  public getInsumoById(id: number): Observable<Insumo | undefined> {
    const insumo = INSUMOS_MOCK.find(i => i.id === id);
    console.log(`🔍 Buscando insumo ID ${id}:`, insumo ? 'Encontrado' : 'No encontrado');
    return of(insumo).pipe(delay(200));
  }

  /**
   * Obtiene insumos que están POR DEBAJO DEL MÍNIMO
   */
  public getInsumosStockBajo(): Observable<Insumo[]> {
    const stockBajo = INSUMOS_MOCK.filter(i => 
      (i.cantidadActual ?? 0) <= (i.cantidadMinima ?? 0)
    );
    console.log(`⚠️ Devolviendo ${stockBajo.length} insumos con stock bajo`);
    return of(stockBajo).pipe(delay(300));
  }

  /**
   * Obtiene los insumos próximos a caducar
   */
  public getProximosACaducar(): Observable<Insumo[]> {
    const proximosCaducar = INSUMOS_MOCK.filter(i => i.estaProximoAVencer === true);
    console.log(`📅 Devolviendo ${proximosCaducar.length} insumos próximos a caducar`);
    return of(proximosCaducar).pipe(delay(300));
  }
}