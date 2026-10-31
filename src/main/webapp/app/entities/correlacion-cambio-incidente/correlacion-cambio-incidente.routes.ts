import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import CorrelacionCambioIncidenteResolve from './route/correlacion-cambio-incidente-routing-resolve.service';

const correlacionCambioIncidenteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/correlacion-cambio-incidente').then(m => m.CorrelacionCambioIncidente),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/correlacion-cambio-incidente-detail').then(m => m.CorrelacionCambioIncidenteDetail),
    resolve: {
      correlacionCambioIncidente: CorrelacionCambioIncidenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/correlacion-cambio-incidente-update').then(m => m.CorrelacionCambioIncidenteUpdate),
    resolve: {
      correlacionCambioIncidente: CorrelacionCambioIncidenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/correlacion-cambio-incidente-update').then(m => m.CorrelacionCambioIncidenteUpdate),
    resolve: {
      correlacionCambioIncidente: CorrelacionCambioIncidenteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default correlacionCambioIncidenteRoute;
