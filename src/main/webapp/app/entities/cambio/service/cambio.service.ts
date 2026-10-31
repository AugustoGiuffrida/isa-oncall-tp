import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICambio, NewCambio } from '../cambio.model';

export type PartialUpdateCambio = Partial<ICambio> & Pick<ICambio, 'id'>;

type RestOf<T extends ICambio | NewCambio> = Omit<T, 'creadoEn'> & {
  creadoEn?: string | null;
};

export type RestCambio = RestOf<ICambio>;

export type NewRestCambio = RestOf<NewCambio>;

export type PartialUpdateRestCambio = RestOf<PartialUpdateCambio>;

@Injectable()
export class CambiosService {
  readonly cambiosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly cambiosResource = httpResource<RestCambio[]>(() => {
    const params = this.cambiosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of cambio that have been fetched. It is updated when the cambiosResource emits a new value.
   * In case of error while fetching the cambios, the signal is set to an empty array.
   */
  readonly cambios = computed(() =>
    (this.cambiosResource.hasValue() ? this.cambiosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/cambios');

  protected convertValueFromServer(restCambio: RestCambio): ICambio {
    return {
      ...restCambio,
      creadoEn: restCambio.creadoEn ? dayjs(restCambio.creadoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class CambioService extends CambiosService {
  protected readonly http = inject(HttpClient);

  create(cambio: NewCambio): Observable<ICambio> {
    const copy = this.convertValueFromClient(cambio);
    return this.http.post<RestCambio>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cambio: ICambio): Observable<ICambio> {
    const copy = this.convertValueFromClient(cambio);
    return this.http
      .put<RestCambio>(`${this.resourceUrl}/${encodeURIComponent(this.getCambioIdentifier(cambio))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cambio: PartialUpdateCambio): Observable<ICambio> {
    const copy = this.convertValueFromClient(cambio);
    return this.http
      .patch<RestCambio>(`${this.resourceUrl}/${encodeURIComponent(this.getCambioIdentifier(cambio))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICambio> {
    return this.http.get<RestCambio>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICambio[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCambio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCambioIdentifier(cambio: Pick<ICambio, 'id'>): number {
    return cambio.id;
  }

  compareCambio(o1: Pick<ICambio, 'id'> | null, o2: Pick<ICambio, 'id'> | null): boolean {
    return o1 && o2 ? this.getCambioIdentifier(o1) === this.getCambioIdentifier(o2) : o1 === o2;
  }

  addCambioToCollectionIfMissing<Type extends Pick<ICambio, 'id'>>(
    cambioCollection: Type[],
    ...cambiosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cambios: Type[] = cambiosToCheck.filter(isPresent);
    if (cambios.length > 0) {
      const cambioCollectionIdentifiers = cambioCollection.map(cambioItem => this.getCambioIdentifier(cambioItem));
      const cambiosToAdd = cambios.filter(cambioItem => {
        const cambioIdentifier = this.getCambioIdentifier(cambioItem);
        if (cambioCollectionIdentifiers.includes(cambioIdentifier)) {
          return false;
        }
        cambioCollectionIdentifiers.push(cambioIdentifier);
        return true;
      });
      return [...cambiosToAdd, ...cambioCollection];
    }
    return cambioCollection;
  }

  protected convertValueFromClient<T extends ICambio | NewCambio | PartialUpdateCambio>(cambio: T): RestOf<T> {
    return {
      ...cambio,
      creadoEn: cambio.creadoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCambio): ICambio {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCambio[]): ICambio[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
