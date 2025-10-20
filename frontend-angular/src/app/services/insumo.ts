import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { Insumo } from '../models/insumo.interface';

@Injectable({
    providedIn: 'root'
  })
export class InsumoService {
    private apiService = inject(ApiService);

    constructor() { }

    public getInsumos(): Observable<Insumo[]> {
        // Usamos el ApiService para hacer la petición GET al endpoint 'insumos'
        return this.apiService.get<Insumo[]>('insumos');
      }
    public getInsumoById(id: number): Observable<Insumo> {
        // Usamos el ApiService y le pasamos el endpoint 'insumos/ID'
        return this.apiService.get<Insumo>(`insumos/${id}`);
      }
    public getInsumosStockBajo(): Observable<Insumo[]> {
        // El endpoint exacto ('insumos/stock-bajo') dependerá de tu API
        return this.apiService.get<Insumo[]>('insumos/stock-bajo');
      }
  }
