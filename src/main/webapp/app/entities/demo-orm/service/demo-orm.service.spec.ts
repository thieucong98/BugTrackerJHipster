import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDemoOrm } from '../demo-orm.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../demo-orm.test-samples';

import { DemoOrmService, RestDemoOrm } from './demo-orm.service';

const requireRestSample: RestDemoOrm = {
  ...sampleWithRequiredData,
  createdTimestamp: sampleWithRequiredData.createdTimestamp?.toJSON(),
  updatedTimestamp: sampleWithRequiredData.updatedTimestamp?.toJSON(),
};

describe('DemoOrm Service', () => {
  let service: DemoOrmService;
  let httpMock: HttpTestingController;
  let expectedResult: IDemoOrm | IDemoOrm[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemoOrmService);
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

    it('should create a DemoOrm', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const demoOrm = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(demoOrm).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemoOrm', () => {
      const demoOrm = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(demoOrm).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DemoOrm', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DemoOrm', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DemoOrm', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDemoOrmToCollectionIfMissing', () => {
      it('should add a DemoOrm to an empty array', () => {
        const demoOrm: IDemoOrm = sampleWithRequiredData;
        expectedResult = service.addDemoOrmToCollectionIfMissing([], demoOrm);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demoOrm);
      });

      it('should not add a DemoOrm to an array that contains it', () => {
        const demoOrm: IDemoOrm = sampleWithRequiredData;
        const demoOrmCollection: IDemoOrm[] = [
          {
            ...demoOrm,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDemoOrmToCollectionIfMissing(demoOrmCollection, demoOrm);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemoOrm to an array that doesn't contain it", () => {
        const demoOrm: IDemoOrm = sampleWithRequiredData;
        const demoOrmCollection: IDemoOrm[] = [sampleWithPartialData];
        expectedResult = service.addDemoOrmToCollectionIfMissing(demoOrmCollection, demoOrm);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demoOrm);
      });

      it('should add only unique DemoOrm to an array', () => {
        const demoOrmArray: IDemoOrm[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const demoOrmCollection: IDemoOrm[] = [sampleWithRequiredData];
        expectedResult = service.addDemoOrmToCollectionIfMissing(demoOrmCollection, ...demoOrmArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demoOrm: IDemoOrm = sampleWithRequiredData;
        const demoOrm2: IDemoOrm = sampleWithPartialData;
        expectedResult = service.addDemoOrmToCollectionIfMissing([], demoOrm, demoOrm2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demoOrm);
        expect(expectedResult).toContain(demoOrm2);
      });

      it('should accept null and undefined values', () => {
        const demoOrm: IDemoOrm = sampleWithRequiredData;
        expectedResult = service.addDemoOrmToCollectionIfMissing([], null, demoOrm, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demoOrm);
      });

      it('should return initial array if no DemoOrm is added', () => {
        const demoOrmCollection: IDemoOrm[] = [sampleWithRequiredData];
        expectedResult = service.addDemoOrmToCollectionIfMissing(demoOrmCollection, undefined, null);
        expect(expectedResult).toEqual(demoOrmCollection);
      });
    });

    describe('compareDemoOrm', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDemoOrm(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDemoOrm(entity1, entity2);
        const compareResult2 = service.compareDemoOrm(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDemoOrm(entity1, entity2);
        const compareResult2 = service.compareDemoOrm(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDemoOrm(entity1, entity2);
        const compareResult2 = service.compareDemoOrm(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
