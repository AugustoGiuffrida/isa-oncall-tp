import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { VentanaDeMantenimientoService } from '../service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

const ventanaDeMantenimientoResolve = (route: ActivatedRouteSnapshot): Observable<null | IVentanaDeMantenimiento> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(VentanaDeMantenimientoService);
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

export default ventanaDeMantenimientoResolve;
