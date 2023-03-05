import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRdFPathTable } from '../rd-f-path-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rd-f-path-table.test-samples';

import { RdFPathTableService } from './rd-f-path-table.service';

const requireRestSample: IRdFPathTable = {
  ...sampleWithRequiredData,
};

describe('RdFPathTable Service', () => {
  let service: RdFPathTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IRdFPathTable | IRdFPathTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RdFPathTableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a RdFPathTable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const rdFPathTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rdFPathTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RdFPathTable', () => {
      const rdFPathTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rdFPathTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RdFPathTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RdFPathTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RdFPathTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRdFPathTableToCollectionIfMissing', () => {
      it('should add a RdFPathTable to an empty array', () => {
        const rdFPathTable: IRdFPathTable = sampleWithRequiredData;
        expectedResult = service.addRdFPathTableToCollectionIfMissing([], rdFPathTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rdFPathTable);
      });

      it('should not add a RdFPathTable to an array that contains it', () => {
        const rdFPathTable: IRdFPathTable = sampleWithRequiredData;
        const rdFPathTableCollection: IRdFPathTable[] = [
          {
            ...rdFPathTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRdFPathTableToCollectionIfMissing(rdFPathTableCollection, rdFPathTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RdFPathTable to an array that doesn't contain it", () => {
        const rdFPathTable: IRdFPathTable = sampleWithRequiredData;
        const rdFPathTableCollection: IRdFPathTable[] = [sampleWithPartialData];
        expectedResult = service.addRdFPathTableToCollectionIfMissing(rdFPathTableCollection, rdFPathTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rdFPathTable);
      });

      it('should add only unique RdFPathTable to an array', () => {
        const rdFPathTableArray: IRdFPathTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rdFPathTableCollection: IRdFPathTable[] = [sampleWithRequiredData];
        expectedResult = service.addRdFPathTableToCollectionIfMissing(rdFPathTableCollection, ...rdFPathTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rdFPathTable: IRdFPathTable = sampleWithRequiredData;
        const rdFPathTable2: IRdFPathTable = sampleWithPartialData;
        expectedResult = service.addRdFPathTableToCollectionIfMissing([], rdFPathTable, rdFPathTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rdFPathTable);
        expect(expectedResult).toContain(rdFPathTable2);
      });

      it('should accept null and undefined values', () => {
        const rdFPathTable: IRdFPathTable = sampleWithRequiredData;
        expectedResult = service.addRdFPathTableToCollectionIfMissing([], null, rdFPathTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rdFPathTable);
      });

      it('should return initial array if no RdFPathTable is added', () => {
        const rdFPathTableCollection: IRdFPathTable[] = [sampleWithRequiredData];
        expectedResult = service.addRdFPathTableToCollectionIfMissing(rdFPathTableCollection, undefined, null);
        expect(expectedResult).toEqual(rdFPathTableCollection);
      });
    });

    describe('compareRdFPathTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRdFPathTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRdFPathTable(entity1, entity2);
        const compareResult2 = service.compareRdFPathTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRdFPathTable(entity1, entity2);
        const compareResult2 = service.compareRdFPathTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRdFPathTable(entity1, entity2);
        const compareResult2 = service.compareRdFPathTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
