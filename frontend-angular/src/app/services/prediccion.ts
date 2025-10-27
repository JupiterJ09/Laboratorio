/**
 * @file prediccion.service.ts
 * @description Servicio para interactuar con la API de Predicciones (proxy Java → Flask)
 */

import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api';
import { RespuestaPrediccion } from '../models/prediccion.interface';

@Injectable({
  providedIn: 'root'
})
export class PrediccionService {
  private apiService = inject(ApiService);
  private endpoint = 'prediccion';

  constructor() {}

  /**
   * Obtiene la predicción para un insumo desde el backend Java.
   * Llama a: GET /api/prediccion/{insumoId}
   */
  public getPrediccion(insumoId: number): Observable<RespuestaPrediccion> {
    console.log(`🧠 Llamando a API Java para predicción: ${this.endpoint}/${insumoId}`);
    return this.apiService.get<RespuestaPrediccion>(`${this.endpoint}/${insumoId}`);
  }

  /**
   * Obtiene la precisión actual del modelo de IA desde el backend Java.
   * Llama a: GET /api/prediccion/precision
   */
  public getPrecisionIA(): Observable<{ precision: number }> {
    console.log('📊 Solicitando precisión de IA al backend Java');
    return this.apiService.get<{ precision: number }>(`${this.endpoint}/precision`);
  }
}
