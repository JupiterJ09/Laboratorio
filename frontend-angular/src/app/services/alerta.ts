/**
 * Archivo alerta.service.ts
 * Servicio para gestionar las alertas, combina peticiones HTTP con mensajes WebSocket
 * Actualizado: 24/10/2025
 */

import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { ApiService } from './api';
import { WebSocketService } from './websocket';
import { Alerta } from '../models/alerta.interface';

@Injectable({
  providedIn: 'root'
})
export class AlertaService {
  private apiService = inject(ApiService);
  private wsService = inject(WebSocketService); // ✅ Activado
  private endpoint = 'alertas';

  private alertasSubject = new BehaviorSubject<Alerta[]>([]);
  public alertas$ = this.alertasSubject.asObservable();

  constructor() {
    console.log('🚀 AlertaService inicializado');

    // ✅ Conectar WebSocket al iniciar el servicio
    this.wsService.connect();

    // ✅ Suscribirse a las nuevas alertas del WebSocket
    this.wsService.alertas$.subscribe({
      next: (nuevaAlerta: Alerta) => {
        console.log('🔔 Nueva alerta recibida por WebSocket:', nuevaAlerta);

        // Obtener la lista actual de alertas
        const alertasActuales = this.alertasSubject.getValue();

        // Verificar si ya existe (evitar duplicados)
        const existe = alertasActuales.some(a => a.id === nuevaAlerta.id);

        if (!existe) {
          // Añadir la nueva alerta al inicio de la lista
          this.alertasSubject.next([nuevaAlerta, ...alertasActuales]);
          console.log('✅ Alerta añadida a la lista');

          // Opcional: Mostrar notificación del navegador
          this.mostrarNotificacionNativa(nuevaAlerta);
        } else {
          console.log('⚠️ Alerta duplicada, no se añade');
        }
      },
      error: (error: any) => {
        console.error('❌ Error en suscripción de alertas:', error);
      }
    });

    // ✅ Cargar alertas iniciales desde la API
    this.cargarAlertasIniciales();
  }

  /**
   * Cargar alertas iniciales al iniciar el servicio
   */
  private cargarAlertasIniciales(): void {
    console.log('📥 Cargando alertas iniciales...');
    this.getAlertas().subscribe({
      next: (alertas) => {
        console.log(`✅ ${alertas.length} alertas cargadas desde la API`);
      },
      error: (error) => {
        console.error('❌ Error al cargar alertas iniciales:', error);
      }
    });
  }

  /**
   * Obtener todas las alertas desde la API
   */
  public getAlertas(): Observable<Alerta[]> {
    return this.apiService.get<Alerta[]>(this.endpoint).pipe(
      tap(alertas => {
        // Actualizar el Subject con las alertas de la API
        this.alertasSubject.next(alertas);
        console.log('📊 Subject actualizado con alertas de la API');
      })
    );
  }

  /**
   * Obtener solo las alertas no leídas (reactivo)
   */
  public getAlertasActivas(): Observable<Alerta[]> {
    return this.alertas$.pipe(
      map(alertas => alertas.filter(alerta => !alerta.leida))
    );
  }

  /**
   * Obtener el conteo de alertas no leídas (reactivo)
   */
  public getConteoNoLeidas(): Observable<number> {
    return this.alertas$.pipe(
      map(alertas => alertas.filter(alerta => !alerta.leida).length)
    );
  }

  /**
   * Marcar una alerta como leída
   */
  public marcarComoLeida(id: number): Observable<Alerta> {
    return this.apiService.put<Alerta>(`${this.endpoint}/${id}/leer`, {}).pipe(
      tap(alertaActualizada => {
        console.log('✅ Alerta marcada como leída:', id);

        // Actualizar la lista local
        const alertas = this.alertasSubject.getValue();
        const indice = alertas.findIndex(a => a.id === id);

        if (indice !== -1) {
          alertas[indice] = alertaActualizada;
          this.alertasSubject.next([...alertas]);
          console.log('📊 Lista local actualizada');
        }
      })
    );
  }

  /**
   * Marcar todas las alertas como leídas
   */
  public marcarTodasComoLeidas(): Observable<void> {
    return this.apiService.put<void>(`${this.endpoint}/leer-todas`, {}).pipe(
      tap(() => {
        console.log('✅ Todas las alertas marcadas como leídas');

        // Actualizar la lista local
        const alertas = this.alertasSubject.getValue();
        const alertasActualizadas = alertas.map(a => ({ ...a, leida: true }));
        this.alertasSubject.next(alertasActualizadas);
      })
    );
  }

  /**
   * Eliminar una alerta
   */
  public eliminarAlerta(id: number): Observable<void> {
    return this.apiService.delete<void>(`${this.endpoint}/${id}`).pipe(
      tap(() => {
        console.log('✅ Alerta eliminada:', id);

        // Remover de la lista local
        const alertas = this.alertasSubject.getValue();
        const alertasFiltradas = alertas.filter(a => a.id !== id);
        this.alertasSubject.next(alertasFiltradas);
      })
    );
  }

  /**
   * Recargar alertas desde la API
   */
  public recargar(): void {
    console.log('🔄 Recargando alertas...');
    this.getAlertas().subscribe();
  }

  /**
   * Verificar si el WebSocket está conectado
   */
  public isWebSocketConectado(): boolean {
    return this.wsService.isConnected();
  }

  /**
   * Desconectar WebSocket (útil para cleanup)
   */
  public desconectar(): void {
    this.wsService.disconnect();
  }

  /**
   * Mostrar notificación nativa del navegador (opcional)
   */
  private mostrarNotificacionNativa(alerta: Alerta): void {
    // Verificar si el navegador soporta notificaciones
    if (!('Notification' in window)) {
      return;
    }

    // Verificar permisos
    if (Notification.permission === 'granted') {
      this.crearNotificacion(alerta);
    } else if (Notification.permission !== 'denied') {
      // Pedir permiso
      Notification.requestPermission().then(permission => {
        if (permission === 'granted') {
          this.crearNotificacion(alerta);
        }
      });
    }
  }

  /**
   * Crear la notificación nativa
   */
  private crearNotificacion(alerta: Alerta): void {
    const icono = alerta.icono || '🔔';

    new Notification(`${icono} ${alerta.titulo}`, {
      body: alerta.mensaje,
      icon: '/assets/icons/notification-icon.png', // Opcional: añade un ícono
      badge: '/assets/icons/badge-icon.png',
      tag: `alerta-${alerta.id}`, // Evita duplicados
      requireInteraction: alerta.esUrgente || false // Mantener visible si es urgente
    });
  }
}
