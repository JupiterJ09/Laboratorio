import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Importamos el servicio y la interfaz de SolicitudPrueba
import { SolicitudPruebaService } from '../../services/solicitud-prueba';
import { SolicitudPrueba } from '../../models/solicitud-prueba.interface';

@Component({
  selector: 'app-trazabilidad',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './trazabilidad.html',
  styleUrl: './trazabilidad.css'
})
export class Trazabilidad implements OnInit {

  private solicitudPruebaService = inject(SolicitudPruebaService);

  // --- Signals para el estado ---
  private listaMaestraSolicitudes = signal<SolicitudPrueba[]>([]);
  public isLoading = signal<boolean>(true);

  // --- Signals para los controles de UI ---
  public terminoBusqueda = signal<string>('');
  public filtroFechaDesde = signal<string>('');
  public filtroFechaHasta = signal<string>('');
  public filtroTipoPrueba = signal<string>('todos');

  // --- Signals Computados (Reactivos) ---

  // 1. Filtra la lista maestra según los controles
  public solicitudesFiltradas = computed(() => {
    let solicitudes = this.listaMaestraSolicitudes();

    // Filtro por término de búsqueda (folio o expediente)
    const busqueda = this.terminoBusqueda().toLowerCase();
    if (busqueda) {
      solicitudes = solicitudes.filter(s =>
        s.folio.toLowerCase().includes(busqueda) ||
        s.expediente.toLowerCase().includes(busqueda)
      );
    }

    // Filtro por tipo de prueba
    if (this.filtroTipoPrueba() !== 'todos') {
      solicitudes = solicitudes.filter(s => s.tipoPrueba === this.filtroTipoPrueba());
    }

    // Filtro por rango de fechas
    const fechaDesde = this.filtroFechaDesde();
    const fechaHasta = this.filtroFechaHasta();
    if (fechaDesde) {
      solicitudes = solicitudes.filter(s => new Date(s.fechaSolicitud) >= new Date(fechaDesde));
    }
    if (fechaHasta) {
      solicitudes = solicitudes.filter(s => new Date(s.fechaSolicitud) <= new Date(fechaHasta));
    }

    return solicitudes;
  });

  // 2. Extrae los tipos de prueba únicos para el <select>
  public tiposDePrueba = computed(() => {
    const tiposUnicos = new Set(this.listaMaestraSolicitudes().map(s => s.tipoPrueba));
    return ['todos', ...Array.from(tiposUnicos)];
  });

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  private cargarSolicitudes(): void {
    this.isLoading.set(true);

    // Ahora obtenemos los datos del servicio real
    this.solicitudPruebaService.getSolicitudes().subscribe({
      next: (solicitudes) => {
        this.listaMaestraSolicitudes.set(solicitudes);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error al cargar solicitudes de prueba:', err);
        this.isLoading.set(false);
      }
    });
  }

  // --- Métodos para UI ---

  public onFiltroChange(): void {
    // Este método se puede usar si se necesita lógica adicional al cambiar un filtro.
  }

  // Devuelve clases de Tailwind para el estado de la solicitud
  getEstadoClasses(estado: SolicitudPrueba['estado']): string {
    const base = 'px-2 inline-flex text-xs leading-5 font-semibold rounded-full';
    switch (estado) {
      case 'COMPLETADA': return `${base} bg-green-100 text-green-800`;
      case 'EN_PROCESO': return `${base} bg-blue-100 text-blue-800`;
      case 'PENDIENTE': return `${base} bg-yellow-100 text-yellow-800`;
      case 'CANCELADA': return `${base} bg-red-100 text-red-800`;
      default: return `${base} bg-gray-100 text-gray-800`;
    }
  }
}
