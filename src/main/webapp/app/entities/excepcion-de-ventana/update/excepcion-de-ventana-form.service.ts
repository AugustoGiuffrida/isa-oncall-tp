import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExcepcionDeVentana, NewExcepcionDeVentana } from '../excepcion-de-ventana.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExcepcionDeVentana for edit and NewExcepcionDeVentanaFormGroupInput for create.
 */
type ExcepcionDeVentanaFormGroupInput = IExcepcionDeVentana | PartialWithRequiredKeyOf<NewExcepcionDeVentana>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IExcepcionDeVentana | NewExcepcionDeVentana> = Omit<T, 'nuevoDesde' | 'nuevoHasta'> & {
  nuevoDesde?: string | null;
  nuevoHasta?: string | null;
};

type ExcepcionDeVentanaFormRawValue = FormValueOf<IExcepcionDeVentana>;

type NewExcepcionDeVentanaFormRawValue = FormValueOf<NewExcepcionDeVentana>;

type ExcepcionDeVentanaFormDefaults = Pick<NewExcepcionDeVentana, 'id' | 'nuevoDesde' | 'nuevoHasta'>;

type ExcepcionDeVentanaFormGroupContent = {
  id: FormControl<ExcepcionDeVentanaFormRawValue['id'] | NewExcepcionDeVentana['id']>;
  fecha: FormControl<ExcepcionDeVentanaFormRawValue['fecha']>;
  tipo: FormControl<ExcepcionDeVentanaFormRawValue['tipo']>;
  nuevoDesde: FormControl<ExcepcionDeVentanaFormRawValue['nuevoDesde']>;
  nuevoHasta: FormControl<ExcepcionDeVentanaFormRawValue['nuevoHasta']>;
  motivo: FormControl<ExcepcionDeVentanaFormRawValue['motivo']>;
  ventana: FormControl<ExcepcionDeVentanaFormRawValue['ventana']>;
};

export type ExcepcionDeVentanaFormGroup = FormGroup<ExcepcionDeVentanaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExcepcionDeVentanaFormService {
  createExcepcionDeVentanaFormGroup(excepcionDeVentana?: ExcepcionDeVentanaFormGroupInput): ExcepcionDeVentanaFormGroup {
    const excepcionDeVentanaRawValue = this.convertExcepcionDeVentanaToExcepcionDeVentanaRawValue({
      ...this.getFormDefaults(),
      ...(excepcionDeVentana ?? { id: null }),
    });

    return new FormGroup<ExcepcionDeVentanaFormGroupContent>({
      id: new FormControl(
        { value: excepcionDeVentanaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      fecha: new FormControl(excepcionDeVentanaRawValue.fecha, {
        validators: [Validators.required],
      }),
      tipo: new FormControl(excepcionDeVentanaRawValue.tipo, {
        validators: [Validators.required],
      }),
      nuevoDesde: new FormControl(excepcionDeVentanaRawValue.nuevoDesde),
      nuevoHasta: new FormControl(excepcionDeVentanaRawValue.nuevoHasta),
      motivo: new FormControl(excepcionDeVentanaRawValue.motivo, {
        validators: [Validators.maxLength(255)],
      }),
      ventana: new FormControl(excepcionDeVentanaRawValue.ventana, {
        validators: [Validators.required],
      }),
    });
  }

  getExcepcionDeVentana(form: ExcepcionDeVentanaFormGroup): IExcepcionDeVentana | NewExcepcionDeVentana {
    return this.convertExcepcionDeVentanaRawValueToExcepcionDeVentana(form.getRawValue());
  }

  resetForm(form: ExcepcionDeVentanaFormGroup, excepcionDeVentana: ExcepcionDeVentanaFormGroupInput): void {
    const excepcionDeVentanaRawValue = this.convertExcepcionDeVentanaToExcepcionDeVentanaRawValue({
      ...this.getFormDefaults(),
      ...excepcionDeVentana,
    });
    form.reset({
      ...excepcionDeVentanaRawValue,
      id: { value: excepcionDeVentanaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ExcepcionDeVentanaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      nuevoDesde: currentTime,
      nuevoHasta: currentTime,
    };
  }

  private convertExcepcionDeVentanaRawValueToExcepcionDeVentana(
    rawExcepcionDeVentana: ExcepcionDeVentanaFormRawValue | NewExcepcionDeVentanaFormRawValue,
  ): IExcepcionDeVentana | NewExcepcionDeVentana {
    return {
      ...rawExcepcionDeVentana,
      nuevoDesde: dayjs(rawExcepcionDeVentana.nuevoDesde, DATE_TIME_FORMAT),
      nuevoHasta: dayjs(rawExcepcionDeVentana.nuevoHasta, DATE_TIME_FORMAT),
    };
  }

  private convertExcepcionDeVentanaToExcepcionDeVentanaRawValue(
    excepcionDeVentana: IExcepcionDeVentana | (Partial<NewExcepcionDeVentana> & ExcepcionDeVentanaFormDefaults),
  ): ExcepcionDeVentanaFormRawValue | PartialWithRequiredKeyOf<NewExcepcionDeVentanaFormRawValue> {
    return {
      ...excepcionDeVentana,
      nuevoDesde: excepcionDeVentana.nuevoDesde ? excepcionDeVentana.nuevoDesde.format(DATE_TIME_FORMAT) : undefined,
      nuevoHasta: excepcionDeVentana.nuevoHasta ? excepcionDeVentana.nuevoHasta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
