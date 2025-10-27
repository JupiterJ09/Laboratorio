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
import { LoteService } from '../../services/lote';
import { PanelAlertas } from '../panel-alertas/panel-alertas'; // Importación corregida para usar el archivo panel-alertas.ts
import { LoteCaducidadDTO } from '../../models/lote.interface';
@Component({
  selector: 'app-dashboard',
  standalone: true, // 'standalone: true'
  imports: [
    CommonModule,
    StatCardComponent, // [NUEVO]
    DashboardTableComponent, // [NUEVO]
    DashboardChartComponent,// [NUEVO]
    PanelAlertas // Usamos la clase PanelAlertas del archivo panel-alertas.ts
    ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  private alertaService = inject(AlertaService);
  private insumoService = inject(InsumoService);
  private prediccionService = inject(PrediccionService);
  private loteService = inject(LoteService);

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

  // Signal para almacenar los insumos para la tabla
  insumosParaTabla = signal<Insumo[]>([]);
  isLoadingInsumos = signal<boolean>(false); // Signal para el estado de carga de la tabla

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
      this.isLoadingInsumos.set(true); // Inicia la carga
      this.insumoService.getInsumosStockBajo().subscribe({
        next: (insumos: Insumo[]) => {
          this.cardInsumosValor.set(insumos.length);
          this.insumosParaTabla.set(insumos); // Guardamos los insumos para la tabla
          this.cardInsumosSubtexto.set('Insumos con stock bajo');
          this.isLoadingInsumos.set(false); // Finaliza la carga
        },
        error: (err) => {
          console.error('Error cargando insumos críticos:', err);
          this.cardInsumosValor.set('Error');
          this.isLoadingInsumos.set(false); // Finaliza la carga en caso de error
        }
      });

      // 3. Cargar Lotes Próximos a Caducar
      this.loteService.getLotesProximosACaducar(7).subscribe({
        next: (lotes: LoteCaducidadDTO[]) => {
          console.log('📦 Respuesta completa del backend:', lotes); 
          this.cardCaducidadValor.set(lotes.length);
          this.cardCaducidadSubtexto.set('Lotes en los próximos 7 días');
          console.log(`⏳ ${lotes.length} lotes próximos a caducar cargados.`);
        },
        error: (err: any) => {
          console.error('❌ Error completo:', err); 
          this.cardCaducidadValor.set('Error');
          this.cardCaducidadSubtexto.set('No se pudo cargar');
        }
      });

      // 4. Cargar Precisión IA desde Flask vía Spring Boot
      this.prediccionService.getPrecisionIA().subscribe({
        next: (data) => {
          const precision = data?.precision ?? null;
          if (precision !== null) {
            this.cardPrediccionValor.set(`${precision}%`);
            this.cardPrediccionSubtexto.set('Precisión del modelo IA');
          } else {
            this.cardPrediccionValor.set('N/A');
            this.cardPrediccionSubtexto.set('No disponible');
          }
        },
        error: (err) => {
          console.error('❌ Error al obtener precisión de la IA:', err);
          this.cardPrediccionValor.set('Error');
          this.cardPrediccionSubtexto.set('No se pudo conectar al servidor');
        }
      });

    }
}
