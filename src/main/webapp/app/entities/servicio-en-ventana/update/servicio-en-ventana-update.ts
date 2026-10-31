import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { VentanaDeMantenimientoService } from 'app/entities/ventana-de-mantenimiento/service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ServicioEnVentanaService } from '../service/servicio-en-ventana.service';
import { IServicioEnVentana } from '../servicio-en-ventana.model';

import { ServicioEnVentanaFormGroup, ServicioEnVentanaFormService } from './servicio-en-ventana-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-servicio-en-ventana-update',
  templateUrl: './servicio-en-ventana-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ServicioEnVentanaUpdate implements OnInit {
  readonly isSaving = signal(false);
  servicioEnVentana: IServicioEnVentana | null = null;

  ventanaDeMantenimientosSharedCollection = signal<IVentanaDeMantenimiento[]>([]);
  serviciosSharedCollection = signal<IServicio[]>([]);

  protected servicioEnVentanaService = inject(ServicioEnVentanaService);
  protected servicioEnVentanaFormService = inject(ServicioEnVentanaFormService);
  protected ventanaDeMantenimientoService = inject(VentanaDeMantenimientoService);
  protected servicioService = inject(ServicioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServicioEnVentanaFormGroup = this.servicioEnVentanaFormService.createServicioEnVentanaFormGroup();

  compareVentanaDeMantenimiento = (o1: IVentanaDeMantenimiento | null, o2: IVentanaDeMantenimiento | null): boolean =>
    this.ventanaDeMantenimientoService.compareVentanaDeMantenimiento(o1, o2);

  compareServicio = (o1: IServicio | null, o2: IServicio | null): boolean => this.servicioService.compareServicio(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ servicioEnVentana }) => {
      this.servicioEnVentana = servicioEnVentana;
      if (servicioEnVentana) {
        this.updateForm(servicioEnVentana);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const servicioEnVentana = this.servicioEnVentanaFormService.getServicioEnVentana(this.editForm);
    if (servicioEnVentana.id === null) {
      this.subscribeToSaveResponse(this.servicioEnVentanaService.create(servicioEnVentana));
    } else {
      this.subscribeToSaveResponse(this.servicioEnVentanaService.update(servicioEnVentana));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IServicioEnVentana | null>): void {
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

  protected updateForm(servicioEnVentana: IServicioEnVentana): void {
    this.servicioEnVentana = servicioEnVentana;
    this.servicioEnVentanaFormService.resetForm(this.editForm, servicioEnVentana);

    this.ventanaDeMantenimientosSharedCollection.update(ventanaDeMantenimientos =>
      this.ventanaDeMantenimientoService.addVentanaDeMantenimientoToCollectionIfMissing<IVentanaDeMantenimiento>(
        ventanaDeMantenimientos,
        servicioEnVentana.ventana,
      ),
    );
    this.serviciosSharedCollection.update(servicios =>
      this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, servicioEnVentana.servicio),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ventanaDeMantenimientoService
      .query()
      .pipe(map((res: HttpResponse<IVentanaDeMantenimiento[]>) => res.body ?? []))
      .pipe(
        map((ventanaDeMantenimientos: IVentanaDeMantenimiento[]) =>
          this.ventanaDeMantenimientoService.addVentanaDeMantenimientoToCollectionIfMissing<IVentanaDeMantenimiento>(
            ventanaDeMantenimientos,
            this.servicioEnVentana?.ventana,
          ),
        ),
      )
      .subscribe((ventanaDeMantenimientos: IVentanaDeMantenimiento[]) =>
        this.ventanaDeMantenimientosSharedCollection.set(ventanaDeMantenimientos),
      );

    this.servicioService
      .query()
      .pipe(map((res: HttpResponse<IServicio[]>) => res.body ?? []))
      .pipe(
        map((servicios: IServicio[]) =>
          this.servicioService.addServicioToCollectionIfMissing<IServicio>(servicios, this.servicioEnVentana?.servicio),
        ),
      )
      .subscribe((servicios: IServicio[]) => this.serviciosSharedCollection.set(servicios));
  }
}
