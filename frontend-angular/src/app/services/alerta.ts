/**
 * @file alerta.service.ts
 * @description Servicio para gestionar alertas usando datos mock
 * @description (Versión optimizada para presentación sin WebSocket)
 * @author AniAra
 * @date 2025-11-21
 */

import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, map, interval } from 'rxjs';
import { ApiMockService } from './api-mock.service';
import { Alerta } from '../models/alerta.interface';

@Injectable({
  providedIn: 'root'
})
export class AlertaService {
  private apiMock = inject(ApiMockService);

  private alertasSubject = new BehaviorSubject<Alerta[]>([]);
  public alertas$ = this.alertasSubject.asObservable();

  constructor() {
    console.log('🚀 AlertaService inicializado con datos mock');
    
    // Cargar alertas iniciales
    this.cargarAlertasIniciales();
    
    // Simular nuevas alertas cada 30 segundos (opcional para demo)
    this.simularAlertasEnTiempoReal();
  }

  /**
   * Cargar alertas iniciales desde el servicio mock
   */
  private cargarAlertasIniciales(): void {
    console.log('📥 Cargando alertas iniciales desde mock...');
    this.apiMock.getAlertas().subscribe({
      next: (alertas) => {
        this.alertasSubject.next(alertas);
        console.log(`✅ ${alertas.length} alertas cargadas desde mock`);
      },
      error: (error) => {
        console.error('❌ Error al cargar alertas mock:', error);
      }
    });
  }

  /**
   * Simula la llegada de nuevas alertas en tiempo real
   * (para hacer la demo más dinámica)
   */
  private simularAlertasEnTiempoReal(): void {
    // Cada 45 segundos, genera una nueva alerta aleatoria
    interval(45000).subscribe(() => {
      const probabilidad = Math.random();
      
      // Solo generar alerta el 40% de las veces
      if (probabilidad < 0.4) {
        const tiposAlerta = [
          {
            tipo: 'STOCK_BAJO' as const,
            prioridad: 'ALTA' as const,
            titulo: 'Stock Bajo Detectado',
            mensaje: 'Se ha detectado stock bajo en un insumo crítico',
            icono: '📦',
            color: 'orange'
          },
          {
            tipo: 'AGOTAMIENTO_PROXIMO' as const,
            prioridad: 'MEDIA' as const,
            titulo: 'Agotamiento Próximo',
            mensaje: 'Un insumo se agotará en los próximos días',
            icono: '⏰',
            color: 'yellow'
          },
          {
            tipo: 'SISTEMA' as const,
            prioridad: 'BAJA' as const,
            titulo: 'Actualización de Sistema',
            mensaje: 'Los datos han sido actualizados correctamente',
            icono: '⚙️',
            color: 'blue'
          }
        ];
        
        const alertaAleatoria = tiposAlerta[Math.floor(Math.random() * tiposAlerta.length)];
        
        this.apiMock.agregarAlerta({
          ...alertaAleatoria,
          esUrgente: alertaAleatoria.prioridad === 'ALTA'
        });
        
        // Recargar alertas para reflejar la nueva
        this.recargar();
        
        console.log('🔔 Nueva alerta simulada generada:', alertaAleatoria.titulo);
      }
    });
  }

  /**
   * Obtener todas las alertas desde el servicio mock
   */
  public getAlertas(): Observable<Alerta[]> {
    return this.apiMock.getAlertas().pipe(
      map(alertas => {
        this.alertasSubject.next(alertas);
        console.log('📊 Alertas actualizadas en el Subject');
        return alertas;
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
    return this.apiMock.marcarAlertaComoLeida(id).pipe(
      map(alertaActualizada => {
        if (alertaActualizada) {
          console.log('✅ Alerta marcada como leída:', id);
          
          // Actualizar la lista local
          const alertas = this.alertasSubject.getValue();
          const indice = alertas.findIndex(a => a.id === id);
          
          if (indice !== -1) {
            alertas[indice] = alertaActualizada;
            this.alertasSubject.next([...alertas]);
            console.log('📊 Lista local actualizada');
          }
          
          return alertaActualizada;
        }
        throw new Error('Alerta no encontrada');
      })
    );
  }

  /**
   * Marcar todas las alertas como leídas
   */
  public marcarTodasComoLeidas(): Observable<void> {
    const alertas = this.alertasSubject.getValue();
    const alertasActualizadas = alertas.map(a => ({ ...a, leida: true }));
    this.alertasSubject.next(alertasActualizadas);
    
    console.log('✅ Todas las alertas marcadas como leídas');
    
    return new Observable(subscriber => {
      subscriber.next();
      subscriber.complete();
    });
  }

  /**
   * Eliminar una alerta
   */
  public eliminarAlerta(id: number): Observable<void> {
    const alertas = this.alertasSubject.getValue();
    const alertasFiltradas = alertas.filter(a => a.id !== id);
    this.alertasSubject.next(alertasFiltradas);
    
    console.log('✅ Alerta eliminada:', id);
    
    return new Observable(subscriber => {
      subscriber.next();
      subscriber.complete();
    });
  }

  /**
   * Recargar alertas desde el servicio mock
   */
  public recargar(): void {
    console.log('🔄 Recargando alertas desde mock...');
    this.getAlertas().subscribe();
  }

  /**
   * Verifica si el "WebSocket" está conectado (siempre true en mock)
   */
  public isWebSocketConectado(): boolean {
    return true; // Siempre conectado en modo mock
  }

  /**
   * Desconectar "WebSocket" (no hace nada en mock)
   */
  public desconectar(): void {
    console.log('ℹ️ Modo mock: no hay WebSocket para desconectar');
  }
}