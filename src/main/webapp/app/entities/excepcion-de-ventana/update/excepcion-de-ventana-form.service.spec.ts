import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../excepcion-de-ventana.test-samples';

import { ExcepcionDeVentanaFormService } from './excepcion-de-ventana-form.service';

describe('ExcepcionDeVentana Form Service', () => {
  let service: ExcepcionDeVentanaFormService;

  beforeEach(() => {
    service = TestBed.inject(ExcepcionDeVentanaFormService);
  });

  describe('Service methods', () => {
    describe('createExcepcionDeVentanaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            tipo: expect.any(Object),
            nuevoDesde: expect.any(Object),
            nuevoHasta: expect.any(Object),
            motivo: expect.any(Object),
            ventana: expect.any(Object),
          }),
        );
      });

      it('passing IExcepcionDeVentana should create a new form with FormGroup', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            fecha: expect.any(Object),
            tipo: expect.any(Object),
            nuevoDesde: expect.any(Object),
            nuevoHasta: expect.any(Object),
            motivo: expect.any(Object),
            ventana: expect.any(Object),
          }),
        );
      });
    });

    describe('getExcepcionDeVentana', () => {
      it('should return NewExcepcionDeVentana for default ExcepcionDeVentana initial value', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup(sampleWithNewData);

        const excepcionDeVentana = service.getExcepcionDeVentana(formGroup);

        expect(excepcionDeVentana).toMatchObject(sampleWithNewData);
      });

      it('should return NewExcepcionDeVentana for empty ExcepcionDeVentana initial value', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup();

        const excepcionDeVentana = service.getExcepcionDeVentana(formGroup);

        expect(excepcionDeVentana).toMatchObject({});
      });

      it('should return IExcepcionDeVentana', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup(sampleWithRequiredData);

        const excepcionDeVentana = service.getExcepcionDeVentana(formGroup);

        expect(excepcionDeVentana).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExcepcionDeVentana should not enable id FormControl', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExcepcionDeVentana should disable id FormControl', () => {
        const formGroup = service.createExcepcionDeVentanaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
