import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICorrelacionCambioIncidente, NewCorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICorrelacionCambioIncidente for edit and NewCorrelacionCambioIncidenteFormGroupInput for create.
 */
type CorrelacionCambioIncidenteFormGroupInput = ICorrelacionCambioIncidente | PartialWithRequiredKeyOf<NewCorrelacionCambioIncidente>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICorrelacionCambioIncidente | NewCorrelacionCambioIncidente> = Omit<T, 'detectadaEn'> & {
  detectadaEn?: string | null;
};

type CorrelacionCambioIncidenteFormRawValue = FormValueOf<ICorrelacionCambioIncidente>;

type NewCorrelacionCambioIncidenteFormRawValue = FormValueOf<NewCorrelacionCambioIncidente>;

type CorrelacionCambioIncidenteFormDefaults = Pick<NewCorrelacionCambioIncidente, 'id' | 'detectadaEn'>;

type CorrelacionCambioIncidenteFormGroupContent = {
  id: FormControl<CorrelacionCambioIncidenteFormRawValue['id'] | NewCorrelacionCambioIncidente['id']>;
  origen: FormControl<CorrelacionCambioIncidenteFormRawValue['origen']>;
  estado: FormControl<CorrelacionCambioIncidenteFormRawValue['estado']>;
  detectadaEn: FormControl<CorrelacionCambioIncidenteFormRawValue['detectadaEn']>;
  nota: FormControl<CorrelacionCambioIncidenteFormRawValue['nota']>;
  incidente: FormControl<CorrelacionCambioIncidenteFormRawValue['incidente']>;
  despliegue: FormControl<CorrelacionCambioIncidenteFormRawValue['despliegue']>;
};

export type CorrelacionCambioIncidenteFormGroup = FormGroup<CorrelacionCambioIncidenteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CorrelacionCambioIncidenteFormService {
  createCorrelacionCambioIncidenteFormGroup(
    correlacionCambioIncidente?: CorrelacionCambioIncidenteFormGroupInput,
  ): CorrelacionCambioIncidenteFormGroup {
    const correlacionCambioIncidenteRawValue = this.convertCorrelacionCambioIncidenteToCorrelacionCambioIncidenteRawValue({
      ...this.getFormDefaults(),
      ...(correlacionCambioIncidente ?? { id: null }),
    });

    return new FormGroup<CorrelacionCambioIncidenteFormGroupContent>({
      id: new FormControl(
        { value: correlacionCambioIncidenteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      origen: new FormControl(correlacionCambioIncidenteRawValue.origen, {
        validators: [Validators.required],
      }),
      estado: new FormControl(correlacionCambioIncidenteRawValue.estado, {
        validators: [Validators.required],
      }),
      detectadaEn: new FormControl(correlacionCambioIncidenteRawValue.detectadaEn, {
        validators: [Validators.required],
      }),
      nota: new FormControl(correlacionCambioIncidenteRawValue.nota, {
        validators: [Validators.maxLength(255)],
      }),
      incidente: new FormControl(correlacionCambioIncidenteRawValue.incidente, {
        validators: [Validators.required],
      }),
      despliegue: new FormControl(correlacionCambioIncidenteRawValue.despliegue, {
        validators: [Validators.required],
      }),
    });
  }

  getCorrelacionCambioIncidente(form: CorrelacionCambioIncidenteFormGroup): ICorrelacionCambioIncidente | NewCorrelacionCambioIncidente {
    return this.convertCorrelacionCambioIncidenteRawValueToCorrelacionCambioIncidente(form.getRawValue());
  }

  resetForm(form: CorrelacionCambioIncidenteFormGroup, correlacionCambioIncidente: CorrelacionCambioIncidenteFormGroupInput): void {
    const correlacionCambioIncidenteRawValue = this.convertCorrelacionCambioIncidenteToCorrelacionCambioIncidenteRawValue({
      ...this.getFormDefaults(),
      ...correlacionCambioIncidente,
    });
    form.reset({
      ...correlacionCambioIncidenteRawValue,
      id: { value: correlacionCambioIncidenteRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CorrelacionCambioIncidenteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      detectadaEn: currentTime,
    };
  }

  private convertCorrelacionCambioIncidenteRawValueToCorrelacionCambioIncidente(
    rawCorrelacionCambioIncidente: CorrelacionCambioIncidenteFormRawValue | NewCorrelacionCambioIncidenteFormRawValue,
  ): ICorrelacionCambioIncidente | NewCorrelacionCambioIncidente {
    return {
      ...rawCorrelacionCambioIncidente,
      detectadaEn: dayjs(rawCorrelacionCambioIncidente.detectadaEn, DATE_TIME_FORMAT),
    };
  }

  private convertCorrelacionCambioIncidenteToCorrelacionCambioIncidenteRawValue(
    correlacionCambioIncidente:
      ICorrelacionCambioIncidente | (Partial<NewCorrelacionCambioIncidente> & CorrelacionCambioIncidenteFormDefaults),
  ): CorrelacionCambioIncidenteFormRawValue | PartialWithRequiredKeyOf<NewCorrelacionCambioIncidenteFormRawValue> {
    return {
      ...correlacionCambioIncidente,
      detectadaEn: correlacionCambioIncidente.detectadaEn ? correlacionCambioIncidente.detectadaEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
