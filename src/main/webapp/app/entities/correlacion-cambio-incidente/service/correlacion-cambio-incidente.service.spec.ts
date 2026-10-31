import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../correlacion-cambio-incidente.test-samples';

import { CorrelacionCambioIncidenteService, RestCorrelacionCambioIncidente } from './correlacion-cambio-incidente.service';

const requireRestSample: RestCorrelacionCambioIncidente = {
  ...sampleWithRequiredData,
  detectadaEn: sampleWithRequiredData.detectadaEn?.toJSON(),
};

describe('CorrelacionCambioIncidente Service', () => {
  let service: CorrelacionCambioIncidenteService;
  let httpMock: HttpTestingController;
  let expectedResult: ICorrelacionCambioIncidente | ICorrelacionCambioIncidente[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CorrelacionCambioIncidenteService);
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

    it('should create a CorrelacionCambioIncidente', () => {
      const correlacionCambioIncidente = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(correlacionCambioIncidente).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CorrelacionCambioIncidente', () => {
      const correlacionCambioIncidente = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(correlacionCambioIncidente).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CorrelacionCambioIncidente', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CorrelacionCambioIncidente', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CorrelacionCambioIncidente', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCorrelacionCambioIncidenteToCollectionIfMissing', () => {
      it('should add a CorrelacionCambioIncidente to an empty array', () => {
        const correlacionCambioIncidente: ICorrelacionCambioIncidente = sampleWithRequiredData;
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing([], correlacionCambioIncidente);
        expect(expectedResult).toEqual([correlacionCambioIncidente]);
      });

      it('should not add a CorrelacionCambioIncidente to an array that contains it', () => {
        const correlacionCambioIncidente: ICorrelacionCambioIncidente = sampleWithRequiredData;
        const correlacionCambioIncidenteCollection: ICorrelacionCambioIncidente[] = [
          {
            ...correlacionCambioIncidente,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing(
          correlacionCambioIncidenteCollection,
          correlacionCambioIncidente,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CorrelacionCambioIncidente to an array that doesn't contain it", () => {
        const correlacionCambioIncidente: ICorrelacionCambioIncidente = sampleWithRequiredData;
        const correlacionCambioIncidenteCollection: ICorrelacionCambioIncidente[] = [sampleWithPartialData];
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing(
          correlacionCambioIncidenteCollection,
          correlacionCambioIncidente,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(correlacionCambioIncidente);
      });

      it('should add only unique CorrelacionCambioIncidente to an array', () => {
        const correlacionCambioIncidenteArray: ICorrelacionCambioIncidente[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const correlacionCambioIncidenteCollection: ICorrelacionCambioIncidente[] = [sampleWithRequiredData];
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing(
          correlacionCambioIncidenteCollection,
          ...correlacionCambioIncidenteArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const correlacionCambioIncidente: ICorrelacionCambioIncidente = sampleWithRequiredData;
        const correlacionCambioIncidente2: ICorrelacionCambioIncidente = sampleWithPartialData;
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing(
          [],
          correlacionCambioIncidente,
          correlacionCambioIncidente2,
        );
        expect(expectedResult).toEqual([correlacionCambioIncidente, correlacionCambioIncidente2]);
      });

      it('should accept null and undefined values', () => {
        const correlacionCambioIncidente: ICorrelacionCambioIncidente = sampleWithRequiredData;
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing([], null, correlacionCambioIncidente, undefined);
        expect(expectedResult).toEqual([correlacionCambioIncidente]);
      });

      it('should return initial array if no CorrelacionCambioIncidente is added', () => {
        const correlacionCambioIncidenteCollection: ICorrelacionCambioIncidente[] = [sampleWithRequiredData];
        expectedResult = service.addCorrelacionCambioIncidenteToCollectionIfMissing(correlacionCambioIncidenteCollection, undefined, null);
        expect(expectedResult).toEqual(correlacionCambioIncidenteCollection);
      });
    });

    describe('compareCorrelacionCambioIncidente', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCorrelacionCambioIncidente(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 30799 };
        const entity2 = null;

        const compareResult1 = service.compareCorrelacionCambioIncidente(entity1, entity2);
        const compareResult2 = service.compareCorrelacionCambioIncidente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 30799 };
        const entity2 = { id: 24177 };

        const compareResult1 = service.compareCorrelacionCambioIncidente(entity1, entity2);
        const compareResult2 = service.compareCorrelacionCambioIncidente(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 30799 };
        const entity2 = { id: 30799 };

        const compareResult1 = service.compareCorrelacionCambioIncidente(entity1, entity2);
        const compareResult2 = service.compareCorrelacionCambioIncidente(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
