import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IVentanaDeMantenimiento, NewVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

export type PartialUpdateVentanaDeMantenimiento = Partial<IVentanaDeMantenimiento> & Pick<IVentanaDeMantenimiento, 'id'>;

type RestOf<T extends IVentanaDeMantenimiento | NewVentanaDeMantenimiento> = Omit<T, 'desde' | 'hasta' | 'repetirHasta'> & {
  desde?: string | null;
  hasta?: string | null;
  repetirHasta?: string | null;
};

export type RestVentanaDeMantenimiento = RestOf<IVentanaDeMantenimiento>;

export type NewRestVentanaDeMantenimiento = RestOf<NewVentanaDeMantenimiento>;

export type PartialUpdateRestVentanaDeMantenimiento = RestOf<PartialUpdateVentanaDeMantenimiento>;

@Injectable()
export class VentanaDeMantenimientosService {
  readonly ventanaDeMantenimientosParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly ventanaDeMantenimientosResource = httpResource<RestVentanaDeMantenimiento[]>(() => {
    const params = this.ventanaDeMantenimientosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of ventanaDeMantenimiento that have been fetched. It is updated when the ventanaDeMantenimientosResource emits a new value.
   * In case of error while fetching the ventanaDeMantenimientos, the signal is set to an empty array.
   */
  readonly ventanaDeMantenimientos = computed(() =>
    (this.ventanaDeMantenimientosResource.hasValue() ? this.ventanaDeMantenimientosResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/ventana-de-mantenimientos');

  protected convertValueFromServer(restVentanaDeMantenimiento: RestVentanaDeMantenimiento): IVentanaDeMantenimiento {
    return {
      ...restVentanaDeMantenimiento,
      desde: restVentanaDeMantenimiento.desde ? dayjs(restVentanaDeMantenimiento.desde) : undefined,
      hasta: restVentanaDeMantenimiento.hasta ? dayjs(restVentanaDeMantenimiento.hasta) : undefined,
      repetirHasta: restVentanaDeMantenimiento.repetirHasta ? dayjs(restVentanaDeMantenimiento.repetirHasta) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class VentanaDeMantenimientoService extends VentanaDeMantenimientosService {
  protected readonly http = inject(HttpClient);

  create(ventanaDeMantenimiento: NewVentanaDeMantenimiento): Observable<IVentanaDeMantenimiento> {
    const copy = this.convertValueFromClient(ventanaDeMantenimiento);
    return this.http.post<RestVentanaDeMantenimiento>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ventanaDeMantenimiento: IVentanaDeMantenimiento): Observable<IVentanaDeMantenimiento> {
    const copy = this.convertValueFromClient(ventanaDeMantenimiento);
    return this.http
      .put<RestVentanaDeMantenimiento>(
        `${this.resourceUrl}/${encodeURIComponent(this.getVentanaDeMantenimientoIdentifier(ventanaDeMantenimiento))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ventanaDeMantenimiento: PartialUpdateVentanaDeMantenimiento): Observable<IVentanaDeMantenimiento> {
    const copy = this.convertValueFromClient(ventanaDeMantenimiento);
    return this.http
      .patch<RestVentanaDeMantenimiento>(
        `${this.resourceUrl}/${encodeURIComponent(this.getVentanaDeMantenimientoIdentifier(ventanaDeMantenimiento))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IVentanaDeMantenimiento> {
    return this.http
      .get<RestVentanaDeMantenimiento>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IVentanaDeMantenimiento[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVentanaDeMantenimiento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getVentanaDeMantenimientoIdentifier(ventanaDeMantenimiento: Pick<IVentanaDeMantenimiento, 'id'>): number {
    return ventanaDeMantenimiento.id;
  }

  compareVentanaDeMantenimiento(o1: Pick<IVentanaDeMantenimiento, 'id'> | null, o2: Pick<IVentanaDeMantenimiento, 'id'> | null): boolean {
    return o1 && o2 ? this.getVentanaDeMantenimientoIdentifier(o1) === this.getVentanaDeMantenimientoIdentifier(o2) : o1 === o2;
  }

  addVentanaDeMantenimientoToCollectionIfMissing<Type extends Pick<IVentanaDeMantenimiento, 'id'>>(
    ventanaDeMantenimientoCollection: Type[],
    ...ventanaDeMantenimientosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ventanaDeMantenimientos: Type[] = ventanaDeMantenimientosToCheck.filter(isPresent);
    if (ventanaDeMantenimientos.length > 0) {
      const ventanaDeMantenimientoCollectionIdentifiers = ventanaDeMantenimientoCollection.map(ventanaDeMantenimientoItem =>
        this.getVentanaDeMantenimientoIdentifier(ventanaDeMantenimientoItem),
      );
      const ventanaDeMantenimientosToAdd = ventanaDeMantenimientos.filter(ventanaDeMantenimientoItem => {
        const ventanaDeMantenimientoIdentifier = this.getVentanaDeMantenimientoIdentifier(ventanaDeMantenimientoItem);
        if (ventanaDeMantenimientoCollectionIdentifiers.includes(ventanaDeMantenimientoIdentifier)) {
          return false;
        }
        ventanaDeMantenimientoCollectionIdentifiers.push(ventanaDeMantenimientoIdentifier);
        return true;
      });
      return [...ventanaDeMantenimientosToAdd, ...ventanaDeMantenimientoCollection];
    }
    return ventanaDeMantenimientoCollection;
  }

  protected convertValueFromClient<T extends IVentanaDeMantenimiento | NewVentanaDeMantenimiento | PartialUpdateVentanaDeMantenimiento>(
    ventanaDeMantenimiento: T,
  ): RestOf<T> {
    return {
      ...ventanaDeMantenimiento,
      desde: ventanaDeMantenimiento.desde?.toJSON() ?? null,
      hasta: ventanaDeMantenimiento.hasta?.toJSON() ?? null,
      repetirHasta: ventanaDeMantenimiento.repetirHasta?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestVentanaDeMantenimiento): IVentanaDeMantenimiento {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestVentanaDeMantenimiento[]): IVentanaDeMantenimiento[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
