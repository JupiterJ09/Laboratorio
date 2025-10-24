/**
 * Archivo websocket.service.ts
 * Servicio para gestionar la conexión WebSocket con el backend
 * usando @stomp/stompjs sobre SockJS
 * Actualizado: 24/10/2025
 */

import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject, Observable, BehaviorSubject } from 'rxjs';
import { Alerta } from '../models/alerta.interface';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client | null = null;
  private readonly webSocketUrl = 'http://localhost:8081/ws'; // ✅ Puerto correcto

  // Subject para las alertas recibidas
  private alertaSubject = new Subject<Alerta>();
  public alertas$: Observable<Alerta> = this.alertaSubject.asObservable();

  // Subject para el estado de conexión
  private conexionSubject = new BehaviorSubject<boolean>(false);
  public conexion$: Observable<boolean> = this.conexionSubject.asObservable();

  constructor() { }

  /**
   * Conecta al WebSocket del backend
   */
  public connect(): void {
    console.log('🔌 Iniciando conexión WebSocket...');

    // Creamos el cliente STOMP (forma moderna)
    this.stompClient = new Client({
      // Usar SockJS como transporte
      webSocketFactory: () => new SockJS(this.webSocketUrl),

      // Configuración de reconexión automática
      reconnectDelay: 5000, // Reintentar cada 5 segundos
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,

      // Debug (puedes desactivarlo en producción)
      debug: (str) => {
        console.log('📡 STOMP:', str);
      },

      // Callback cuando se conecta exitosamente
      onConnect: (frame) => {
        console.log('✅ WebSocket conectado exitosamente');
        console.log('Frame:', frame);
        this.conexionSubject.next(true);
        this.subscribeToAlerts();
      },

      // Callback en caso de error STOMP
      onStompError: (frame) => {
        console.error('❌ Error STOMP:', frame.headers['message']);
        console.error('Detalles:', frame.body);
        this.conexionSubject.next(false);
      },

      // Callback cuando se desconecta el WebSocket
      onWebSocketClose: (event) => {
        console.warn('⚠️ WebSocket cerrado:', event);
        this.conexionSubject.next(false);
      },

      // Callback cuando se cierra la conexión STOMP
      onDisconnect: () => {
        console.log('🔌 STOMP desconectado');
        this.conexionSubject.next(false);
      }
    });

    // Activar la conexión
    this.stompClient.activate();
  }

  /**
   * Suscribirse al topic de alertas
   */
  private subscribeToAlerts(): void {
    if (!this.stompClient) {
      console.error('❌ No hay cliente STOMP disponible');
      return;
    }

    // ✅ Sin ":" al final
    this.stompClient.subscribe('/topic/alertas', (message: IMessage) => {
      console.log('🔔 Alerta recibida:', message.body);

      try {
        const alerta: Alerta = JSON.parse(message.body);

        // Emitir la alerta a todos los suscriptores
        this.alertaSubject.next(alerta);

        console.log('✅ Alerta procesada:', alerta);
      } catch (error) {
        console.error('❌ Error al parsear alerta:', error);
      }
    });

    console.log('✅ Suscrito a /topic/alertas');
  }

  /**
   * Desconectar del WebSocket
   */
  public disconnect(): void {
    if (this.stompClient) {
      console.log('🔌 Desconectando WebSocket...');
      this.stompClient.deactivate();
      this.conexionSubject.next(false);
      console.log('✅ WebSocket desconectado');
    }
  }

  /**
   * Verificar si está conectado
   */
  public isConnected(): boolean {
    return this.stompClient?.connected || false;
  }

  /**
   * Enviar un mensaje (por si necesitas enviar algo al servidor)
   */
  public sendMessage(destination: string, body: any): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: destination,
        body: JSON.stringify(body)
      });
      console.log('📤 Mensaje enviado a:', destination);
    } else {
      console.error('❌ No se puede enviar: WebSocket no conectado');
    }
  }
}
