import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cambio.test-samples';

import { CambioFormService } from './cambio-form.service';

describe('Cambio Form Service', () => {
  let service: CambioFormService;

  beforeEach(() => {
    service = TestBed.inject(CambioFormService);
  });

  describe('Service methods', () => {
    describe('createCambioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCambioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descripcion: expect.any(Object),
            ticketUrl: expect.any(Object),
            creadoEn: expect.any(Object),
            autor: expect.any(Object),
          }),
        );
      });

      it('passing ICambio should create a new form with FormGroup', () => {
        const formGroup = service.createCambioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titulo: expect.any(Object),
            descripcion: expect.any(Object),
            ticketUrl: expect.any(Object),
            creadoEn: expect.any(Object),
            autor: expect.any(Object),
          }),
        );
      });
    });

    describe('getCambio', () => {
      it('should return NewCambio for default Cambio initial value', () => {
        const formGroup = service.createCambioFormGroup(sampleWithNewData);

        const cambio = service.getCambio(formGroup);

        expect(cambio).toMatchObject(sampleWithNewData);
      });

      it('should return NewCambio for empty Cambio initial value', () => {
        const formGroup = service.createCambioFormGroup();

        const cambio = service.getCambio(formGroup);

        expect(cambio).toMatchObject({});
      });

      it('should return ICambio', () => {
        const formGroup = service.createCambioFormGroup(sampleWithRequiredData);

        const cambio = service.getCambio(formGroup);

        expect(cambio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICambio should not enable id FormControl', () => {
        const formGroup = service.createCambioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCambio should disable id FormControl', () => {
        const formGroup = service.createCambioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
