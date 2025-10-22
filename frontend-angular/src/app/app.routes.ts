import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard';


export const routes: Routes = [

    {
      path: '',
      component: DashboardComponent
    },
    {
      path: 'inventario',
      component: DashboardComponent // Placeholder
    },
    {
      path: 'predicciones',
      component: DashboardComponent // Placeholder
    },
    {
      path: 'caducidad',
      component: DashboardComponent // Placeholder
    },

    // (Buena práctica) Redirigir cualquier ruta desconocida al inicio
    {
      path: '**',
      redirectTo: ''
    }
  ];
