import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import VentanaDeMantenimientoResolve from './route/ventana-de-mantenimiento-routing-resolve.service';

const ventanaDeMantenimientoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ventana-de-mantenimiento').then(m => m.VentanaDeMantenimiento),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ventana-de-mantenimiento-detail').then(m => m.VentanaDeMantenimientoDetail),
    resolve: {
      ventanaDeMantenimiento: VentanaDeMantenimientoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/ventana-de-mantenimiento-update').then(m => m.VentanaDeMantenimientoUpdate),
    resolve: {
      ventanaDeMantenimiento: VentanaDeMantenimientoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/ventana-de-mantenimiento-update').then(m => m.VentanaDeMantenimientoUpdate),
    resolve: {
      ventanaDeMantenimiento: VentanaDeMantenimientoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ventanaDeMantenimientoRoute;
