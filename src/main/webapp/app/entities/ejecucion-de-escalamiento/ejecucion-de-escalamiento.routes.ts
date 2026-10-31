import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import EjecucionDeEscalamientoResolve from './route/ejecucion-de-escalamiento-routing-resolve.service';

const ejecucionDeEscalamientoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/ejecucion-de-escalamiento').then(m => m.EjecucionDeEscalamiento),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/ejecucion-de-escalamiento-detail').then(m => m.EjecucionDeEscalamientoDetail),
    resolve: {
      ejecucionDeEscalamiento: EjecucionDeEscalamientoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ejecucionDeEscalamientoRoute;
