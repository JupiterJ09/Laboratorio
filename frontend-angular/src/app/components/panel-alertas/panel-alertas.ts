import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { map } from 'rxjs';

import { AlertaService } from '../../services/alerta';
import { Alerta, PrioridadAlerta, TipoAlerta } from '../../models/alerta.interface';

@Component({
  selector: 'app-panel-alertas',
  standalone: true, // Aseguramos que sea standalone
  imports: [CommonModule],
  templateUrl: './panel-alertas.html',
  styleUrl: './panel-alertas.css'
})
export class PanelAlertas implements OnInit { // Implementamos OnInit

  private alertaService = inject(AlertaService);

  // Signal para almacenar las últimas 5 alertas
  ultimasAlertas = signal<Alerta[]>([]);
  isLoading = signal<boolean>(true); // Signal para el estado de carga

  ngOnInit(): void {
    // Nos suscribimos al observable de alertas del servicio
    this.alertaService.alertas$.pipe(
      // Tomamos solo las 5 más recientes
      map(alertas => alertas.slice(0, 5))
    ).subscribe(alertas => {
      this.ultimasAlertas.set(alertas);
      this.isLoading.set(false); // Desactivamos el spinner cuando las alertas se cargan
    });
  }

  /**
   * Devuelve las clases de Tailwind CSS según la prioridad de la alerta.
   * @param prioridad La prioridad de la alerta.
   * @returns Un string con las clases CSS.
   */
  getClasesPorPrioridad(prioridad: PrioridadAlerta): string {
    const baseClasses = 'p-4 mb-4 rounded-lg shadow-md border-l-4 flex items-center gap-4 transition-transform duration-300 hover:scale-[1.02]';

    switch (prioridad) {
      case 'CRITICA':
        // Para CRITICA, añadimos la animación de pulso
        return `${baseClasses} bg-red-100 border-red-500 text-red-800 animate-pulse`;
      case 'ALTA':
        return `${baseClasses} bg-orange-100 border-orange-500 text-orange-800`;
      case 'MEDIA':
        return `${baseClasses} bg-yellow-100 border-yellow-500 text-yellow-800`;
      case 'BAJA':
        return `${baseClasses} bg-blue-100 border-blue-500 text-blue-800`;
      default:
        return `${baseClasses} bg-gray-100 border-gray-500 text-gray-800`;
    }
  }

  /**
   * Devuelve el nombre de un ícono de Heroicons según el tipo de alerta.
   * (Actualmente no se usa directamente en el HTML, pero es útil tenerlo)
   * @param tipo El tipo de la alerta.
   * @returns El nombre del ícono.
   */
  getIconoPorTipo(tipo: TipoAlerta): string {
    switch (tipo) {
      case 'STOCK_BAJO':
        return 'archive-box-x-mark';
      case 'CADUCIDAD':
        return 'calendar-days';
      case 'VENCIDO':
        return 'exclamation-triangle';
      case 'SISTEMA':
        return 'cog-6-tooth';
      default:
        return 'information-circle';
    }
  }
}
