import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rd-f-batch-register.test-samples';

import { RdFBatchRegisterFormService } from './rd-f-batch-register-form.service';

describe('RdFBatchRegister Form Service', () => {
  let service: RdFBatchRegisterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RdFBatchRegisterFormService);
  });

  describe('Service methods', () => {
    describe('createRdFBatchRegisterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRdFBatchRegisterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            workflowId: expect.any(Object),
            dbname: expect.any(Object),
            feedId: expect.any(Object),
            func: expect.any(Object),
            reqDatetime: expect.any(Object),
            execUser: expect.any(Object),
            systemIds: expect.any(Object),
            mode: expect.any(Object),
            done: expect.any(Object),
            createdTimestamp: expect.any(Object),
            updatedTimestamp: expect.any(Object),
          })
        );
      });

      it('passing IRdFBatchRegister should create a new form with FormGroup', () => {
        const formGroup = service.createRdFBatchRegisterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            workflowId: expect.any(Object),
            dbname: expect.any(Object),
            feedId: expect.any(Object),
            func: expect.any(Object),
            reqDatetime: expect.any(Object),
            execUser: expect.any(Object),
            systemIds: expect.any(Object),
            mode: expect.any(Object),
            done: expect.any(Object),
            createdTimestamp: expect.any(Object),
            updatedTimestamp: expect.any(Object),
          })
        );
      });
    });

    describe('getRdFBatchRegister', () => {
      it('should return NewRdFBatchRegister for default RdFBatchRegister initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRdFBatchRegisterFormGroup(sampleWithNewData);

        const rdFBatchRegister = service.getRdFBatchRegister(formGroup) as any;

        expect(rdFBatchRegister).toMatchObject(sampleWithNewData);
      });

      it('should return NewRdFBatchRegister for empty RdFBatchRegister initial value', () => {
        const formGroup = service.createRdFBatchRegisterFormGroup();

        const rdFBatchRegister = service.getRdFBatchRegister(formGroup) as any;

        expect(rdFBatchRegister).toMatchObject({});
      });

      it('should return IRdFBatchRegister', () => {
        const formGroup = service.createRdFBatchRegisterFormGroup(sampleWithRequiredData);

        const rdFBatchRegister = service.getRdFBatchRegister(formGroup) as any;

        expect(rdFBatchRegister).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRdFBatchRegister should not enable id FormControl', () => {
        const formGroup = service.createRdFBatchRegisterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRdFBatchRegister should disable id FormControl', () => {
        const formGroup = service.createRdFBatchRegisterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
