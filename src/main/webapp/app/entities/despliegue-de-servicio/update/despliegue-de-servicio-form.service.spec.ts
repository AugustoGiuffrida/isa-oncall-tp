import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../despliegue-de-servicio.test-samples';

import { DespliegueDeServicioFormService } from './despliegue-de-servicio-form.service';

describe('DespliegueDeServicio Form Service', () => {
  let service: DespliegueDeServicioFormService;

  beforeEach(() => {
    service = TestBed.inject(DespliegueDeServicioFormService);
  });

  describe('Service methods', () => {
    describe('createDespliegueDeServicioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            version: expect.any(Object),
            desplegadoEn: expect.any(Object),
            cambio: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });

      it('passing IDespliegueDeServicio should create a new form with FormGroup', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            version: expect.any(Object),
            desplegadoEn: expect.any(Object),
            cambio: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });
    });

    describe('getDespliegueDeServicio', () => {
      it('should return NewDespliegueDeServicio for default DespliegueDeServicio initial value', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup(sampleWithNewData);

        const despliegueDeServicio = service.getDespliegueDeServicio(formGroup);

        expect(despliegueDeServicio).toMatchObject(sampleWithNewData);
      });

      it('should return NewDespliegueDeServicio for empty DespliegueDeServicio initial value', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup();

        const despliegueDeServicio = service.getDespliegueDeServicio(formGroup);

        expect(despliegueDeServicio).toMatchObject({});
      });

      it('should return IDespliegueDeServicio', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup(sampleWithRequiredData);

        const despliegueDeServicio = service.getDespliegueDeServicio(formGroup);

        expect(despliegueDeServicio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDespliegueDeServicio should not enable id FormControl', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDespliegueDeServicio should disable id FormControl', () => {
        const formGroup = service.createDespliegueDeServicioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
