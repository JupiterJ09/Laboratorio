/**
 * @file solicitud-prueba.ts (VERSIÓN MOCK ADAPTADA)
 * @description Servicio de solicitudes de prueba usando datos mock inline
 * @author AniAra
 * @date 2025-11-21
 */

import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { SolicitudPrueba } from '../models/solicitud-prueba.interface';

// Función helper para generar fechas recientes
function getFechaReciente(dias: number): string {
  const fecha = new Date();
  fecha.setDate(fecha.getDate() - dias);
  return fecha.toISOString();
}

// Datos mock inline
const SOLICITUDES_MOCK: SolicitudPrueba[] = [
  {
    id: 1,
    folio: 'SP-2024-001',
    expediente: 'EXP-20241115-001',
    fechaSolicitud: getFechaReciente(5),
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
    fechaSolicitud: getFechaReciente(4),
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
    fechaSolicitud: getFechaReciente(3),
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
    fechaSolicitud: getFechaReciente(2),
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
    fechaSolicitud: getFechaReciente(1),
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
    fechaSolicitud: getFechaReciente(0.5),
    tipoPrueba: 'Radiografía de Tórax',
    estado: 'PENDIENTE',
    insumos: [
      { nombre: 'Placa radiográfica', cantidadUsada: 2, unidad: 'unidades' }
    ]
  }
];

@Injectable({
  providedIn: 'root'
})
export class SolicitudPruebaService {
  constructor() {
    console.log('✅ SolicitudPruebaService inicializado con datos mock inline');
  }

  /**
   * Obtiene todas las solicitudes de prueba
   */
  public getSolicitudes(): Observable<SolicitudPrueba[]> {
    console.log(`📋 Devolviendo ${SOLICITUDES_MOCK.length} solicitudes de prueba`);
    return of([...SOLICITUDES_MOCK]).pipe(delay(300));
  }

  /**
   * Obtiene una solicitud por ID
   */
  public getSolicitudById(id: number): Observable<SolicitudPrueba | undefined> {
    const solicitud = SOLICITUDES_MOCK.find(s => s.id === id);
    console.log(`🔍 Buscando solicitud ID ${id}:`, solicitud ? 'Encontrada' : 'No encontrada');
    return of(solicitud).pipe(delay(200));
  }

  /**
   * Obtiene solicitudes por estado
   */
  public getSolicitudesPorEstado(estado: string): Observable<SolicitudPrueba[]> {
    const filtradas = SOLICITUDES_MOCK.filter(s => s.estado === estado);
    console.log(`🔍 Solicitudes con estado ${estado}: ${filtradas.length}`);
    return of(filtradas).pipe(delay(300));
  }

  /**
   * Buscar solicitudes por folio
   */
  public buscarPorFolio(folio: string): Observable<SolicitudPrueba | undefined> {
    const solicitud = SOLICITUDES_MOCK.find(s => 
      s.folio.toLowerCase().includes(folio.toLowerCase())
    );
    console.log(`🔍 Buscando folio ${folio}:`, solicitud ? 'Encontrado' : 'No encontrado');
    return of(solicitud).pipe(delay(200));
  }
}