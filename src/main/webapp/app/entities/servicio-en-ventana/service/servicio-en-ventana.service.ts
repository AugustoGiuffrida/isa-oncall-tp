import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IServicioEnVentana, NewServicioEnVentana } from '../servicio-en-ventana.model';

export type PartialUpdateServicioEnVentana = Partial<IServicioEnVentana> & Pick<IServicioEnVentana, 'id'>;

@Injectable()
export class ServicioEnVentanasService {
  readonly servicioEnVentanasParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly servicioEnVentanasResource = httpResource<IServicioEnVentana[]>(() => {
    const params = this.servicioEnVentanasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of servicioEnVentana that have been fetched. It is updated when the servicioEnVentanasResource emits a new value.
   * In case of error while fetching the servicioEnVentanas, the signal is set to an empty array.
   */
  readonly servicioEnVentanas = computed(() => (this.servicioEnVentanasResource.hasValue() ? this.servicioEnVentanasResource.value() : []));
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/servicio-en-ventanas');
}

@Injectable({ providedIn: 'root' })
export class ServicioEnVentanaService extends ServicioEnVentanasService {
  protected readonly http = inject(HttpClient);

  create(servicioEnVentana: NewServicioEnVentana): Observable<IServicioEnVentana> {
    return this.http.post<IServicioEnVentana>(this.resourceUrl, servicioEnVentana);
  }

  update(servicioEnVentana: IServicioEnVentana): Observable<IServicioEnVentana> {
    return this.http.put<IServicioEnVentana>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServicioEnVentanaIdentifier(servicioEnVentana))}`,
      servicioEnVentana,
    );
  }

  partialUpdate(servicioEnVentana: PartialUpdateServicioEnVentana): Observable<IServicioEnVentana> {
    return this.http.patch<IServicioEnVentana>(
      `${this.resourceUrl}/${encodeURIComponent(this.getServicioEnVentanaIdentifier(servicioEnVentana))}`,
      servicioEnVentana,
    );
  }

  find(id: number): Observable<IServicioEnVentana> {
    return this.http.get<IServicioEnVentana>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IServicioEnVentana[]>> {
    const options = createRequestOption(req);
    return this.http.get<IServicioEnVentana[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getServicioEnVentanaIdentifier(servicioEnVentana: Pick<IServicioEnVentana, 'id'>): number {
    return servicioEnVentana.id;
  }

  compareServicioEnVentana(o1: Pick<IServicioEnVentana, 'id'> | null, o2: Pick<IServicioEnVentana, 'id'> | null): boolean {
    return o1 && o2 ? this.getServicioEnVentanaIdentifier(o1) === this.getServicioEnVentanaIdentifier(o2) : o1 === o2;
  }

  addServicioEnVentanaToCollectionIfMissing<Type extends Pick<IServicioEnVentana, 'id'>>(
    servicioEnVentanaCollection: Type[],
    ...servicioEnVentanasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const servicioEnVentanas: Type[] = servicioEnVentanasToCheck.filter(isPresent);
    if (servicioEnVentanas.length > 0) {
      const servicioEnVentanaCollectionIdentifiers = servicioEnVentanaCollection.map(servicioEnVentanaItem =>
        this.getServicioEnVentanaIdentifier(servicioEnVentanaItem),
      );
      const servicioEnVentanasToAdd = servicioEnVentanas.filter(servicioEnVentanaItem => {
        const servicioEnVentanaIdentifier = this.getServicioEnVentanaIdentifier(servicioEnVentanaItem);
        if (servicioEnVentanaCollectionIdentifiers.includes(servicioEnVentanaIdentifier)) {
          return false;
        }
        servicioEnVentanaCollectionIdentifiers.push(servicioEnVentanaIdentifier);
        return true;
      });
      return [...servicioEnVentanasToAdd, ...servicioEnVentanaCollection];
    }
    return servicioEnVentanaCollection;
  }
}
