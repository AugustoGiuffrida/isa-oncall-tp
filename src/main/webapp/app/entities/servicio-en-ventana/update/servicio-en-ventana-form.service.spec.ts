import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../servicio-en-ventana.test-samples';

import { ServicioEnVentanaFormService } from './servicio-en-ventana-form.service';

describe('ServicioEnVentana Form Service', () => {
  let service: ServicioEnVentanaFormService;

  beforeEach(() => {
    service = TestBed.inject(ServicioEnVentanaFormService);
  });

  describe('Service methods', () => {
    describe('createServicioEnVentanaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServicioEnVentanaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nota: expect.any(Object),
            ventana: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });

      it('passing IServicioEnVentana should create a new form with FormGroup', () => {
        const formGroup = service.createServicioEnVentanaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nota: expect.any(Object),
            ventana: expect.any(Object),
            servicio: expect.any(Object),
          }),
        );
      });
    });

    describe('getServicioEnVentana', () => {
      it('should return NewServicioEnVentana for default ServicioEnVentana initial value', () => {
        const formGroup = service.createServicioEnVentanaFormGroup(sampleWithNewData);

        const servicioEnVentana = service.getServicioEnVentana(formGroup);

        expect(servicioEnVentana).toMatchObject(sampleWithNewData);
      });

      it('should return NewServicioEnVentana for empty ServicioEnVentana initial value', () => {
        const formGroup = service.createServicioEnVentanaFormGroup();

        const servicioEnVentana = service.getServicioEnVentana(formGroup);

        expect(servicioEnVentana).toMatchObject({});
      });

      it('should return IServicioEnVentana', () => {
        const formGroup = service.createServicioEnVentanaFormGroup(sampleWithRequiredData);

        const servicioEnVentana = service.getServicioEnVentana(formGroup);

        expect(servicioEnVentana).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServicioEnVentana should not enable id FormControl', () => {
        const formGroup = service.createServicioEnVentanaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServicioEnVentana should disable id FormControl', () => {
        const formGroup = service.createServicioEnVentanaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
