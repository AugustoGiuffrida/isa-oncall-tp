import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICambio, NewCambio } from '../cambio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICambio for edit and NewCambioFormGroupInput for create.
 */
type CambioFormGroupInput = ICambio | PartialWithRequiredKeyOf<NewCambio>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICambio | NewCambio> = Omit<T, 'creadoEn'> & {
  creadoEn?: string | null;
};

type CambioFormRawValue = FormValueOf<ICambio>;

type NewCambioFormRawValue = FormValueOf<NewCambio>;

type CambioFormDefaults = Pick<NewCambio, 'id' | 'creadoEn'>;

type CambioFormGroupContent = {
  id: FormControl<CambioFormRawValue['id'] | NewCambio['id']>;
  titulo: FormControl<CambioFormRawValue['titulo']>;
  descripcion: FormControl<CambioFormRawValue['descripcion']>;
  ticketUrl: FormControl<CambioFormRawValue['ticketUrl']>;
  creadoEn: FormControl<CambioFormRawValue['creadoEn']>;
  autor: FormControl<CambioFormRawValue['autor']>;
};

export type CambioFormGroup = FormGroup<CambioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CambioFormService {
  createCambioFormGroup(cambio?: CambioFormGroupInput): CambioFormGroup {
    const cambioRawValue = this.convertCambioToCambioRawValue({
      ...this.getFormDefaults(),
      ...(cambio ?? { id: null }),
    });

    return new FormGroup<CambioFormGroupContent>({
      id: new FormControl(
        { value: cambioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titulo: new FormControl(cambioRawValue.titulo, {
        validators: [Validators.required, Validators.maxLength(140)],
      }),
      descripcion: new FormControl(cambioRawValue.descripcion, {
        validators: [Validators.maxLength(2000)],
      }),
      ticketUrl: new FormControl(cambioRawValue.ticketUrl, {
        validators: [Validators.maxLength(255)],
      }),
      creadoEn: new FormControl(cambioRawValue.creadoEn, {
        validators: [Validators.required],
      }),
      autor: new FormControl(cambioRawValue.autor, {
        validators: [Validators.required],
      }),
    });
  }

  getCambio(form: CambioFormGroup): ICambio | NewCambio {
    return this.convertCambioRawValueToCambio(form.getRawValue());
  }

  resetForm(form: CambioFormGroup, cambio: CambioFormGroupInput): void {
    const cambioRawValue = this.convertCambioToCambioRawValue({ ...this.getFormDefaults(), ...cambio });
    form.reset({
      ...cambioRawValue,
      id: { value: cambioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CambioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creadoEn: currentTime,
    };
  }

  private convertCambioRawValueToCambio(rawCambio: CambioFormRawValue | NewCambioFormRawValue): ICambio | NewCambio {
    return {
      ...rawCambio,
      creadoEn: dayjs(rawCambio.creadoEn, DATE_TIME_FORMAT),
    };
  }

  private convertCambioToCambioRawValue(
    cambio: ICambio | (Partial<NewCambio> & CambioFormDefaults),
  ): CambioFormRawValue | PartialWithRequiredKeyOf<NewCambioFormRawValue> {
    return {
      ...cambio,
      creadoEn: cambio.creadoEn ? cambio.creadoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
