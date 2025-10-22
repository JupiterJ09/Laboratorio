import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; //importamos el routerlink
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink
    ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class SidebarComponent {
// Ponemos los links y los íconos SVG aquí para mantener el HTML limpio
  links = [
    { path: '/', label: 'Dashboard', icon: 'M2.25 12l8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25' },
    { path: '/inventario', label: 'Inventario', icon: 'M20.25 7.5l-.625 10.632a2.25 2.25 0 01-2.247 2.118H6.622a2.25 2.25 0 01-2.247-2.118L3.75 7.5M10 11.25h4M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125z' },
    { path: '/predicciones', label: 'Predicciones', icon: 'M10.5 6a7.5 7.5 0 107.5 7.5h-7.5V6zM21 13.5A9 9 0 1112 4.5v9h9z' },
    { path: '/caducidad', label: 'Caducidad', icon: 'M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5' },
    { path: '/trazabilidad', label: 'Trazabilidad', icon: 'M15.042 21.672L13.684 16.6m0 0l-2.5 2.5m2.5-2.5l-2.5-2.5M15.042 21.672L13.684 16.6m0 0l-2.5 2.5m2.5-2.5l-2.5-2.5M15.042 21.672L13.684 16.6m0 0l-2.5 2.5m2.5-2.5l-2.5-2.5M15.042 21.672L13.684 16.6M13.684 16.6l-2.5 2.5M13.684 16.6l-2.5-2.5M6 12.75l5.25 5.25M6 12.75l-5.25-5.25M6 12.75l-5.25 5.25M12 8.25l-5.25 5.25M12 8.25l5.25 5.25M12 8.25l5.25-5.25M12 8.25l-5.25-5.25' },
    { path: '/reportes', label: 'Reportes', icon: 'M3.75 12h16.5m-16.5 3.75h16.5M3.75 19.5h16.5M5.625 4.5h12.75a1.875 1.875 0 010 3.75H5.625a1.875 1.875 0 010-3.75z' }
  ];
}
