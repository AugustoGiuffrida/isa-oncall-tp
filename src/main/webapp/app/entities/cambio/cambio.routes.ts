import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CambioResolve from './route/cambio-routing-resolve.service';

const cambioRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cambio').then(m => m.Cambio),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cambio-detail').then(m => m.CambioDetail),
    resolve: {
      cambio: CambioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cambio-update').then(m => m.CambioUpdate),
    resolve: {
      cambio: CambioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cambio-update').then(m => m.CambioUpdate),
    resolve: {
      cambio: CambioResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cambioRoute;
