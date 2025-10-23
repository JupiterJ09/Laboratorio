/**
 *componente hijo que renderiza un grafico de linea con la prediccion de demanda de un insumo
 *hecho 23/10/25 Alcazardavid, 5.1
 */
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartConfiguration } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-grafico-prediccion',
  standalone: true,
  imports: [
    CommonModule,
    BaseChartDirective
  ],
  templateUrl: './grafico-prediccion.html',
  styleUrl: './grafico-prediccion.css'
})
export class GraficoPrediccionComponent implements OnChanges {
  @Input() datos: number[] = []; //valores en y
  @Input() etiquetas: string[] = []; //valores en x
  @Input() titulo: string = 'Prediccion';
  public lineChartType: ChartConfiguration['type'] = 'line';
  public lineChartData: ChartConfiguration['data'] = {
    labels: this.etiquetas,
    datasets: [{
      data: this.datos,
      label: this.titulo,
      fill: true,
      tension: 0.3,
      borderColor: 'rgb(75, 192, 192)',
      backgroundColor: 'rgba(75, 192, 192, 0.2)'
    }]
  };

  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        title: {
          display: true,
          text: 'Dias'
        }
      },
      y: {
        title: {
          display: true,
          text: 'Stock Proyectado'
        }
      }
    }
  };
  constructor() { }
  /**
  * Este "hook" (ngOnChanges) se dispara CADA VEZ que
  * los @Input (datos, etiquetas) cambian.
  * Esto es lo que hace que el gráfico se actualice
  * cuando los datos reales lleguen.
  */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['datos'] || changes['etiquetas']) {
      this.actualizarGrafico();
    }
  }

  private actualizarGrafico(): void {
    // Actualiza los datos del gráfico
    this.lineChartData = {
      labels: this.etiquetas,
      datasets: [
        {
          ...this.lineChartData.datasets[0], // Mantiene la configuración de color
          data: this.datos,
          label: this.titulo
        }
      ]
    };
  }
}
