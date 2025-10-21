/**
 *archivo prediccion.ts
 *Servicio para gestionar la logica de negocio de las predicciones
 *hecho 20/10/25 Alcazardavid, 5.9
 */

 import { Injectable, inject } from '@angular/core';
 import { Observable } from 'rxjs';
 import { ApiService } from './api.service';
 import { Prediccion } from '../models/prediccion.interface';

 @Injectable({
    providedIn: 'root'
   })
 export class PrediccionService {
    private apiService = inject(ApiService);
    private endpoint = 'prediccion';

    constructor() { }

    public getPrediccion(insumoId: number): Observable<Prediccion> {
        // Esto llamará a: GET http://localhost:8080/api/prediccion/{insumoId}
        return this.apiService.get<Prediccion>(`${this.endpoint}/${insumoId}`);
      }
    public getTopCriticos(): Observable<any[]> { // Puedes cambiar 'any[]' por una interfaz si la tienes
        // Esto llamará a: GET http://localhost:8080/api/prediccion/top-criticos
        return this.apiService.get<any[]>(`${this.endpoint}/top-criticos`);
      }
 }
