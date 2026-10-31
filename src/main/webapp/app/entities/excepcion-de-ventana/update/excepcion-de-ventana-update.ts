import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { TipoExcepcionDeVentana } from 'app/entities/enumerations/tipo-excepcion-de-ventana.model';
import { VentanaDeMantenimientoService } from 'app/entities/ventana-de-mantenimiento/service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IExcepcionDeVentana } from '../excepcion-de-ventana.model';
import { ExcepcionDeVentanaService } from '../service/excepcion-de-ventana.service';

import { ExcepcionDeVentanaFormGroup, ExcepcionDeVentanaFormService } from './excepcion-de-ventana-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-excepcion-de-ventana-update',
  templateUrl: './excepcion-de-ventana-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class ExcepcionDeVentanaUpdate implements OnInit {
  readonly isSaving = signal(false);
  excepcionDeVentana: IExcepcionDeVentana | null = null;
  tipoExcepcionDeVentanaValues = Object.keys(TipoExcepcionDeVentana);

  ventanaDeMantenimientosSharedCollection = signal<IVentanaDeMantenimiento[]>([]);

  protected excepcionDeVentanaService = inject(ExcepcionDeVentanaService);
  protected excepcionDeVentanaFormService = inject(ExcepcionDeVentanaFormService);
  protected ventanaDeMantenimientoService = inject(VentanaDeMantenimientoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExcepcionDeVentanaFormGroup = this.excepcionDeVentanaFormService.createExcepcionDeVentanaFormGroup();

  compareVentanaDeMantenimiento = (o1: IVentanaDeMantenimiento | null, o2: IVentanaDeMantenimiento | null): boolean =>
    this.ventanaDeMantenimientoService.compareVentanaDeMantenimiento(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ excepcionDeVentana }) => {
      this.excepcionDeVentana = excepcionDeVentana;
      if (excepcionDeVentana) {
        this.updateForm(excepcionDeVentana);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const excepcionDeVentana = this.excepcionDeVentanaFormService.getExcepcionDeVentana(this.editForm);
    if (excepcionDeVentana.id === null) {
      this.subscribeToSaveResponse(this.excepcionDeVentanaService.create(excepcionDeVentana));
    } else {
      this.subscribeToSaveResponse(this.excepcionDeVentanaService.update(excepcionDeVentana));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IExcepcionDeVentana | null>): void {
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

  protected updateForm(excepcionDeVentana: IExcepcionDeVentana): void {
    this.excepcionDeVentana = excepcionDeVentana;
    this.excepcionDeVentanaFormService.resetForm(this.editForm, excepcionDeVentana);

    this.ventanaDeMantenimientosSharedCollection.update(ventanaDeMantenimientos =>
      this.ventanaDeMantenimientoService.addVentanaDeMantenimientoToCollectionIfMissing<IVentanaDeMantenimiento>(
        ventanaDeMantenimientos,
        excepcionDeVentana.ventana,
      ),
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
            this.excepcionDeVentana?.ventana,
          ),
        ),
      )
      .subscribe((ventanaDeMantenimientos: IVentanaDeMantenimiento[]) =>
        this.ventanaDeMantenimientosSharedCollection.set(ventanaDeMantenimientos),
      );
  }
}
