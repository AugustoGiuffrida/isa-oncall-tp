import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IExcepcionDeVentana } from '../excepcion-de-ventana.model';
import { ExcepcionDeVentanaService } from '../service/excepcion-de-ventana.service';

const excepcionDeVentanaResolve = (route: ActivatedRouteSnapshot): Observable<null | IExcepcionDeVentana> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ExcepcionDeVentanaService);
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

export default excepcionDeVentanaResolve;
