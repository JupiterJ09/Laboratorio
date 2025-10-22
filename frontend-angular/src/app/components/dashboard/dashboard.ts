/**
 *componente padre que actua como la pagina principal del dashboard
 *llama a los servicios (alertaservice) para obtener datos y se los pasa a los componentes hijos (stat-card)
 *hecho 22/10/25 Alcazardavid, 6.2
 */

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatCardComponent } from '../stat-card/stat-card'; //nuevo componente
@Component({
  selector: 'app-dashboard',
  standalone: true, // 'standalone: true'
  imports: [
    CommonModule,
    StatCardComponent
    ], // CommonModule a los imports
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent {

}
