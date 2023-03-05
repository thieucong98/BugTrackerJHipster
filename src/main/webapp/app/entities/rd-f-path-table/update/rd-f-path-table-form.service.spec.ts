import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rd-f-path-table.test-samples';

import { RdFPathTableFormService } from './rd-f-path-table-form.service';

describe('RdFPathTable Form Service', () => {
  let service: RdFPathTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RdFPathTableFormService);
  });

  describe('Service methods', () => {
    describe('createRdFPathTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRdFPathTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            path: expect.any(Object),
            contentsXslt: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });

      it('passing IRdFPathTable should create a new form with FormGroup', () => {
        const formGroup = service.createRdFPathTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            path: expect.any(Object),
            contentsXslt: expect.any(Object),
            description: expect.any(Object),
          })
        );
      });
    });

    describe('getRdFPathTable', () => {
      it('should return NewRdFPathTable for default RdFPathTable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRdFPathTableFormGroup(sampleWithNewData);

        const rdFPathTable = service.getRdFPathTable(formGroup) as any;

        expect(rdFPathTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewRdFPathTable for empty RdFPathTable initial value', () => {
        const formGroup = service.createRdFPathTableFormGroup();

        const rdFPathTable = service.getRdFPathTable(formGroup) as any;

        expect(rdFPathTable).toMatchObject({});
      });

      it('should return IRdFPathTable', () => {
        const formGroup = service.createRdFPathTableFormGroup(sampleWithRequiredData);

        const rdFPathTable = service.getRdFPathTable(formGroup) as any;

        expect(rdFPathTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRdFPathTable should not enable id FormControl', () => {
        const formGroup = service.createRdFPathTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRdFPathTable should disable id FormControl', () => {
        const formGroup = service.createRdFPathTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
