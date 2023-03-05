import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../demo-orm.test-samples';

import { DemoOrmFormService } from './demo-orm-form.service';

describe('DemoOrm Form Service', () => {
  let service: DemoOrmFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DemoOrmFormService);
  });

  describe('Service methods', () => {
    describe('createDemoOrmFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDemoOrmFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeId: expect.any(Object),
            codeName: expect.any(Object),
            itemKey: expect.any(Object),
            itemValueJa: expect.any(Object),
            itemValueEn: expect.any(Object),
            itemValuePair: expect.any(Object),
            parentCodeId: expect.any(Object),
            parentItemKey: expect.any(Object),
            parentItemKeyBackup: expect.any(Object),
            parentItemKeyNew: expect.any(Object),
            createdTimestamp: expect.any(Object),
            updatedTimestamp: expect.any(Object),
          })
        );
      });

      it('passing IDemoOrm should create a new form with FormGroup', () => {
        const formGroup = service.createDemoOrmFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeId: expect.any(Object),
            codeName: expect.any(Object),
            itemKey: expect.any(Object),
            itemValueJa: expect.any(Object),
            itemValueEn: expect.any(Object),
            itemValuePair: expect.any(Object),
            parentCodeId: expect.any(Object),
            parentItemKey: expect.any(Object),
            parentItemKeyBackup: expect.any(Object),
            parentItemKeyNew: expect.any(Object),
            createdTimestamp: expect.any(Object),
            updatedTimestamp: expect.any(Object),
          })
        );
      });
    });

    describe('getDemoOrm', () => {
      it('should return NewDemoOrm for default DemoOrm initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDemoOrmFormGroup(sampleWithNewData);

        const demoOrm = service.getDemoOrm(formGroup) as any;

        expect(demoOrm).toMatchObject(sampleWithNewData);
      });

      it('should return NewDemoOrm for empty DemoOrm initial value', () => {
        const formGroup = service.createDemoOrmFormGroup();

        const demoOrm = service.getDemoOrm(formGroup) as any;

        expect(demoOrm).toMatchObject({});
      });

      it('should return IDemoOrm', () => {
        const formGroup = service.createDemoOrmFormGroup(sampleWithRequiredData);

        const demoOrm = service.getDemoOrm(formGroup) as any;

        expect(demoOrm).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDemoOrm should not enable id FormControl', () => {
        const formGroup = service.createDemoOrmFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDemoOrm should disable id FormControl', () => {
        const formGroup = service.createDemoOrmFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
