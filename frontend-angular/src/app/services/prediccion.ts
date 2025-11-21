/**
 * @file prediccion.ts (VERSIÓN MOCK ADAPTADA)
 * @description Servicio de predicciones usando datos mock inline
 * @author AniAra
 * @date 2025-11-21
 */

import { Injectable } from '@angular/core';
import { Observable, of, delay } from 'rxjs';
import { RespuestaPrediccion, PrediccionDia } from '../models/prediccion.interface';

// Datos de insumos para generar predicciones
const INSUMOS_INFO: any[] = [
  { id: 1, nombre: 'Paracetamol 500mg', stockActual: 45, consumoDiario: 2.5 },
  { id: 2, nombre: 'Gasas Estériles 10x10cm', stockActual: 350, consumoDiario: 5.2 },
  { id: 3, nombre: 'Suero Fisiológico 1000ml', stockActual: 180, consumoDiario: 8.5 },
  { id: 4, nombre: 'Jeringas Desechables 5ml', stockActual: 520, consumoDiario: 12.3 },
  { id: 5, nombre: 'Antibiótico Amoxicilina 500mg', stockActual: 85, consumoDiario: 3.8 },
  { id: 6, nombre: 'Guantes de Látex Talla M', stockActual: 120, consumoDiario: 4.2 },
  { id: 7, nombre: 'Alcohol Gel 70% 500ml', stockActual: 65, consumoDiario: 6.5 },
  { id: 8, nombre: 'Termómetro Digital', stockActual: 28, consumoDiario: 0.8 }
];

@Injectable({
  providedIn: 'root'
})
export class PrediccionService {
  constructor() {
    console.log('✅ PrediccionService inicializado con datos mock inline');
  }

  /**
   * Obtiene la predicción de demanda para un insumo específico
   * @param insumoId ID del insumo
   */
  public getPrediccion(insumoId: number): Observable<RespuestaPrediccion> {
    console.log(`📊 Generando predicción para insumo #${insumoId}...`);

    const insumo = INSUMOS_INFO.find(i => i.id === insumoId);
    
    if (!insumo) {
      console.warn(`⚠️ Insumo #${insumoId} no encontrado`);
      return of({
        insumo_id: insumoId,
        nombre_insumo: 'Insumo Desconocido',
        proyeccion_30_dias: []
      }).pipe(delay(500));
    }

    // Generar proyección de 30 días
    const proyeccion: PrediccionDia[] = [];
    let stockActual = insumo.stockActual;
    const consumoDiario = insumo.consumoDiario;
    
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

    console.log(`✅ Predicción generada: ${proyeccion.length} días para ${insumo.nombre}`);
    return of(respuesta).pipe(delay(800));
  }

  /**
   * Devuelve la precisión del modelo de IA
   */
  public getPrecisionIA(): Observable<{ precision: number }> {
    // Simula una precisión entre 85% y 95%
    const precision = Math.round((85 + Math.random() * 10) * 10) / 10;
    console.log(`🎯 Precisión del modelo IA: ${precision}%`);
    return of({ precision }).pipe(delay(400));
  }
}