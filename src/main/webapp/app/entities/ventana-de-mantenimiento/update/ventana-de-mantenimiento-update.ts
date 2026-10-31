import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize } from 'rxjs';

import { Recurrencia } from 'app/entities/enumerations/recurrencia.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { VentanaDeMantenimientoService } from '../service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

import { VentanaDeMantenimientoFormGroup, VentanaDeMantenimientoFormService } from './ventana-de-mantenimiento-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-ventana-de-mantenimiento-update',
  templateUrl: './ventana-de-mantenimiento-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class VentanaDeMantenimientoUpdate implements OnInit {
  readonly isSaving = signal(false);
  ventanaDeMantenimiento: IVentanaDeMantenimiento | null = null;
  recurrenciaValues = Object.keys(Recurrencia);

  protected ventanaDeMantenimientoService = inject(VentanaDeMantenimientoService);
  protected ventanaDeMantenimientoFormService = inject(VentanaDeMantenimientoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VentanaDeMantenimientoFormGroup = this.ventanaDeMantenimientoFormService.createVentanaDeMantenimientoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventanaDeMantenimiento }) => {
      this.ventanaDeMantenimiento = ventanaDeMantenimiento;
      if (ventanaDeMantenimiento) {
        this.updateForm(ventanaDeMantenimiento);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const ventanaDeMantenimiento = this.ventanaDeMantenimientoFormService.getVentanaDeMantenimiento(this.editForm);
    if (ventanaDeMantenimiento.id === null) {
      this.subscribeToSaveResponse(this.ventanaDeMantenimientoService.create(ventanaDeMantenimiento));
    } else {
      this.subscribeToSaveResponse(this.ventanaDeMantenimientoService.update(ventanaDeMantenimiento));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IVentanaDeMantenimiento | null>): void {
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

  protected updateForm(ventanaDeMantenimiento: IVentanaDeMantenimiento): void {
    this.ventanaDeMantenimiento = ventanaDeMantenimiento;
    this.ventanaDeMantenimientoFormService.resetForm(this.editForm, ventanaDeMantenimiento);
  }
}
