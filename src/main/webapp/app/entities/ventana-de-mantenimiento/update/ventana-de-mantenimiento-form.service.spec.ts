import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../ventana-de-mantenimiento.test-samples';

import { VentanaDeMantenimientoFormService } from './ventana-de-mantenimiento-form.service';

describe('VentanaDeMantenimiento Form Service', () => {
  let service: VentanaDeMantenimientoFormService;

  beforeEach(() => {
    service = TestBed.inject(VentanaDeMantenimientoFormService);
  });

  describe('Service methods', () => {
    describe('createVentanaDeMantenimientoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            desde: expect.any(Object),
            hasta: expect.any(Object),
            recurrencia: expect.any(Object),
            zonaHoraria: expect.any(Object),
            repetirHasta: expect.any(Object),
            activa: expect.any(Object),
          }),
        );
      });

      it('passing IVentanaDeMantenimiento should create a new form with FormGroup', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            desde: expect.any(Object),
            hasta: expect.any(Object),
            recurrencia: expect.any(Object),
            zonaHoraria: expect.any(Object),
            repetirHasta: expect.any(Object),
            activa: expect.any(Object),
          }),
        );
      });
    });

    describe('getVentanaDeMantenimiento', () => {
      it('should return NewVentanaDeMantenimiento for default VentanaDeMantenimiento initial value', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup(sampleWithNewData);

        const ventanaDeMantenimiento = service.getVentanaDeMantenimiento(formGroup);

        expect(ventanaDeMantenimiento).toMatchObject(sampleWithNewData);
      });

      it('should return NewVentanaDeMantenimiento for empty VentanaDeMantenimiento initial value', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup();

        const ventanaDeMantenimiento = service.getVentanaDeMantenimiento(formGroup);

        expect(ventanaDeMantenimiento).toMatchObject({});
      });

      it('should return IVentanaDeMantenimiento', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup(sampleWithRequiredData);

        const ventanaDeMantenimiento = service.getVentanaDeMantenimiento(formGroup);

        expect(ventanaDeMantenimiento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVentanaDeMantenimiento should not enable id FormControl', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVentanaDeMantenimiento should disable id FormControl', () => {
        const formGroup = service.createVentanaDeMantenimientoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
