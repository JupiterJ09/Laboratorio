/**
 *componente padre que carga datos simulados de prediccion y se los pasa al componente hijo (grafico-prediccion)
 */
import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GraficoPrediccionComponent } from '../grafico-prediccion/grafico-prediccion';
@Component({
  selector: 'app-dashboard-chart',
  standalone: true,
  imports: [
    CommonModule,
    GraficoPrediccionComponent,
    ],
  templateUrl: './dashboard-chart.html',
  styleUrl: './dashboard-chart.css'
})
export class DashboardChartComponent implements OnInit {
// 3. Crea Signals para guardar los datos que irán al gráfico
  public chartData = signal<number[]>([]);
  public chartLabels = signal<string[]>([]);
  public chartTitle = signal('Cargando predicción...');

  ngOnInit(): void {
    // 4. Llama al método para cargar los datos SIMULADOS
    this.cargarPrediccionSimulada();
  }

  /**
   * [ Tarea 6.8 ] Carga datos simulados para el gráfico.
   */
  private cargarPrediccionSimulada(): void {
    console.log('📊 Cargando datos simulados para el gráfico...');

    // [ ] Mostrar proyección 30 días (SIMULADO)
    const datosSimulados = [100, 98, 95, 90, 88, 82, 75, 70, 68, 60, 55, 50, 48, 42, 40, 35, 33, 30, 25, 22, 20, 18, 15, 12, 10, 8, 5, 3, 2, 0];
    const etiquetasSimuladas = Array.from({ length: 30 }, (_, i) => `Día ${i + 1}`);

    // Actualiza los signals
    this.chartData.set(datosSimulados);
    this.chartLabels.set(etiquetasSimuladas);
    this.chartTitle.set(`Predicción Próximos 30 Días (Simulado)`);
  }
}
