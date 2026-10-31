import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVentanaDeMantenimiento, NewVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVentanaDeMantenimiento for edit and NewVentanaDeMantenimientoFormGroupInput for create.
 */
type VentanaDeMantenimientoFormGroupInput = IVentanaDeMantenimiento | PartialWithRequiredKeyOf<NewVentanaDeMantenimiento>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVentanaDeMantenimiento | NewVentanaDeMantenimiento> = Omit<T, 'desde' | 'hasta'> & {
  desde?: string | null;
  hasta?: string | null;
};

type VentanaDeMantenimientoFormRawValue = FormValueOf<IVentanaDeMantenimiento>;

type NewVentanaDeMantenimientoFormRawValue = FormValueOf<NewVentanaDeMantenimiento>;

type VentanaDeMantenimientoFormDefaults = Pick<NewVentanaDeMantenimiento, 'id' | 'desde' | 'hasta' | 'activa'>;

type VentanaDeMantenimientoFormGroupContent = {
  id: FormControl<VentanaDeMantenimientoFormRawValue['id'] | NewVentanaDeMantenimiento['id']>;
  nombre: FormControl<VentanaDeMantenimientoFormRawValue['nombre']>;
  descripcion: FormControl<VentanaDeMantenimientoFormRawValue['descripcion']>;
  desde: FormControl<VentanaDeMantenimientoFormRawValue['desde']>;
  hasta: FormControl<VentanaDeMantenimientoFormRawValue['hasta']>;
  recurrencia: FormControl<VentanaDeMantenimientoFormRawValue['recurrencia']>;
  zonaHoraria: FormControl<VentanaDeMantenimientoFormRawValue['zonaHoraria']>;
  repetirHasta: FormControl<VentanaDeMantenimientoFormRawValue['repetirHasta']>;
  activa: FormControl<VentanaDeMantenimientoFormRawValue['activa']>;
};

export type VentanaDeMantenimientoFormGroup = FormGroup<VentanaDeMantenimientoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VentanaDeMantenimientoFormService {
  createVentanaDeMantenimientoFormGroup(ventanaDeMantenimiento?: VentanaDeMantenimientoFormGroupInput): VentanaDeMantenimientoFormGroup {
    const ventanaDeMantenimientoRawValue = this.convertVentanaDeMantenimientoToVentanaDeMantenimientoRawValue({
      ...this.getFormDefaults(),
      ...(ventanaDeMantenimiento ?? { id: null }),
    });

    return new FormGroup<VentanaDeMantenimientoFormGroupContent>({
      id: new FormControl(
        { value: ventanaDeMantenimientoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(ventanaDeMantenimientoRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      descripcion: new FormControl(ventanaDeMantenimientoRawValue.descripcion, {
        validators: [Validators.maxLength(500)],
      }),
      desde: new FormControl(ventanaDeMantenimientoRawValue.desde, {
        validators: [Validators.required],
      }),
      hasta: new FormControl(ventanaDeMantenimientoRawValue.hasta, {
        validators: [Validators.required],
      }),
      recurrencia: new FormControl(ventanaDeMantenimientoRawValue.recurrencia, {
        validators: [Validators.required],
      }),
      zonaHoraria: new FormControl(ventanaDeMantenimientoRawValue.zonaHoraria, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      repetirHasta: new FormControl(ventanaDeMantenimientoRawValue.repetirHasta),
      activa: new FormControl(ventanaDeMantenimientoRawValue.activa, {
        validators: [Validators.required],
      }),
    });
  }

  getVentanaDeMantenimiento(form: VentanaDeMantenimientoFormGroup): IVentanaDeMantenimiento | NewVentanaDeMantenimiento {
    return this.convertVentanaDeMantenimientoRawValueToVentanaDeMantenimiento(form.getRawValue());
  }

  resetForm(form: VentanaDeMantenimientoFormGroup, ventanaDeMantenimiento: VentanaDeMantenimientoFormGroupInput): void {
    const ventanaDeMantenimientoRawValue = this.convertVentanaDeMantenimientoToVentanaDeMantenimientoRawValue({
      ...this.getFormDefaults(),
      ...ventanaDeMantenimiento,
    });
    form.reset({
      ...ventanaDeMantenimientoRawValue,
      id: { value: ventanaDeMantenimientoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): VentanaDeMantenimientoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      desde: currentTime,
      hasta: currentTime,
      activa: false,
    };
  }

  private convertVentanaDeMantenimientoRawValueToVentanaDeMantenimiento(
    rawVentanaDeMantenimiento: VentanaDeMantenimientoFormRawValue | NewVentanaDeMantenimientoFormRawValue,
  ): IVentanaDeMantenimiento | NewVentanaDeMantenimiento {
    return {
      ...rawVentanaDeMantenimiento,
      desde: dayjs(rawVentanaDeMantenimiento.desde, DATE_TIME_FORMAT),
      hasta: dayjs(rawVentanaDeMantenimiento.hasta, DATE_TIME_FORMAT),
    };
  }

  private convertVentanaDeMantenimientoToVentanaDeMantenimientoRawValue(
    ventanaDeMantenimiento: IVentanaDeMantenimiento | (Partial<NewVentanaDeMantenimiento> & VentanaDeMantenimientoFormDefaults),
  ): VentanaDeMantenimientoFormRawValue | PartialWithRequiredKeyOf<NewVentanaDeMantenimientoFormRawValue> {
    return {
      ...ventanaDeMantenimiento,
      desde: ventanaDeMantenimiento.desde ? ventanaDeMantenimiento.desde.format(DATE_TIME_FORMAT) : undefined,
      hasta: ventanaDeMantenimiento.hasta ? ventanaDeMantenimiento.hasta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
