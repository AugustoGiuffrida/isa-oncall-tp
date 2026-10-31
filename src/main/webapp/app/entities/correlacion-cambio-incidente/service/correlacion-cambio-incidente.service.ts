import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { ICorrelacionCambioIncidente, NewCorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';

export type PartialUpdateCorrelacionCambioIncidente = Partial<ICorrelacionCambioIncidente> & Pick<ICorrelacionCambioIncidente, 'id'>;

type RestOf<T extends ICorrelacionCambioIncidente | NewCorrelacionCambioIncidente> = Omit<T, 'detectadaEn'> & {
  detectadaEn?: string | null;
};

export type RestCorrelacionCambioIncidente = RestOf<ICorrelacionCambioIncidente>;

export type NewRestCorrelacionCambioIncidente = RestOf<NewCorrelacionCambioIncidente>;

export type PartialUpdateRestCorrelacionCambioIncidente = RestOf<PartialUpdateCorrelacionCambioIncidente>;

@Injectable()
export class CorrelacionCambioIncidentesService {
  readonly correlacionCambioIncidentesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly correlacionCambioIncidentesResource = httpResource<RestCorrelacionCambioIncidente[]>(() => {
    const params = this.correlacionCambioIncidentesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of correlacionCambioIncidente that have been fetched. It is updated when the correlacionCambioIncidentesResource emits a new value.
   * In case of error while fetching the correlacionCambioIncidentes, the signal is set to an empty array.
   */
  readonly correlacionCambioIncidentes = computed(() =>
    (this.correlacionCambioIncidentesResource.hasValue() ? this.correlacionCambioIncidentesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/correlacion-cambio-incidentes');

  protected convertValueFromServer(restCorrelacionCambioIncidente: RestCorrelacionCambioIncidente): ICorrelacionCambioIncidente {
    return {
      ...restCorrelacionCambioIncidente,
      detectadaEn: restCorrelacionCambioIncidente.detectadaEn ? dayjs(restCorrelacionCambioIncidente.detectadaEn) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class CorrelacionCambioIncidenteService extends CorrelacionCambioIncidentesService {
  protected readonly http = inject(HttpClient);

  create(correlacionCambioIncidente: NewCorrelacionCambioIncidente): Observable<ICorrelacionCambioIncidente> {
    const copy = this.convertValueFromClient(correlacionCambioIncidente);
    return this.http.post<RestCorrelacionCambioIncidente>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(correlacionCambioIncidente: ICorrelacionCambioIncidente): Observable<ICorrelacionCambioIncidente> {
    const copy = this.convertValueFromClient(correlacionCambioIncidente);
    return this.http
      .put<RestCorrelacionCambioIncidente>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCorrelacionCambioIncidenteIdentifier(correlacionCambioIncidente))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(correlacionCambioIncidente: PartialUpdateCorrelacionCambioIncidente): Observable<ICorrelacionCambioIncidente> {
    const copy = this.convertValueFromClient(correlacionCambioIncidente);
    return this.http
      .patch<RestCorrelacionCambioIncidente>(
        `${this.resourceUrl}/${encodeURIComponent(this.getCorrelacionCambioIncidenteIdentifier(correlacionCambioIncidente))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ICorrelacionCambioIncidente> {
    return this.http
      .get<RestCorrelacionCambioIncidente>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ICorrelacionCambioIncidente[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCorrelacionCambioIncidente[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCorrelacionCambioIncidenteIdentifier(correlacionCambioIncidente: Pick<ICorrelacionCambioIncidente, 'id'>): number {
    return correlacionCambioIncidente.id;
  }

  compareCorrelacionCambioIncidente(
    o1: Pick<ICorrelacionCambioIncidente, 'id'> | null,
    o2: Pick<ICorrelacionCambioIncidente, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getCorrelacionCambioIncidenteIdentifier(o1) === this.getCorrelacionCambioIncidenteIdentifier(o2) : o1 === o2;
  }

  addCorrelacionCambioIncidenteToCollectionIfMissing<Type extends Pick<ICorrelacionCambioIncidente, 'id'>>(
    correlacionCambioIncidenteCollection: Type[],
    ...correlacionCambioIncidentesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const correlacionCambioIncidentes: Type[] = correlacionCambioIncidentesToCheck.filter(isPresent);
    if (correlacionCambioIncidentes.length > 0) {
      const correlacionCambioIncidenteCollectionIdentifiers = correlacionCambioIncidenteCollection.map(correlacionCambioIncidenteItem =>
        this.getCorrelacionCambioIncidenteIdentifier(correlacionCambioIncidenteItem),
      );
      const correlacionCambioIncidentesToAdd = correlacionCambioIncidentes.filter(correlacionCambioIncidenteItem => {
        const correlacionCambioIncidenteIdentifier = this.getCorrelacionCambioIncidenteIdentifier(correlacionCambioIncidenteItem);
        if (correlacionCambioIncidenteCollectionIdentifiers.includes(correlacionCambioIncidenteIdentifier)) {
          return false;
        }
        correlacionCambioIncidenteCollectionIdentifiers.push(correlacionCambioIncidenteIdentifier);
        return true;
      });
      return [...correlacionCambioIncidentesToAdd, ...correlacionCambioIncidenteCollection];
    }
    return correlacionCambioIncidenteCollection;
  }

  protected convertValueFromClient<
    T extends ICorrelacionCambioIncidente | NewCorrelacionCambioIncidente | PartialUpdateCorrelacionCambioIncidente,
  >(correlacionCambioIncidente: T): RestOf<T> {
    return {
      ...correlacionCambioIncidente,
      detectadaEn: correlacionCambioIncidente.detectadaEn?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestCorrelacionCambioIncidente): ICorrelacionCambioIncidente {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestCorrelacionCambioIncidente[]): ICorrelacionCambioIncidente[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
