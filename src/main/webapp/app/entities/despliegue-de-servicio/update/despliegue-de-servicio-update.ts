import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { ICambio } from 'app/entities/cambio/cambio.model';
import { CambioService } from 'app/entities/cambio/service/cambio.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IDespliegueDeServicio } from '../despliegue-de-servicio.model';
import { DespliegueDeServicioService } from '../service/despliegue-de-servicio.service';

import { DespliegueDeServicioFormGroup, DespliegueDeServicioFormService } from './despliegue-de-servicio-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-despliegue-de-servicio-update',
  templateUrl: './despliegue-de-servicio-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class DespliegueDeServicioUpdate implements OnInit {
  readonly isSaving = signal(false);
  despliegueDeServicio: IDespliegueDeServicio | null = null;

  cambiosSharedCollection = signal<ICambio[]>([]);
  serviciosSharedCollection = signal<IServicio[]>([]);

  protected despliegueDeServicioService = inject(DespliegueDeServicioService);
  protected despliegueDeServicioFormService = inject(DespliegueDeServicioFormService);
  protected cambioService = inject(CambioService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DespliegueDeServicioFormGroup = this.despliegueDeServicioFormService.createDespliegueDeServicioFormGroup();

  compareCambio = (o1: ICambio | null, o2: ICambio | null): boolean => this.cambioService.compareCambio(o1, o2);

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ despliegueDeServicio }) => {
      this.despliegueDeServicio = despliegueDeServicio;
      if (despliegueDeServicio) {
        this.updateForm(despliegueDeServicio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const despliegueDeServicio = this.despliegueDeServicioFormService.getDespliegueDeServicio(this.editForm);
    if (despliegueDeServicio.id === null) {
      this.subscribeToSaveResponse(this.despliegueDeServicioService.create(despliegueDeServicio));
    } else {
      this.subscribeToSaveResponse(this.despliegueDeServicioService.update(despliegueDeServicio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IDespliegueDeServicio | null>): void {
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

  protected updateForm(despliegueDeServicio: IDespliegueDeServicio): void {
    this.despliegueDeServicio = despliegueDeServicio;
    this.despliegueDeServicioFormService.resetForm(this.editForm, despliegueDeServicio);

    this.cambiosSharedCollection.update(cambios =>
      this.cambioService.addCambioToCollectionIfMissing<ICambio>(cambios, despliegueDeServicio.cambio),
    );
    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, despliegueDeServicio.servicio),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cambioService
      .query()
      .pipe(map((res: HttpResponse<ICambio[]>) => res.body ?? []))
      .pipe(
        map((cambios: ICambio[]) => this.cambioService.addCambioToCollectionIfMissing<ICambio>(cambios, this.despliegueDeServicio?.cambio)),
      )
      .subscribe((cambios: ICambio[]) => this.cambiosSharedCollection.set(cambios));

    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.despliegueDeServicio?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
