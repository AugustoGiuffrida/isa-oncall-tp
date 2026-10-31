import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICambio } from '../cambio.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../cambio.test-samples';

import { CambioService, RestCambio } from './cambio.service';

const requireRestSample: RestCambio = {
  ...sampleWithRequiredData,
  creadoEn: sampleWithRequiredData.creadoEn?.toJSON(),
};

describe('Cambio Service', () => {
  let service: CambioService;
  let httpMock: HttpTestingController;
  let expectedResult: ICambio | ICambio[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CambioService);
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

    it('should create a Cambio', () => {
      const cambio = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cambio).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cambio', () => {
      const cambio = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cambio).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cambio', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cambio', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Cambio', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCambioToCollectionIfMissing', () => {
      it('should add a Cambio to an empty array', () => {
        const cambio: ICambio = sampleWithRequiredData;
        expectedResult = service.addCambioToCollectionIfMissing([], cambio);
        expect(expectedResult).toEqual([cambio]);
      });

      it('should not add a Cambio to an array that contains it', () => {
        const cambio: ICambio = sampleWithRequiredData;
        const cambioCollection: ICambio[] = [
          {
            ...cambio,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCambioToCollectionIfMissing(cambioCollection, cambio);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cambio to an array that doesn't contain it", () => {
        const cambio: ICambio = sampleWithRequiredData;
        const cambioCollection: ICambio[] = [sampleWithPartialData];
        expectedResult = service.addCambioToCollectionIfMissing(cambioCollection, cambio);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cambio);
      });

      it('should add only unique Cambio to an array', () => {
        const cambioArray: ICambio[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cambioCollection: ICambio[] = [sampleWithRequiredData];
        expectedResult = service.addCambioToCollectionIfMissing(cambioCollection, ...cambioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cambio: ICambio = sampleWithRequiredData;
        const cambio2: ICambio = sampleWithPartialData;
        expectedResult = service.addCambioToCollectionIfMissing([], cambio, cambio2);
        expect(expectedResult).toEqual([cambio, cambio2]);
      });

      it('should accept null and undefined values', () => {
        const cambio: ICambio = sampleWithRequiredData;
        expectedResult = service.addCambioToCollectionIfMissing([], null, cambio, undefined);
        expect(expectedResult).toEqual([cambio]);
      });

      it('should return initial array if no Cambio is added', () => {
        const cambioCollection: ICambio[] = [sampleWithRequiredData];
        expectedResult = service.addCambioToCollectionIfMissing(cambioCollection, undefined, null);
        expect(expectedResult).toEqual(cambioCollection);
      });
    });

    describe('compareCambio', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCambio(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11471 };
        const entity2 = null;

        const compareResult1 = service.compareCambio(entity1, entity2);
        const compareResult2 = service.compareCambio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11471 };
        const entity2 = { id: 30774 };

        const compareResult1 = service.compareCambio(entity1, entity2);
        const compareResult2 = service.compareCambio(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11471 };
        const entity2 = { id: 11471 };

        const compareResult1 = service.compareCambio(entity1, entity2);
        const compareResult2 = service.compareCambio(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
