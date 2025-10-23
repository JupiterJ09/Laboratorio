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
// [ ] En dashboard.component.ts inyectar servicios
import { InsumoService } from '../../services/insumo';
import { PrediccionService } from '../../services/prediccion';

// Importamos las interfaces para que 'alertas' no sea 'any'
import { Insumo } from '../../models/insumo.interface';
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
  private insumoService = inject(InsumoService);
  private prediccionService = inject(PrediccionService);
  // --- 2. Crear Signals (variables) para las 4 tarjetas ---

  // [ ] Mostrar: alertas
  cardAlertasValor = signal<number | string>('...');
  cardAlertasSubtexto = signal('Cargando...');

  // [ ] Mostrar: insumos críticos
  cardInsumosValor = signal<number | string>('...');
  cardInsumosSubtexto = signal('Cargando...');

  // [ ] Mostrar: próximos caducar
  cardCaducidadValor = signal<number | string>('...');
  cardCaducidadSubtexto = signal('Cargando...');

  // [ ] Mostrar: precisión IA
  cardPrediccionValor = signal<number | string>('...');
  cardPrediccionSubtexto = signal('Cargando...');

  ngOnInit(): void {
      this.cargarEstadisticas();
    }
  private cargarEstadisticas(): void {

      // 1. Cargar Alertas (la que ya tenías)
      this.alertaService.getAlertasActivas().subscribe((alertas: Alerta[]) => {
        this.cardAlertasValor.set(alertas.length);
        this.cardAlertasSubtexto.set('Alertas activas sin leer');
      });

      // 2. Cargar Insumos Críticos
      // (Usa el método que creaste en la Tarea 5.8)
      this.insumoService.getInsumosStockBajo().subscribe((insumos: Insumo[]) => {
        this.cardInsumosValor.set(insumos.length);
        this.cardInsumosSubtexto.set('Insumos con stock bajo');
      });

      // 3. Cargar Próximos a Caducar (Placeholder)
      // (Aún no tienes un servicio para esto, así que ponemos datos fijos)
      // TODO: Crear 'InsumoService.getProximosACaducar()'
      this.cardCaducidadValor.set(23);
      this.cardCaducidadSubtexto.set('En los próximos 7 días');


      // 4. Cargar Precisión IA (Placeholder)
      // (Tu servicio 'PrediccionService' aún no tiene un método para esto)
      // TODO: Crear 'PrediccionService.getPrecisionIA()'
      this.cardPrediccionValor.set('92.5%');
      this.cardPrediccionSubtexto.set('Precisión del modelo');
    }
}
