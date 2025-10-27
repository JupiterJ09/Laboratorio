import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoteService } from '../../services/lote';
import { LoteCaducidadDTO } from '../../models/lote.interface';
import { PrioridadAlerta } from '../../models/alerta.interface';

@Component({
  selector: 'app-caducidad',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule // Necesario para [(ngModel)]
  ],
  templateUrl: './caducidad.html',
  styleUrl: './caducidad.css'
})
export class Caducidad implements OnInit {

  private loteService = inject(LoteService);

  // --- Signals para el estado ---
  private listaMaestraLotes = signal<LoteCaducidadDTO[]>([]);
  public isLoading = signal<boolean>(true);

  // --- Signals para los controles de UI ---
  public filtroNivelAlerta = signal<string>('todos'); // 'todos', 'CRITICA', 'ALTA', 'MEDIA', 'BAJA'
  public ordenarPor = signal<string>('caducidadAsc'); // 'caducidadAsc', 'caducidadDesc'

  // --- Signals Computados (Reactivos) ---

  public lotesFiltradosYOrdenados = computed(() => {
    let lotes = this.listaMaestraLotes();

    // 1. Filtrar por nivel de alerta
    if (this.filtroNivelAlerta() !== 'todos') {
      lotes = lotes.filter(lote => lote.nivelAlerta === this.filtroNivelAlerta());
    }

    // 2. Ordenar
    const orden = this.ordenarPor();
    lotes = [...lotes].sort((a, b) => { // Crear una copia para no mutar el array original
      const fechaA = new Date(a.fechaCaducidad).getTime();
      const fechaB = new Date(b.fechaCaducidad).getTime();

      if (orden === 'caducidadAsc') {
        return fechaA - fechaB;
      } else { // caducidadDesc
        return fechaB - fechaA;
      }
    });

    return lotes;
  });

  ngOnInit(): void {
    this.cargarLotes();
  }

  private cargarLotes(): void {
    this.isLoading.set(true);
    // Asumo que quieres lotes próximos a caducar, por ejemplo, en los próximos 90 días
    this.loteService.getLotesProximosACaducar(90).subscribe({
      next: (lotes) => {
        this.listaMaestraLotes.set(lotes);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar lotes próximos a caducar:', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Métodos para UI ---

  public onFiltroChange(): void {
    // No necesitamos resetear paginación aquí ya que no hay paginación en esta tabla por ahora
    // Si se añade paginación, se resetearía la página actual a 1.
  }

  getBadgeClasses(nivelAlerta: PrioridadAlerta): string {
    const baseClasses = 'px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wider'; // Clases base para todos los badges
    switch (nivelAlerta) {
      case 'CRITICA':
        return `${baseClasses} bg-red-200 text-red-800`;
      case 'ALTA':
        return `${baseClasses} bg-orange-200 text-orange-800`;
      case 'MEDIA':
        return `${baseClasses} bg-yellow-200 text-yellow-800`;
      case 'BAJA':
        return `${baseClasses} bg-blue-200 text-blue-800`;
      default:
        return `${baseClasses} bg-gray-200 text-gray-800`;
    }
  }
}
