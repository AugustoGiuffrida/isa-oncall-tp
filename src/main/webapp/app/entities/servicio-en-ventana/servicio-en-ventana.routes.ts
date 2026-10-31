import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ServicioEnVentanaResolve from './route/servicio-en-ventana-routing-resolve.service';

const servicioEnVentanaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/servicio-en-ventana').then(m => m.ServicioEnVentana),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/servicio-en-ventana-detail').then(m => m.ServicioEnVentanaDetail),
    resolve: {
      servicioEnVentana: ServicioEnVentanaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/servicio-en-ventana-update').then(m => m.ServicioEnVentanaUpdate),
    resolve: {
      servicioEnVentana: ServicioEnVentanaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/servicio-en-ventana-update').then(m => m.ServicioEnVentanaUpdate),
    resolve: {
      servicioEnVentana: ServicioEnVentanaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default servicioEnVentanaRoute;
