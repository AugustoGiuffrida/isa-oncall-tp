import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IDespliegueDeServicio } from 'app/entities/despliegue-de-servicio/despliegue-de-servicio.model';
import { DespliegueDeServicioService } from 'app/entities/despliegue-de-servicio/service/despliegue-de-servicio.service';
import { EstadoCorrelacion } from 'app/entities/enumerations/estado-correlacion.model';
import { OrigenCorrelacion } from 'app/entities/enumerations/origen-correlacion.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';

import { ICorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';
import { CorrelacionCambioIncidenteService } from '../service/correlacion-cambio-incidente.service';

import { CorrelacionCambioIncidenteFormGroup, CorrelacionCambioIncidenteFormService } from './correlacion-cambio-incidente-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-correlacion-cambio-incidente-update',
  templateUrl: './correlacion-cambio-incidente-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CorrelacionCambioIncidenteUpdate implements OnInit {
  readonly isSaving = signal(false);
  correlacionCambioIncidente: ICorrelacionCambioIncidente | null = null;
  origenCorrelacionValues = Object.keys(OrigenCorrelacion);
  estadoCorrelacionValues = Object.keys(EstadoCorrelacion);

  incidentesSharedCollection = signal<IIncidente[]>([]);
  despliegueDeServiciosSharedCollection = signal<IDespliegueDeServicio[]>([]);

  protected correlacionCambioIncidenteService = inject(CorrelacionCambioIncidenteService);
  protected correlacionCambioIncidenteFormService = inject(CorrelacionCambioIncidenteFormService);
  protected incidenteService = inject(IncidenteService);
  protected despliegueDeServicioService = inject(DespliegueDeServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CorrelacionCambioIncidenteFormGroup = this.correlacionCambioIncidenteFormService.createCorrelacionCambioIncidenteFormGroup();

  compareIncidente = (o1: IIncidente | null, o2: IIncidente | null): boolean => this.incidenteService.compareIncidente(o1, o2);

  compareDespliegueDeServicio = (o1: IDespliegueDeServicio | null, o2: IDespliegueDeServicio | null): boolean =>
    this.despliegueDeServicioService.compareDespliegueDeServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ correlacionCambioIncidente }) => {
      this.correlacionCambioIncidente = correlacionCambioIncidente;
      if (correlacionCambioIncidente) {
        this.updateForm(correlacionCambioIncidente);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const correlacionCambioIncidente = this.correlacionCambioIncidenteFormService.getCorrelacionCambioIncidente(this.editForm);
    if (correlacionCambioIncidente.id === null) {
      this.subscribeToSaveResponse(this.correlacionCambioIncidenteService.create(correlacionCambioIncidente));
    } else {
      this.subscribeToSaveResponse(this.correlacionCambioIncidenteService.update(correlacionCambioIncidente));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICorrelacionCambioIncidente | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(correlacionCambioIncidente: ICorrelacionCambioIncidente): void {
    this.correlacionCambioIncidente = correlacionCambioIncidente;
    this.correlacionCambioIncidenteFormService.resetForm(this.editForm, correlacionCambioIncidente);

    this.incidentesSharedCollection.update(incidentes =>
      this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, correlacionCambioIncidente.incidente),
    );
    this.despliegueDeServiciosSharedCollection.update(despliegueDeServicios =>
      this.despliegueDeServicioService.addDespliegueDeServicioToCollectionIfMissing<IDespliegueDeServicio>(
        despliegueDeServicios,
        correlacionCambioIncidente.despliegue,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.incidenteService
      .query()
      .pipe(map((res: HttpResponse<IIncidente[]>) => res.body ?? []))
      .pipe(
        map((incidentes: IIncidente[]) =>
          this.incidenteService.addIncidenteToCollectionIfMissing<IIncidente>(incidentes, this.correlacionCambioIncidente?.incidente),
        ),
      )
      .subscribe((incidentes: IIncidente[]) => this.incidentesSharedCollection.set(incidentes));

    this.despliegueDeServicioService
      .query()
      .pipe(map((res: HttpResponse<IDespliegueDeServicio[]>) => res.body ?? []))
      .pipe(
        map((despliegueDeServicios: IDespliegueDeServicio[]) =>
          this.despliegueDeServicioService.addDespliegueDeServicioToCollectionIfMissing<IDespliegueDeServicio>(
            despliegueDeServicios,
            this.correlacionCambioIncidente?.despliegue,
          ),
        ),
      )
      .subscribe((despliegueDeServicios: IDespliegueDeServicio[]) => this.despliegueDeServiciosSharedCollection.set(despliegueDeServicios));
  }
}
