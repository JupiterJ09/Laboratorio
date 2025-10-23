/**
 *Archivo alerta.ts
 *servicio para gestionar las alertas, combina peticiones http con mensajes
 *hecho 20/10/25 Alcazardavid, 5.11
 */


import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { ApiService } from './api';
//import { WebSocketService } from './websocket';
import { Alerta } from '../models/alerta.interface';

@Injectable({
  providedIn: 'root'
})
export class AlertaService {
  private apiService = inject(ApiService);
  //private wsService = inject(WebSocketService);
  private endpoint = 'alertas';

  private alertasSubject = new BehaviorSubject<Alerta[]>([]);
  public alertas$ = this.alertasSubject.asObservable();

  constructor() {
      //WebSocketService
      //Conectamos el WebSocket tan pronto como se crea el servicio.
      //this.wsService.connect();

      //Nos suscribimos a las nuevas alertas que llegan del WebSocket.
      //this.wsService.alertas$.subscribe((nuevaAlerta: Alerta) => {
        // Cuando llega una nueva alerta Obtenemos la lista actual de alertas
        //const alertasActuales = this.alertasSubject.getValue();
        //Añadimos la nueva alerta al inicio de la lista
        //Emitimos la nueva lista actualizada a todos los suscriptores
        //this.alertasSubject.next([nuevaAlerta, ...alertasActuales]);
     // });
    }
  public getAlertas(): Observable<Alerta[]> {
      return this.apiService.get<Alerta[]>(this.endpoint).pipe(
        tap(alertas => {
          // "tap" nos deja "espiar" la respuesta sin modificarla
          // Actualizamos nuestro Subject con las alertas de la API
          this.alertasSubject.next(alertas);
        })
      );
    }
  public getAlertasActivas(): Observable<Alerta[]> {
      return this.alertas$.pipe(
        // Usamos 'map' para transformar la lista completa
        map(alertas => alertas.filter(alerta => !alerta.leida))
      );
    }
  public marcarComoLeida(id: number): Observable<Alerta> {
      // Llama a: PUT http://localhost:8080/api/alertas/{id}/leer
      return this.apiService.put<Alerta>(`${this.endpoint}/${id}/leer`, {}).pipe(
        tap(alertaActualizada => {
          // Una vez que la API confirma...
          // 1. Obtenemos la lista actual
          const alertas = this.alertasSubject.getValue();
          // 2. Buscamos y reemplazamos la alerta que cambió
          const indice = alertas.findIndex(a => a.id === id);
          if (indice !== -1) {
            alertas[indice] = alertaActualizada;
          }
          // 3. Emitimos la lista actualizada
          this.alertasSubject.next([...alertas]);
        })
      );
    }
}
