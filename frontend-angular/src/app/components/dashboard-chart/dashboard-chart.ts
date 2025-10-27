/**
 * @file dashboard-chart.ts
 * @description Carga datos de predicción REALES (via proxy Java)
 * @description y los pasa al componente hijo (grafico-prediccion).
 */
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PrediccionService } from '../../services/prediccion';
// Importa la nueva estructura de respuesta
import { RespuestaPrediccion } from '../../models/prediccion.interface';

import { GraficoPrediccionComponent } from '../grafico-prediccion/grafico-prediccion';

@Component({
  selector: 'app-dashboard-chart',
  standalone: true,
  imports: [
    CommonModule,
    GraficoPrediccionComponent
  ],
  templateUrl: './dashboard-chart.html',
  styleUrl: './dashboard-chart.css'
})
export class DashboardChartComponent implements OnInit {

  private prediccionService = inject(PrediccionService);

  // Signals para el gráfico
  public chartData = signal<number[]>([]);
  public chartLabels = signal<string[]>([]);
  public chartTitle = signal('Cargando predicción...');

  ngOnInit(): void {
    this.cargarPrediccion();
  }

  /**
   * Carga la predicción real desde el servicio (via proxy Java).
   */
  private cargarPrediccion(): void {
    const insumoIdParaProbar = 1;
    this.chartTitle.set(`Cargando predicción para Insumo #${insumoIdParaProbar}...`);

    this.prediccionService.getPrediccion(insumoIdParaProbar).subscribe({
      next: (respuesta: RespuestaPrediccion) => {
        console.log('📈 Predicción recibida del backend Java (proxy):', respuesta);

        // Extrae los datos del array 'proyeccion_30_dias'
        const proyeccion = respuesta.proyeccion_30_dias || [];
        const datosReales = proyeccion.map(dia => dia.stock_estimado);
        const etiquetasReales = proyeccion.map(dia => dia.fecha); // O usar 'dia.dia' si prefieres números

        if (datosReales.length > 0) {
          this.chartData.set(datosReales);
          this.chartLabels.set(etiquetasReales);
          this.chartTitle.set(`Predicción para ${respuesta.nombre_insumo || `Insumo #${insumoIdParaProbar}`}`);
        } else {
          console.warn('La predicción recibida no contiene datos en proyeccion_30_dias');
          this.chartTitle.set(`No hay datos de predicción para Insumo #${insumoIdParaProbar}`);
          this.chartData.set([]);
          this.chartLabels.set([]);
        }
      },
      error: (err) => {
        console.error('❌ Error al cargar la predicción:', err);
        this.chartTitle.set('Error al cargar predicción');
        this.chartData.set([]);
        this.chartLabels.set([]);
      }
    });
  }
}
