/**
 * @file predicciones.ts
 * @description Página para visualizar y generar predicciones de demanda de insumos.
 * @author David Alcázar Gómez
 * @date 2025-10-27
 */
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importa FormsModule para [(ngModel)]

// Importa servicios e interfaces
import { InsumoService } from '../../services/insumo';
import { Insumo } from '../../models/insumo.interface';
import { PrediccionService } from '../../services/prediccion';
import { RespuestaPrediccion } from '../../models/prediccion.interface'; // Correcta importación

// Importa el componente del gráfico
import { GraficoPrediccionComponent } from '../../components/grafico-prediccion/grafico-prediccion';

@Component({
  selector: 'app-predicciones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    GraficoPrediccionComponent
  ],
  templateUrl: './predicciones.html',
  styleUrl: './predicciones.css'
})
export class PrediccionesComponent implements OnInit {

  // --- Inyección de Servicios ---
  private insumoService = inject(InsumoService);
  private prediccionService = inject(PrediccionService);

  // --- Estado del Componente (Signals) ---
  listaInsumos = signal<Insumo[]>([]);
  insumoSeleccionadoId = signal<number | null>(null);
  periodoDias = signal<number>(30);

  // Datos para el gráfico
  chartData = signal<number[]>([]);
  chartLabels = signal<string[]>([]);
  chartTitle = signal('Seleccione un insumo para ver la predicción');

  // Métricas
  metricaPromedio = signal<number | string>('--');
  metricaDiasRestantes = signal<number | string>('--');
  metricaPrecision = signal<number | string>('--');

  isLoading = signal(false);

  ngOnInit(): void {
    this.cargarListaInsumos();
  }

  /**
   * Carga la lista de insumos activos para el select.
   */
  private cargarListaInsumos(): void {
    this.insumoService.getInsumosActivos().subscribe({
      next: (insumos: Insumo[]) => { // Añadido tipo explícito
        this.listaInsumos.set(insumos);
      },
      error: (err: any) => console.error('Error al cargar insumos:', err) // Añadido tipo explícito
    });
  }

  /**
   * Llama al servicio de predicción cuando se hace clic en "Generar Proyección".
   */
  generarProyeccion(): void {
    const id = this.insumoSeleccionadoId();
    if (id === null) {
      this.chartTitle.set('Por favor, seleccione un insumo');
      return;
    }

    this.isLoading.set(true);
    this.chartTitle.set(`Cargando predicción para Insumo #${id}...`);
    this.chartData.set([]);
    this.chartLabels.set([]);
    this.metricaPromedio.set('--');
    this.metricaDiasRestantes.set('--');
    this.metricaPrecision.set('--');

    this.prediccionService.getPrediccion(id).subscribe({
      next: (respuesta: RespuestaPrediccion) => { // Usa la interfaz correcta
        console.log('📈 Predicción REAL recibida:', respuesta);

        // Extrae los datos REALES del array 'proyeccion_30_dias'
        const proyeccion = respuesta.proyeccion_30_dias || [];
        const datosReales = proyeccion.map(dia => dia.stock_estimado);
        const etiquetasReales = proyeccion.map(dia => dia.fecha);

        const insumoNombre = respuesta.nombre_insumo || `Insumo #${id}`;
        const diasProyectados = proyeccion.length;

        // Actualiza los signals para el gráfico con datos REALES
        if (datosReales.length > 0) {
          this.chartData.set(datosReales);
          this.chartLabels.set(etiquetasReales);
          this.chartTitle.set(`Proyección de ${diasProyectados} días para ${insumoNombre}`);
        } else {
            console.warn('La predicción recibida no contiene datos en proyeccion_30_dias');
            this.chartTitle.set(`No hay datos de predicción para ${insumoNombre}`);
            this.chartData.set([]);
            this.chartLabels.set([]);
        }

        // Calcula/Obtiene métricas REALES (si vienen en la respuesta)
        const respuestaAny = respuesta as any; // Usamos 'as any' para acceder a props no definidas en la interfaz base

        // --- ¡CORRECCIÓN AQUÍ! ---
        const promedioStr = respuestaAny.promedio_diario; // Obtiene el string "1.90"
        const promedioNum = parseFloat(promedioStr); // Convierte a número 1.9
        this.metricaPromedio.set(!isNaN(promedioNum) ? promedioNum.toFixed(1) : '--'); // Formatea si es válido
        // --- Fin Corrección ---

        // Asumiendo que dias_restantes también puede ser string o null
        const diasRestantesStr = respuestaAny.dias_restantes;
        const diasRestantesNum = parseInt(diasRestantesStr, 10); // Convierte a entero
        this.metricaDiasRestantes.set(!isNaN(diasRestantesNum) ? diasRestantesNum.toFixed(0) : 'N/A');

        // La precisión sigue simulada por ahora
        this.prediccionService.getPrecisionIA().subscribe(data => {
            const precisionValue = typeof data.precision === 'number' ? `${data.precision.toFixed(1)}%` : data.precision;
            this.metricaPrecision.set(precisionValue || '--');
        });

        this.isLoading.set(false);
      },
      error: (err: any) => { // Añadido tipo explícito
        console.error('Error al generar proyección:', err);
        this.chartTitle.set(`Error al cargar predicción para Insumo #${id}`);
        this.isLoading.set(false);
      }
    });
  }
}
