import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IDespliegueDeServicio } from '../despliegue-de-servicio.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../despliegue-de-servicio.test-samples';

import { DespliegueDeServicioService, RestDespliegueDeServicio } from './despliegue-de-servicio.service';

const requireRestSample: RestDespliegueDeServicio = {
  ...sampleWithRequiredData,
  desplegadoEn: sampleWithRequiredData.desplegadoEn?.toJSON(),
};

describe('DespliegueDeServicio Service', () => {
  let service: DespliegueDeServicioService;
  let httpMock: HttpTestingController;
  let expectedResult: IDespliegueDeServicio | IDespliegueDeServicio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DespliegueDeServicioService);
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

    it('should create a DespliegueDeServicio', () => {
      const despliegueDeServicio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(despliegueDeServicio).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DespliegueDeServicio', () => {
      const despliegueDeServicio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(despliegueDeServicio).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DespliegueDeServicio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DespliegueDeServicio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DespliegueDeServicio', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addDespliegueDeServicioToCollectionIfMissing', () => {
      it('should add a DespliegueDeServicio to an empty array', () => {
        const despliegueDeServicio: IDespliegueDeServicio = sampleWithRequiredData;
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing([], despliegueDeServicio);
        expect(expectedResult).toEqual([despliegueDeServicio]);
      });

      it('should not add a DespliegueDeServicio to an array that contains it', () => {
        const despliegueDeServicio: IDespliegueDeServicio = sampleWithRequiredData;
        const despliegueDeServicioCollection: IDespliegueDeServicio[] = [
          {
            ...despliegueDeServicio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing(despliegueDeServicioCollection, despliegueDeServicio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DespliegueDeServicio to an array that doesn't contain it", () => {
        const despliegueDeServicio: IDespliegueDeServicio = sampleWithRequiredData;
        const despliegueDeServicioCollection: IDespliegueDeServicio[] = [sampleWithPartialData];
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing(despliegueDeServicioCollection, despliegueDeServicio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(despliegueDeServicio);
      });

      it('should add only unique DespliegueDeServicio to an array', () => {
        const despliegueDeServicioArray: IDespliegueDeServicio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const despliegueDeServicioCollection: IDespliegueDeServicio[] = [sampleWithRequiredData];
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing(despliegueDeServicioCollection, ...despliegueDeServicioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const despliegueDeServicio: IDespliegueDeServicio = sampleWithRequiredData;
        const despliegueDeServicio2: IDespliegueDeServicio = sampleWithPartialData;
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing([], despliegueDeServicio, despliegueDeServicio2);
        expect(expectedResult).toEqual([despliegueDeServicio, despliegueDeServicio2]);
      });

      it('should accept null and undefined values', () => {
        const despliegueDeServicio: IDespliegueDeServicio = sampleWithRequiredData;
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing([], null, despliegueDeServicio, undefined);
        expect(expectedResult).toEqual([despliegueDeServicio]);
      });

      it('should return initial array if no DespliegueDeServicio is added', () => {
        const despliegueDeServicioCollection: IDespliegueDeServicio[] = [sampleWithRequiredData];
        expectedResult = service.addDespliegueDeServicioToCollectionIfMissing(despliegueDeServicioCollection, undefined, null);
        expect(expectedResult).toEqual(despliegueDeServicioCollection);
      });
    });

    describe('compareDespliegueDeServicio', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDespliegueDeServicio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6111 };
        const entity2 = null;

        const compareResult1 = service.compareDespliegueDeServicio(entity1, entity2);
        const compareResult2 = service.compareDespliegueDeServicio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6111 };
        const entity2 = { id: 32496 };

        const compareResult1 = service.compareDespliegueDeServicio(entity1, entity2);
        const compareResult2 = service.compareDespliegueDeServicio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6111 };
        const entity2 = { id: 6111 };

        const compareResult1 = service.compareDespliegueDeServicio(entity1, entity2);
        const compareResult2 = service.compareDespliegueDeServicio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
