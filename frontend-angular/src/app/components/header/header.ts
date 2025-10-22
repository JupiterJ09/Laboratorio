import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-header',
  standalone: true, // 1. Hazlo standalone
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
// 2. ESTA LÍNEA ES LA SOLUCIÓN AL SEGUNDO ERROR
export class HeaderComponent {

}
