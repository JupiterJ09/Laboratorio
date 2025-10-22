/**
 *archivo websocket.ts
 *servicio para gestionar la conexion websocket con el backend
 *usando stompjs sobre sockJs
 *hecho 20/10/25 Alcazardavid, 5.10
*/


import { Injectable } from '@angular/core';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class WebSocketService {
  private stompClient!: Client;
  private readonly webSocketUrl = 'http://localhost:8080/ws';
  private alertaSubject = new Subject<any>();

  public alertas$: Observable<any> = this.alertaSubject.asObservable();

  constructor(){ }

  public connect(): void {
    console.log('iniciando conexion websocket-----');

    //Creamos al cliente Stomp
    this.stompClient = Stomp.over(() => new SockJS(this.webSocketUrl));
    this.stompClient.debug = (str) => {
      console.log(str);
      }
    //conectamos
    this.stompClient.activate();

    //manejamos la conexion exitosa
    this.stompClient.onConnect = (frame) => {
      console.log('conectando al websocket_', frame);
      this.subscribeToAlerts();
      };
    }
  private subscribeToAlerts(): void {
    this.stompClient.subscribe('/topic/alertas:', (message: IMessage) => {
      console.log('mensaje recibido de /topic/alertas:', message.body);
      const alerta = JSON.parse(message.body);
      this.alertaSubject.next(alerta);
      });
    }
  public disconnect(): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.deactivate();
      console.log('WebSocket desconectado.');
    }
  }
}
