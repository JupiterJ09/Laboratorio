/**
 * @file predicciones.ts
 * @description Página para visualizar y generar predicciones de demanda de insumos.
 * @author David Alcázar Gómez
 * @date 2025-10-23
 */
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // 1. Importa FormsModule para [(ngModel)]

// 2. Importa el servicio y la interfaz de Insumo
import { InsumoService } from '../../services/insumo';
import { Insumo } from '../../models/insumo.interface';

// 3. Importa el servicio y la interfaz de Prediccion (¡Recuerda verificarla!)
import { PrediccionService } from '../../services/prediccion';
import { Prediccion } from '../../models/prediccion.interface';

// 4. Importa el componente del gráfico que ya creaste
import { GraficoPrediccionComponent } from '../../components/grafico-prediccion/grafico-prediccion';

@Component({
  selector: 'app-predicciones',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule, // 5. Añade FormsModule
    GraficoPrediccionComponent // 6. Añade el componente del gráfico
  ],
  templateUrl: './predicciones.html',
  styleUrl: './predicciones.css'
})
export class PrediccionesComponent implements OnInit {

  // --- Inyección de Servicios ---
  private insumoService = inject(InsumoService);
  private prediccionService = inject(PrediccionService);

  // --- Estado del Componente (Signals) ---
  // [ ] Select para elegir insumo
  listaInsumos = signal<Insumo[]>([]);
  insumoSeleccionadoId = signal<number | null>(null);

  // [ ] Input para periodo (mes) - Asumimos un número de días por simplicidad
  periodoDias = signal<number>(30); // Por defecto, 30 días

  // Datos para el gráfico
  chartData = signal<number[]>([]);
  chartLabels = signal<string[]>([]);
  chartTitle = signal('Seleccione un insumo para ver la predicción');

  // [ ] Mostrar métricas: promedio, días restantes, precisión
  metricaPromedio = signal<number | string>('--');
  metricaDiasRestantes = signal<number | string>('--');
  metricaPrecision = signal<number | string>('--');

  isLoading = signal(false); // Para mostrar un indicador de carga

  ngOnInit(): void {
    // Carga la lista de insumos al iniciar
    this.cargarListaInsumos();
  }

  /**
   * Carga la lista de insumos activos para el select.
   */
  private cargarListaInsumos(): void {
    this.insumoService.getInsumosActivos().subscribe({
      next: (insumos) => {
        this.listaInsumos.set(insumos);
        // Opcional: Seleccionar el primer insumo por defecto
        if (insumos.length > 0) {
          // this.insumoSeleccionadoId.set(insumos[0].id);
          // this.generarProyeccion(); // Y cargar su gráfico
        }
      },
      error: (err) => console.error('Error al cargar insumos:', err)
    });
  }

  /**
   * [ ] Botón "Generar Proyección" - Lógica
   * Llama al servicio de predicción cuando se hace clic.
   */
  generarProyeccion(): void {
    const id = this.insumoSeleccionadoId();
    if (id === null) {
      this.chartTitle.set('Por favor, seleccione un insumo');
      return; // No hacer nada si no hay insumo seleccionado
    }

    this.isLoading.set(true);
    this.chartTitle.set(`Cargando predicción para Insumo #${id}...`);
    this.chartData.set([]); // Limpiar gráfico anterior
    this.chartLabels.set([]);
    this.metricaPromedio.set('--');
    this.metricaDiasRestantes.set('--');
    this.metricaPrecision.set('--');

    // Llama al servicio (usará datos simulados si el backend no responde)
    this.prediccionService.getPrediccion(id).subscribe({
      next: (prediccion: any) => { // Usa 'any' temporalmente
        console.log('Predicción recibida:', prediccion);

        // --- Simulación (Borrar/Adaptar con datos reales) ---
        const dias = this.periodoDias();
        const datosSimulados = Array.from({ length: dias }, (_, i) => 100 - (i * (100 / (dias -1 || 1) )) ); // Simula descenso lineal
        const etiquetasSimuladas = Array.from({ length: dias }, (_, i) => `Día ${i + 1}`);
        const insumoNombre = this.listaInsumos().find(ins => ins.id === id)?.nombre || `Insumo #${id}`;
        // --- Fin Simulación ---

        // Actualiza los signals para el gráfico y métricas
        // TODO: Usar datos reales de 'prediccion'
        this.chartData.set(datosSimulados);
        this.chartLabels.set(etiquetasSimuladas);
        this.chartTitle.set(`Proyección de ${dias} días para ${insumoNombre}`);

        // TODO: Calcular/Obtener métricas reales
        this.metricaPromedio.set( (datosSimulados.reduce((a, b) => a + b, 0) / dias).toFixed(1) );
        this.metricaDiasRestantes.set(dias); // Simulación
        this.metricaPrecision.set('92.5%'); // Simulación

        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al generar proyección:', err);
        this.chartTitle.set(`Error al cargar predicción para Insumo #${id}`);
        this.isLoading.set(false);
      }
    });
  }
}
