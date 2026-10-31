import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDespliegueDeServicio, NewDespliegueDeServicio } from '../despliegue-de-servicio.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDespliegueDeServicio for edit and NewDespliegueDeServicioFormGroupInput for create.
 */
type DespliegueDeServicioFormGroupInput = IDespliegueDeServicio | PartialWithRequiredKeyOf<NewDespliegueDeServicio>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDespliegueDeServicio | NewDespliegueDeServicio> = Omit<T, 'desplegadoEn'> & {
  desplegadoEn?: string | null;
};

type DespliegueDeServicioFormRawValue = FormValueOf<IDespliegueDeServicio>;

type NewDespliegueDeServicioFormRawValue = FormValueOf<NewDespliegueDeServicio>;

type DespliegueDeServicioFormDefaults = Pick<NewDespliegueDeServicio, 'id' | 'desplegadoEn'>;

type DespliegueDeServicioFormGroupContent = {
  id: FormControl<DespliegueDeServicioFormRawValue['id'] | NewDespliegueDeServicio['id']>;
  version: FormControl<DespliegueDeServicioFormRawValue['version']>;
  desplegadoEn: FormControl<DespliegueDeServicioFormRawValue['desplegadoEn']>;
  cambio: FormControl<DespliegueDeServicioFormRawValue['cambio']>;
  servicio: FormControl<DespliegueDeServicioFormRawValue['servicio']>;
};

export type DespliegueDeServicioFormGroup = FormGroup<DespliegueDeServicioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DespliegueDeServicioFormService {
  createDespliegueDeServicioFormGroup(despliegueDeServicio?: DespliegueDeServicioFormGroupInput): DespliegueDeServicioFormGroup {
    const despliegueDeServicioRawValue = this.convertDespliegueDeServicioToDespliegueDeServicioRawValue({
      ...this.getFormDefaults(),
      ...(despliegueDeServicio ?? { id: null }),
    });

    return new FormGroup<DespliegueDeServicioFormGroupContent>({
      id: new FormControl(
        { value: despliegueDeServicioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      version: new FormControl(despliegueDeServicioRawValue.version, {
        validators: [Validators.required, Validators.maxLength(60)],
      }),
      desplegadoEn: new FormControl(despliegueDeServicioRawValue.desplegadoEn, {
        validators: [Validators.required],
      }),
      cambio: new FormControl(despliegueDeServicioRawValue.cambio, {
        validators: [Validators.required],
      }),
      servicio: new FormControl(despliegueDeServicioRawValue.servicio, {
        validators: [Validators.required],
      }),
    });
  }

  getDespliegueDeServicio(form: DespliegueDeServicioFormGroup): IDespliegueDeServicio | NewDespliegueDeServicio {
    return this.convertDespliegueDeServicioRawValueToDespliegueDeServicio(form.getRawValue());
  }

  resetForm(form: DespliegueDeServicioFormGroup, despliegueDeServicio: DespliegueDeServicioFormGroupInput): void {
    const despliegueDeServicioRawValue = this.convertDespliegueDeServicioToDespliegueDeServicioRawValue({
      ...this.getFormDefaults(),
      ...despliegueDeServicio,
    });
    form.reset({
      ...despliegueDeServicioRawValue,
      id: { value: despliegueDeServicioRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): DespliegueDeServicioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      desplegadoEn: currentTime,
    };
  }

  private convertDespliegueDeServicioRawValueToDespliegueDeServicio(
    rawDespliegueDeServicio: DespliegueDeServicioFormRawValue | NewDespliegueDeServicioFormRawValue,
  ): IDespliegueDeServicio | NewDespliegueDeServicio {
    return {
      ...rawDespliegueDeServicio,
      desplegadoEn: dayjs(rawDespliegueDeServicio.desplegadoEn, DATE_TIME_FORMAT),
    };
  }

  private convertDespliegueDeServicioToDespliegueDeServicioRawValue(
    despliegueDeServicio: IDespliegueDeServicio | (Partial<NewDespliegueDeServicio> & DespliegueDeServicioFormDefaults),
  ): DespliegueDeServicioFormRawValue | PartialWithRequiredKeyOf<NewDespliegueDeServicioFormRawValue> {
    return {
      ...despliegueDeServicio,
      desplegadoEn: despliegueDeServicio.desplegadoEn ? despliegueDeServicio.desplegadoEn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
