import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../correlacion-cambio-incidente.test-samples';

import { CorrelacionCambioIncidenteFormService } from './correlacion-cambio-incidente-form.service';

describe('CorrelacionCambioIncidente Form Service', () => {
  let service: CorrelacionCambioIncidenteFormService;

  beforeEach(() => {
    service = TestBed.inject(CorrelacionCambioIncidenteFormService);
  });

  describe('Service methods', () => {
    describe('createCorrelacionCambioIncidenteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origen: expect.any(Object),
            estado: expect.any(Object),
            detectadaEn: expect.any(Object),
            nota: expect.any(Object),
            incidente: expect.any(Object),
            despliegue: expect.any(Object),
          }),
        );
      });

      it('passing ICorrelacionCambioIncidente should create a new form with FormGroup', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            origen: expect.any(Object),
            estado: expect.any(Object),
            detectadaEn: expect.any(Object),
            nota: expect.any(Object),
            incidente: expect.any(Object),
            despliegue: expect.any(Object),
          }),
        );
      });
    });

    describe('getCorrelacionCambioIncidente', () => {
      it('should return NewCorrelacionCambioIncidente for default CorrelacionCambioIncidente initial value', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup(sampleWithNewData);

        const correlacionCambioIncidente = service.getCorrelacionCambioIncidente(formGroup);

        expect(correlacionCambioIncidente).toMatchObject(sampleWithNewData);
      });

      it('should return NewCorrelacionCambioIncidente for empty CorrelacionCambioIncidente initial value', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup();

        const correlacionCambioIncidente = service.getCorrelacionCambioIncidente(formGroup);

        expect(correlacionCambioIncidente).toMatchObject({});
      });

      it('should return ICorrelacionCambioIncidente', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup(sampleWithRequiredData);

        const correlacionCambioIncidente = service.getCorrelacionCambioIncidente(formGroup);

        expect(correlacionCambioIncidente).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICorrelacionCambioIncidente should not enable id FormControl', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCorrelacionCambioIncidente should disable id FormControl', () => {
        const formGroup = service.createCorrelacionCambioIncidenteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
