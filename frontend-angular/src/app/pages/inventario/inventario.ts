import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { InsumoService } from '../../services/insumo';
import { Insumo } from '../../models/insumo.interface';
import { DashboardTableComponent } from '../../components/dashboard-table/dashboard-table';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule, // Necesario para [(ngModel)]
    DashboardTableComponent
  ],
  templateUrl: './inventario.html',
  styleUrl: './inventario.css'
})
export class Inventario implements OnInit {

  private insumoService = inject(InsumoService);

  // --- Signals para el estado ---
  // Lista maestra de insumos, nunca se modifica directamente
  private listaMaestraInsumos = signal<Insumo[]>([]);
  public isLoading = signal<boolean>(true);

  // --- Signals para los controles de UI ---
  public terminoBusqueda = signal<string>('');
  public categoriaSeleccionada = signal<string>('todas');
  public mostrarSoloStockBajo = signal<boolean>(false);
  public paginaActual = signal<number>(1);
  public itemsPorPagina = signal<number>(10);

  // --- Signals Computados (Reactivos) ---

  // 1. Filtra la lista maestra según los controles
  public insumosFiltrados = computed(() => {
    let insumos = this.listaMaestraInsumos();

    // Filtro por término de búsqueda
    if (this.terminoBusqueda()) {
      insumos = insumos.filter(insumo =>
        insumo.nombre.toLowerCase().includes(this.terminoBusqueda().toLowerCase())
      );
    }

    // Filtro por categoría
    if (this.categoriaSeleccionada() !== 'todas') {
      insumos = insumos.filter(insumo => insumo.categoria === this.categoriaSeleccionada());
    }

    // Filtro por stock bajo
    if (this.mostrarSoloStockBajo()) {
      insumos = insumos.filter(insumo => (insumo.cantidadActual ?? 0) <= (insumo.cantidadMinima ?? 0));
    }

    return insumos;
  });

  // 2. Extrae las categorías únicas para el <select>
  public categorias = computed(() => {
    const categoriasUnicas = new Set(this.listaMaestraInsumos().map(i => i.categoria).filter(Boolean) as string[]);
    return ['todas', ...Array.from(categoriasUnicas)];
  });

  // 3. Calcula el total de páginas
  public totalPaginas = computed(() => {
    return Math.ceil(this.insumosFiltrados().length / this.itemsPorPagina());
  });

  // 4. Aplica la paginación a la lista ya filtrada
  public insumosPaginados = computed(() => {
    const inicio = (this.paginaActual() - 1) * this.itemsPorPagina();
    const fin = inicio + this.itemsPorPagina();
    return this.insumosFiltrados().slice(inicio, fin);
  });

  ngOnInit(): void {
    this.cargarInsumos();
  }

  private cargarInsumos(): void {
    this.isLoading.set(true);
    this.insumoService.getInsumosActivos().subscribe({
      next: (insumos) => {
        this.listaMaestraInsumos.set(insumos);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar insumos:', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Métodos para la Paginación ---

  public irAPagina(pagina: number): void {
    if (pagina >= 1 && pagina <= this.totalPaginas()) {
      this.paginaActual.set(pagina);
    }
  }

  public cambiarItemsPorPagina(event: Event): void {
    const valor = (event.target as HTMLSelectElement).value;
    this.itemsPorPagina.set(Number(valor));
    this.irAPagina(1); // Resetear a la primera página
  }

  // Resetea la página a 1 cada vez que un filtro cambia
  public onFiltroChange(): void {
    this.irAPagina(1);
  }
}
