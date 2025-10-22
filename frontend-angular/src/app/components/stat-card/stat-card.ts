/**
 *stat-card.ts
 *componente (hijo) reutilizable para mostrar una tarjeta de estadistica
 *recibe los datos (titulo, valor, etc) desde un componente padre a traves de Input
 *hecho 22/10/25 Alcazardavid, 6.2
 */

import { Component, Input } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';

@Component({
  selector: 'app-stat-card',
  standalone: true,
  imports: [
    CommonModule,
    NgClass
  ],
  templateUrl: './stat-card.html',
  styleUrl: './stat-card.css'
})
export class StatCardComponent {
  @Input() titulo!: string;
  @Input() valor!: string | number;
  @Input() subtexto!: string;
  @Input() color: string = 'blue';
  }
