import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../ventana-de-mantenimiento.test-samples';

import { RestVentanaDeMantenimiento, VentanaDeMantenimientoService } from './ventana-de-mantenimiento.service';

const requireRestSample: RestVentanaDeMantenimiento = {
  ...sampleWithRequiredData,
  desde: sampleWithRequiredData.desde?.toJSON(),
  hasta: sampleWithRequiredData.hasta?.toJSON(),
  repetirHasta: sampleWithRequiredData.repetirHasta?.format(DATE_FORMAT),
};

describe('VentanaDeMantenimiento Service', () => {
  let service: VentanaDeMantenimientoService;
  let httpMock: HttpTestingController;
  let expectedResult: IVentanaDeMantenimiento | IVentanaDeMantenimiento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VentanaDeMantenimientoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a VentanaDeMantenimiento', () => {
      const ventanaDeMantenimiento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ventanaDeMantenimiento).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VentanaDeMantenimiento', () => {
      const ventanaDeMantenimiento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ventanaDeMantenimiento).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VentanaDeMantenimiento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VentanaDeMantenimiento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VentanaDeMantenimiento', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addVentanaDeMantenimientoToCollectionIfMissing', () => {
      it('should add a VentanaDeMantenimiento to an empty array', () => {
        const ventanaDeMantenimiento: IVentanaDeMantenimiento = sampleWithRequiredData;
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing([], ventanaDeMantenimiento);
        expect(expectedResult).toEqual([ventanaDeMantenimiento]);
      });

      it('should not add a VentanaDeMantenimiento to an array that contains it', () => {
        const ventanaDeMantenimiento: IVentanaDeMantenimiento = sampleWithRequiredData;
        const ventanaDeMantenimientoCollection: IVentanaDeMantenimiento[] = [
          {
            ...ventanaDeMantenimiento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing(ventanaDeMantenimientoCollection, ventanaDeMantenimiento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VentanaDeMantenimiento to an array that doesn't contain it", () => {
        const ventanaDeMantenimiento: IVentanaDeMantenimiento = sampleWithRequiredData;
        const ventanaDeMantenimientoCollection: IVentanaDeMantenimiento[] = [sampleWithPartialData];
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing(ventanaDeMantenimientoCollection, ventanaDeMantenimiento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ventanaDeMantenimiento);
      });

      it('should add only unique VentanaDeMantenimiento to an array', () => {
        const ventanaDeMantenimientoArray: IVentanaDeMantenimiento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ventanaDeMantenimientoCollection: IVentanaDeMantenimiento[] = [sampleWithRequiredData];
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing(
          ventanaDeMantenimientoCollection,
          ...ventanaDeMantenimientoArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ventanaDeMantenimiento: IVentanaDeMantenimiento = sampleWithRequiredData;
        const ventanaDeMantenimiento2: IVentanaDeMantenimiento = sampleWithPartialData;
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing([], ventanaDeMantenimiento, ventanaDeMantenimiento2);
        expect(expectedResult).toEqual([ventanaDeMantenimiento, ventanaDeMantenimiento2]);
      });

      it('should accept null and undefined values', () => {
        const ventanaDeMantenimiento: IVentanaDeMantenimiento = sampleWithRequiredData;
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing([], null, ventanaDeMantenimiento, undefined);
        expect(expectedResult).toEqual([ventanaDeMantenimiento]);
      });

      it('should return initial array if no VentanaDeMantenimiento is added', () => {
        const ventanaDeMantenimientoCollection: IVentanaDeMantenimiento[] = [sampleWithRequiredData];
        expectedResult = service.addVentanaDeMantenimientoToCollectionIfMissing(ventanaDeMantenimientoCollection, undefined, null);
        expect(expectedResult).toEqual(ventanaDeMantenimientoCollection);
      });
    });

    describe('compareVentanaDeMantenimiento', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVentanaDeMantenimiento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31419 };
        const entity2 = null;

        const compareResult1 = service.compareVentanaDeMantenimiento(entity1, entity2);
        const compareResult2 = service.compareVentanaDeMantenimiento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31419 };
        const entity2 = { id: 27219 };

        const compareResult1 = service.compareVentanaDeMantenimiento(entity1, entity2);
        const compareResult2 = service.compareVentanaDeMantenimiento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31419 };
        const entity2 = { id: 31419 };

        const compareResult1 = service.compareVentanaDeMantenimiento(entity1, entity2);
        const compareResult2 = service.compareVentanaDeMantenimiento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
