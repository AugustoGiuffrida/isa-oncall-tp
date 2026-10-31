import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';
import { CorrelacionCambioIncidenteService } from '../service/correlacion-cambio-incidente.service';

const correlacionCambioIncidenteResolve = (route: ActivatedRouteSnapshot): Observable<null | ICorrelacionCambioIncidente> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CorrelacionCambioIncidenteService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default correlacionCambioIncidenteResolve;
