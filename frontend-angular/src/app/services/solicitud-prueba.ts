/**
 * Servicio para gestionar las solicitudes de prueba.
 */
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api';
import { SolicitudPrueba } from '../models/solicitud-prueba.interface';

@Injectable({
  providedIn: 'root'
})
export class SolicitudPruebaService {
  private apiService = inject(ApiService);
  private endpoint = 'solicitudes-prueba'; // Asumiendo que este es el endpoint de tu backend

  constructor() { }

  /**
   * Obtiene todas las solicitudes de prueba desde el backend.
   */
  public getSolicitudes(): Observable<SolicitudPrueba[]> {
    return this.apiService.get<SolicitudPrueba[]>(this.endpoint);
  }
}