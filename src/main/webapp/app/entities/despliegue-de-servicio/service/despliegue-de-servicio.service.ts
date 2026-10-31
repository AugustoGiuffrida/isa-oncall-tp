import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IDespliegueDeServicio, NewDespliegueDeServicio } from '../despliegue-de-servicio.model';

export type PartialUpdateDespliegueDeServicio = Partial<IDespliegueDeServicio> & Pick<IDespliegueDeServicio, 'id'>;

type RestOf<T extends IDespliegueDeServicio | NewDespliegueDeServicio> = Omit<T, 'desplegadoEn'> & {
  desplegadoEn?: string | null;
};

export type RestDespliegueDeServicio = RestOf<IDespliegueDeServicio>;

export type NewRestDespliegueDeServicio = RestOf<NewDespliegueDeServicio>;

export type PartialUpdateRestDespliegueDeServicio = RestOf<PartialUpdateDespliegueDeServicio>;

@Injectable()
export class DespliegueDeServiciosService {
  readonly despliegueDeServiciosParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly despliegueDeServiciosResource = httpResource<RestDespliegueDeServicio[]>(() => {
    const params = this.despliegueDeServiciosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of despliegueDeServicio that have been fetched. It is updated when the despliegueDeServiciosResource emits a new value.
   * In case of error while fetching the despliegueDeServicios, the signal is set to an empty array.
   */
  readonly despliegueDeServicios = computed(() =>
    (this.despliegueDeServiciosResource.hasValue() ? this.despliegueDeServiciosResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/despliegue-de-servicios');

  protected convertValueFromServer(restDespliegueDeServicio: RestDespliegueDeServicio): IDespliegueDeServicio {
    return {
      ...restDespliegueDeServicio,
      desplegadoEn: restDespliegueDeServicio.desplegadoEn ? dayjs(restDespliegueDeServicio.desplegadoEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class DespliegueDeServicioService extends DespliegueDeServiciosService {
  protected readonly http = inject(HttpClient);

  create(despliegueDeServicio: NewDespliegueDeServicio): Observable<IDespliegueDeServicio> {
    const copy = this.convertValueFromClient(despliegueDeServicio);
    return this.http.post<RestDespliegueDeServicio>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(despliegueDeServicio: IDespliegueDeServicio): Observable<IDespliegueDeServicio> {
    const copy = this.convertValueFromClient(despliegueDeServicio);
    return this.http
      .put<RestDespliegueDeServicio>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDespliegueDeServicioIdentifier(despliegueDeServicio))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(despliegueDeServicio: PartialUpdateDespliegueDeServicio): Observable<IDespliegueDeServicio> {
    const copy = this.convertValueFromClient(despliegueDeServicio);
    return this.http
      .patch<RestDespliegueDeServicio>(
        `${this.resourceUrl}/${encodeURIComponent(this.getDespliegueDeServicioIdentifier(despliegueDeServicio))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IDespliegueDeServicio> {
    return this.http
      .get<RestDespliegueDeServicio>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IDespliegueDeServicio[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDespliegueDeServicio[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getDespliegueDeServicioIdentifier(despliegueDeServicio: Pick<IDespliegueDeServicio, 'id'>): number {
    return despliegueDeServicio.id;
  }

  compareDespliegueDeServicio(o1: Pick<IDespliegueDeServicio, 'id'> | null, o2: Pick<IDespliegueDeServicio, 'id'> | null): boolean {
    return o1 && o2 ? this.getDespliegueDeServicioIdentifier(o1) === this.getDespliegueDeServicioIdentifier(o2) : o1 === o2;
  }

  addDespliegueDeServicioToCollectionIfMissing<Type extends Pick<IDespliegueDeServicio, 'id'>>(
    despliegueDeServicioCollection: Type[],
    ...despliegueDeServiciosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const despliegueDeServicios: Type[] = despliegueDeServiciosToCheck.filter(isPresent);
    if (despliegueDeServicios.length > 0) {
      const despliegueDeServicioCollectionIdentifiers = despliegueDeServicioCollection.map(despliegueDeServicioItem =>
        this.getDespliegueDeServicioIdentifier(despliegueDeServicioItem),
      );
      const despliegueDeServiciosToAdd = despliegueDeServicios.filter(despliegueDeServicioItem => {
        const despliegueDeServicioIdentifier = this.getDespliegueDeServicioIdentifier(despliegueDeServicioItem);
        if (despliegueDeServicioCollectionIdentifiers.includes(despliegueDeServicioIdentifier)) {
          return false;
        }
        despliegueDeServicioCollectionIdentifiers.push(despliegueDeServicioIdentifier);
        return true;
      });
      return [...despliegueDeServiciosToAdd, ...despliegueDeServicioCollection];
    }
    return despliegueDeServicioCollection;
  }

  protected convertValueFromClient<T extends IDespliegueDeServicio | NewDespliegueDeServicio | PartialUpdateDespliegueDeServicio>(
    despliegueDeServicio: T,
  ): RestOf<T> {
    return {
      ...despliegueDeServicio,
      desplegadoEn: despliegueDeServicio.desplegadoEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestDespliegueDeServicio): IDespliegueDeServicio {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestDespliegueDeServicio[]): IDespliegueDeServicio[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
