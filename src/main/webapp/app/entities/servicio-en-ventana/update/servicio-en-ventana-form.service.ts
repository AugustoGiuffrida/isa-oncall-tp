import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IServicioEnVentana, NewServicioEnVentana } from '../servicio-en-ventana.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServicioEnVentana for edit and NewServicioEnVentanaFormGroupInput for create.
 */
type ServicioEnVentanaFormGroupInput = IServicioEnVentana | PartialWithRequiredKeyOf<NewServicioEnVentana>;

type ServicioEnVentanaFormDefaults = Pick<NewServicioEnVentana, 'id'>;

type ServicioEnVentanaFormGroupContent = {
  id: FormControl<IServicioEnVentana['id'] | NewServicioEnVentana['id']>;
  nota: FormControl<IServicioEnVentana['nota']>;
  ventana: FormControl<IServicioEnVentana['ventana']>;
  servicio: FormControl<IServicioEnVentana['servicio']>;
};

export type ServicioEnVentanaFormGroup = FormGroup<ServicioEnVentanaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServicioEnVentanaFormService {
  createServicioEnVentanaFormGroup(servicioEnVentana?: ServicioEnVentanaFormGroupInput): ServicioEnVentanaFormGroup {
    const servicioEnVentanaRawValue = {
      ...this.getFormDefaults(),
      ...(servicioEnVentana ?? { id: null }),
    };

    return new FormGroup<ServicioEnVentanaFormGroupContent>({
      id: new FormControl(
        { value: servicioEnVentanaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nota: new FormControl(servicioEnVentanaRawValue.nota, {
        validators: [Validators.maxLength(255)],
      }),
      ventana: new FormControl(servicioEnVentanaRawValue.ventana, {
        validators: [Validators.required],
      }),
      servicio: new FormControl(servicioEnVentanaRawValue.servicio, {
        validators: [Validators.required],
      }),
    });
  }

  getServicioEnVentana(form: ServicioEnVentanaFormGroup): IServicioEnVentana | NewServicioEnVentana {
    return form.getRawValue();
  }

  resetForm(form: ServicioEnVentanaFormGroup, servicioEnVentana: ServicioEnVentanaFormGroupInput): void {
    const servicioEnVentanaRawValue = { ...this.getFormDefaults(), ...servicioEnVentana };
    form.reset({
      ...servicioEnVentanaRawValue,
      id: { value: servicioEnVentanaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServicioEnVentanaFormDefaults {
    return {
      id: null,
    };
  }
}
