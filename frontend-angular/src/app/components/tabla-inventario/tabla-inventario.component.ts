import { Component, Input } from '@angular/core';
import { Insumo } from '../../models/insumo.interface';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-tabla-inventario',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tabla-inventario.component.html',
  styleUrl: './tabla-inventario.component.css'
})
export class TablaInventarioComponent {
  @Input() insumos: Insumo[] = [];

  constructor() {}
  getNivelRiesgo(dias: number): string {
    if (dias <= 0) return 'DESPERDICIO';
    if (dias <= 30) return 'PRIORIDAD'; // 30 días o menos es prioridad
    return 'SEGURO'; // Más de 30 días
  }

  getBadgeClasses(dias: number): string {
    // Clases base que todos los badges tendrán
    const baseClasses = 'px-3 py-1 rounded-full text-xs font-semibold uppercase tracking-wider'; // Añadí uppercase

    if (dias <= 0) {
      // Gris oscuro para 'DESPERDICIO'
      return `${baseClasses} bg-gray-700 text-white`;
    }
    if (dias <= 30) {
      // Rojo/Rosa para 'PRIORIDAD'
      return `${baseClasses} bg-red-200 text-red-800`;
    }
    // Verde para 'SEGURO'
    return `${baseClasses} bg-green-200 text-green-800`;
  }

}
