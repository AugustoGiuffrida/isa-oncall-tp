import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ServicioEnVentanaService } from '../service/servicio-en-ventana.service';
import { IServicioEnVentana } from '../servicio-en-ventana.model';

const servicioEnVentanaResolve = (route: ActivatedRouteSnapshot): Observable<null | IServicioEnVentana> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ServicioEnVentanaService);
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

export default servicioEnVentanaResolve;
