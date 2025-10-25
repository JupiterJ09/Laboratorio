import { Injectable, inject } from '@angular/core';
import { Observable, of } from 'rxjs'; // Importa 'of' para el método simulado
import { ApiService } from './api';
import { Insumo } from '../models/insumo.interface';

@Injectable({
    providedIn: 'root'
  })
export class InsumoService {
    private apiService = inject(ApiService);
    private endpoint = 'insumos'; // Endpoint base

    constructor() { }

    /**
     * Obtiene todos los insumos (activos e inactivos)
     * (Cambié el nombre del método original para claridad)
     */
    public getInsumosTodos(): Observable<Insumo[]> {
        return this.apiService.get<Insumo[]>(this.endpoint);
      }

    /**
     * [NUEVO] Obtiene solo los insumos ACTIVOS
     * (Este es el método que faltaba)
     */
    public getInsumosActivos(): Observable<Insumo[]> {
        return this.apiService.get<Insumo[]>(`${this.endpoint}/activos`);
      }

    /**
     * Obtiene un insumo por su ID
     */
    public getInsumoById(id: number): Observable<Insumo> {
        return this.apiService.get<Insumo>(`${this.endpoint}/${id}`);
      }

    /**
     * Obtiene insumos que están POR DEBAJO DEL MÍNIMO
     */
    public getInsumosStockBajo(): Observable<Insumo[]> {
        // [CORREGIDO] Endpoint correcto del backend
        return this.apiService.get<Insumo[]>(`${this.endpoint}/bajo-minimo`);
      }

    /**
     * [TAREA PENDIENTE EN EL DASHBOARD]
     * Obtiene los insumos próximos a caducar.
     * TODO: Implementar usando LoteService o crear endpoint específico.
     */
    public getProximosACaducar(): Observable<Insumo[]> {
        console.warn('InsumoService.getProximosACaducar() no está implementado correctamente. Devolviendo array vacío.');
        // Puedes llamar a un endpoint real si existe o devolver vacío
        // return this.apiService.get<Insumo[]>(`${this.endpoint}/proximos-vencer?dias=7`);
        return of([]); // Devuelve vacío por ahora
      }

    // Puedes añadir más métodos del backend aquí si los necesitas
    // public getInsumosConAlerta(): Observable<Insumo[]> { ... }
    // public getInsumosVencidos(): Observable<Insumo[]> { ... }
  }
