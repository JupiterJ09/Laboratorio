/**
 * Archivo lote.ts
 * Servicio para gestionar lotes
 * Actualizado: 24/10/2025
 */

import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api';
import { Lote, LoteCaducidadDTO } from '../models/lote.interface'; // ✅ Importar desde la interfaz

@Injectable({
  providedIn: 'root'
})
export class LoteService {
  private apiService = inject(ApiService);
  private endpoint = 'lotes';

  constructor() { }

  // ==========================================
  // CRUD BÁSICO
  // ==========================================

  /**
   * Obtener todos los lotes
   */
  public getAllLotes(): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(this.endpoint);
  }

  /**
   * Obtener lotes activos
   */
  public getLotesActivos(): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(`${this.endpoint}/activos`);
  }

  /**
   * Obtener un lote por ID
   */
  public getLoteById(id: number): Observable<Lote> {
    return this.apiService.get<Lote>(`${this.endpoint}/${id}`);
  }

  // ==========================================
  // ALERTAS DE CADUCIDAD ⭐ IMPORTANTE
  // ==========================================

  /**
   * ✅ Obtener lotes próximos a caducar
   * @param dias - Número de días hacia adelante (por defecto 7)
   */
  public getLotesProximosACaducar(dias: number = 7): Observable<LoteCaducidadDTO[]> {
    console.log(`📦 Llamando a API para lotes próximos a caducar en ${dias} días...`);
    return this.apiService.get<LoteCaducidadDTO[]>(`${this.endpoint}/proximos-caducar?dias=${dias}`);
  }

  /**
   * Obtener lotes vencidos
   */
  public getLotesVencidos(): Observable<LoteCaducidadDTO[]> {
    return this.apiService.get<LoteCaducidadDTO[]>(`${this.endpoint}/vencidos`);
  }

  /**
   * Obtener lotes de un insumo próximos a caducar
   */
  public getLotesPorInsumoProximosCaducar(
    insumoId: number,
    dias: number = 30
  ): Observable<LoteCaducidadDTO[]> {
    return this.apiService.get<LoteCaducidadDTO[]>(
      `${this.endpoint}/caducidad/insumo/${insumoId}?dias=${dias}`
    );
  }

  // ==========================================
  // GESTIÓN DE STOCK
  // ==========================================

  /**
   * Obtener lotes con stock disponible
   */
  public getLotesConStock(): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(`${this.endpoint}/con-stock`);
  }

  /**
   * Obtener lotes agotados
   */
  public getLotesAgotados(): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(`${this.endpoint}/agotados`);
  }

  /**
   * Obtener lotes ordenados por caducidad (FEFO)
   */
  public getLotesOrdenadosPorCaducidad(insumoId: number): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(`${this.endpoint}/insumo/${insumoId}/ordenados`);
  }

  // ==========================================
  // BÚSQUEDAS
  // ==========================================

  /**
   * Buscar lote por número
   */
  public buscarPorNumero(numeroLote: string): Observable<Lote> {
    return this.apiService.get<Lote>(`${this.endpoint}/numero/${numeroLote}`);
  }

  /**
   * Obtener lotes de un insumo
   */
  public getLotesPorInsumo(insumoId: number): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(`${this.endpoint}/insumo/${insumoId}`);
  }

  /**
   * Obtener lotes por proveedor
   */
  public getLotesPorProveedor(proveedor: string): Observable<Lote[]> {
    return this.apiService.get<Lote[]>(`${this.endpoint}/proveedor/${proveedor}`);
  }

  // ==========================================
  // ESTADÍSTICAS
  // ==========================================

  /**
   * Obtener estadísticas de lotes
   */
  public getEstadisticas(): Observable<any> {
    return this.apiService.get(`${this.endpoint}/estadisticas`);
  }
}
