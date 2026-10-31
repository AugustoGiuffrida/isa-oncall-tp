import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import DespliegueDeServicioResolve from './route/despliegue-de-servicio-routing-resolve.service';

const despliegueDeServicioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/despliegue-de-servicio').then(m => m.DespliegueDeServicio),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/despliegue-de-servicio-detail').then(m => m.DespliegueDeServicioDetail),
    resolve: {
      despliegueDeServicio: DespliegueDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/despliegue-de-servicio-update').then(m => m.DespliegueDeServicioUpdate),
    resolve: {
      despliegueDeServicio: DespliegueDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/despliegue-de-servicio-update').then(m => m.DespliegueDeServicioUpdate),
    resolve: {
      despliegueDeServicio: DespliegueDeServicioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default despliegueDeServicioRoute;
