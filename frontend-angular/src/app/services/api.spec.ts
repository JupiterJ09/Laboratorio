/**
 *archivo api.spec.ts
 *es un servicio base para centralizar todas las peticiones
 *http a la API, con los metodos genericos (get,put, post, delete)
 *y el manejo de errores
 *hecho el 20/10/25, Alcazardavid, 5.7
 */

import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
  })
export class ApiService {
    //definimos la url
    private readonly baseURL = 'http://localhost:8080/api';
    //le inyectamos el httpclient
    private http = inject(HttpClient);

    constructor() { }
    //manejo de errores
    private handleError(error: HttpErrorResponse){
        console.error('ha ocurrido un error en la peticion', error.message);
        return throwError(() => new Error('Error en la peticion' + error.message));
      }

    //metodo get
    public get<T>(endpoint: string): Observable<T> {
      const url = `${this.baseURL}/${endpoint}`;
      return this.http.get<T>(url).pipe(
        catchError(this.handleError)
        );
      }
    //metodo post
    public post<T>(endpoint: string, data: any): Observable<T> {
        const url = `${this.baseURL}/${endpoint}`;
        return this.http.post<T>(url, data).pipe(
          catchError(this.handleError)
        );
      }
    //metodo put
    public put<T>(endpoint: string, data: any): Observable<T> {
        const url = `${this.baseURL}/${endpoint}`;
        return this.http.put<T>(url, data).pipe(
          catchError(this.handleError)
        );
      }
    //metodo delete
    public delete<T>(endpoint: string): Observable<T> {
        const url = `${this.baseURL}/${endpoint}`;
        return this.http.delete<T>(url).pipe(
          catchError(this.handleError)
        );
      }
  }
