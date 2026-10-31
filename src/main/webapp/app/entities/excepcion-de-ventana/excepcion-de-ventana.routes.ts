import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ExcepcionDeVentanaResolve from './route/excepcion-de-ventana-routing-resolve.service';

const excepcionDeVentanaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/excepcion-de-ventana').then(m => m.ExcepcionDeVentana),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/excepcion-de-ventana-detail').then(m => m.ExcepcionDeVentanaDetail),
    resolve: {
      excepcionDeVentana: ExcepcionDeVentanaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/excepcion-de-ventana-update').then(m => m.ExcepcionDeVentanaUpdate),
    resolve: {
      excepcionDeVentana: ExcepcionDeVentanaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/excepcion-de-ventana-update').then(m => m.ExcepcionDeVentanaUpdate),
    resolve: {
      excepcionDeVentana: ExcepcionDeVentanaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default excepcionDeVentanaRoute;
