/**
 *componente padre que actua como la pagina principal del dashboard
 *llama a los servicios (alertaservice) para obtener datos y se los pasa a los componentes hijos (stat-card)
 *hecho 22/10/25 Alcazardavid, 6.2
 */

import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatCardComponent } from '../stat-card/stat-card'; //nuevo componente
import { DashboardTableComponent } from '../dashboard-table/dashboard-table';
import { DashboardChartComponent } from '../dashboard-chart/dashboard-chart';
import { AlertaService } from '../../services/alerta';
import { Alerta } from '../../models/alerta.interface';
@Component({
  selector: 'app-dashboard',
  standalone: true, // 'standalone: true'
  imports: [
    CommonModule,
    StatCardComponent,
    DashboardTableComponent, // [NUEVO]
    DashboardChartComponent  // [NUEVO]
    ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private alertaService = inject(AlertaService);

  //signals para guardar los datos
  numeroDeAlertas = signal(0);
  subtextoAlertas = signal('cargando...');

  ngOnInit(): void {
    this.alertaService.getAlertasActivas().subscribe((alertas: Alerta[]) => {
        // Actualiza los signals con los datos reales
        this.numeroDeAlertas.set(alertas.length);
        this.subtextoAlertas.set('Alertas activas sin leer');
      });
    }
}
