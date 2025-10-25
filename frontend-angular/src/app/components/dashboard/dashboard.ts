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
import { LoteCaducidadDTO } from '../../models/lote.interface';
@Component({
  selector: 'app-dashboard',
  standalone: true, // 'standalone: true'
  imports: [
    CommonModule,
    StatCardComponent, // [NUEVO]
    DashboardTableComponent, // [NUEVO]
    DashboardChartComponent,// [NUEVO]
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
        this.insumosParaTabla.set(insumos); // Guardamos los insumos para la tabla
        this.cardInsumosSubtexto.set('Insumos con stock bajo');
      });

      // 3. Cargar Lotes Próximos a Caducar
      this.loteService.getLotesProximosACaducar(7).subscribe({
        next: (lotes: LoteCaducidadDTO[]) => {
          console.log('📦 Respuesta completa del backend:', lotes); // ✅ AGREGA ESTO
          this.cardCaducidadValor.set(lotes.length);
          this.cardCaducidadSubtexto.set('Lotes en los próximos 7 días');
          console.log(`⏳ ${lotes.length} lotes próximos a caducar cargados.`);
        },
        error: (err: any) => {
          console.error('❌ Error completo:', err); // ✅ AGREGA ESTO
          this.cardCaducidadValor.set('Error');
          this.cardCaducidadSubtexto.set('No se pudo cargar');
        }
      });

      // 4. Cargar Precisión IA (Placeholder)
      // (Tu servicio 'PrediccionService' aún no tiene un método para esto)
      // TODO: Crear 'PrediccionService.getPrecisionIA()'
      this.cardPrediccionValor.set('92.5%');
      this.cardPrediccionSubtexto.set('Precisión del modelo');
    }
}
