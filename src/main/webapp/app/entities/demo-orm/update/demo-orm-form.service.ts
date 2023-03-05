import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDemoOrm, NewDemoOrm } from '../demo-orm.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDemoOrm for edit and NewDemoOrmFormGroupInput for create.
 */
type DemoOrmFormGroupInput = IDemoOrm | PartialWithRequiredKeyOf<NewDemoOrm>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDemoOrm | NewDemoOrm> = Omit<T, 'createdTimestamp' | 'updatedTimestamp'> & {
  createdTimestamp?: string | null;
  updatedTimestamp?: string | null;
};

type DemoOrmFormRawValue = FormValueOf<IDemoOrm>;

type NewDemoOrmFormRawValue = FormValueOf<NewDemoOrm>;

type DemoOrmFormDefaults = Pick<NewDemoOrm, 'id' | 'createdTimestamp' | 'updatedTimestamp'>;

type DemoOrmFormGroupContent = {
  id: FormControl<DemoOrmFormRawValue['id'] | NewDemoOrm['id']>;
  codeId: FormControl<DemoOrmFormRawValue['codeId']>;
  codeName: FormControl<DemoOrmFormRawValue['codeName']>;
  itemKey: FormControl<DemoOrmFormRawValue['itemKey']>;
  itemValueJa: FormControl<DemoOrmFormRawValue['itemValueJa']>;
  itemValueEn: FormControl<DemoOrmFormRawValue['itemValueEn']>;
  itemValuePair: FormControl<DemoOrmFormRawValue['itemValuePair']>;
  parentCodeId: FormControl<DemoOrmFormRawValue['parentCodeId']>;
  parentItemKey: FormControl<DemoOrmFormRawValue['parentItemKey']>;
  parentItemKeyBackup: FormControl<DemoOrmFormRawValue['parentItemKeyBackup']>;
  parentItemKeyNew: FormControl<DemoOrmFormRawValue['parentItemKeyNew']>;
  createdTimestamp: FormControl<DemoOrmFormRawValue['createdTimestamp']>;
  updatedTimestamp: FormControl<DemoOrmFormRawValue['updatedTimestamp']>;
};

export type DemoOrmFormGroup = FormGroup<DemoOrmFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DemoOrmFormService {
  createDemoOrmFormGroup(demoOrm: DemoOrmFormGroupInput = { id: null }): DemoOrmFormGroup {
    const demoOrmRawValue = this.convertDemoOrmToDemoOrmRawValue({
      ...this.getFormDefaults(),
      ...demoOrm,
    });
    return new FormGroup<DemoOrmFormGroupContent>({
      id: new FormControl(
        { value: demoOrmRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeId: new FormControl(demoOrmRawValue.codeId, {
        validators: [Validators.required, Validators.maxLength(10)],
      }),
      codeName: new FormControl(demoOrmRawValue.codeName, {
        validators: [Validators.required, Validators.maxLength(128)],
      }),
      itemKey: new FormControl(demoOrmRawValue.itemKey, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      itemValueJa: new FormControl(demoOrmRawValue.itemValueJa, {
        validators: [Validators.maxLength(256)],
      }),
      itemValueEn: new FormControl(demoOrmRawValue.itemValueEn, {
        validators: [Validators.maxLength(256)],
      }),
      itemValuePair: new FormControl(demoOrmRawValue.itemValuePair, {
        validators: [Validators.maxLength(255)],
      }),
      parentCodeId: new FormControl(demoOrmRawValue.parentCodeId, {
        validators: [Validators.maxLength(5)],
      }),
      parentItemKey: new FormControl(demoOrmRawValue.parentItemKey, {
        validators: [Validators.maxLength(128)],
      }),
      parentItemKeyBackup: new FormControl(demoOrmRawValue.parentItemKeyBackup, {
        validators: [Validators.maxLength(45)],
      }),
      parentItemKeyNew: new FormControl(demoOrmRawValue.parentItemKeyNew, {
        validators: [Validators.maxLength(45)],
      }),
      createdTimestamp: new FormControl(demoOrmRawValue.createdTimestamp),
      updatedTimestamp: new FormControl(demoOrmRawValue.updatedTimestamp),
    });
  }

  getDemoOrm(form: DemoOrmFormGroup): IDemoOrm | NewDemoOrm {
    return this.convertDemoOrmRawValueToDemoOrm(form.getRawValue() as DemoOrmFormRawValue | NewDemoOrmFormRawValue);
  }

  resetForm(form: DemoOrmFormGroup, demoOrm: DemoOrmFormGroupInput): void {
    const demoOrmRawValue = this.convertDemoOrmToDemoOrmRawValue({ ...this.getFormDefaults(), ...demoOrm });
    form.reset(
      {
        ...demoOrmRawValue,
        id: { value: demoOrmRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DemoOrmFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdTimestamp: currentTime,
      updatedTimestamp: currentTime,
    };
  }

  private convertDemoOrmRawValueToDemoOrm(rawDemoOrm: DemoOrmFormRawValue | NewDemoOrmFormRawValue): IDemoOrm | NewDemoOrm {
    return {
      ...rawDemoOrm,
      createdTimestamp: dayjs(rawDemoOrm.createdTimestamp, DATE_TIME_FORMAT),
      updatedTimestamp: dayjs(rawDemoOrm.updatedTimestamp, DATE_TIME_FORMAT),
    };
  }

  private convertDemoOrmToDemoOrmRawValue(
    demoOrm: IDemoOrm | (Partial<NewDemoOrm> & DemoOrmFormDefaults)
  ): DemoOrmFormRawValue | PartialWithRequiredKeyOf<NewDemoOrmFormRawValue> {
    return {
      ...demoOrm,
      createdTimestamp: demoOrm.createdTimestamp ? demoOrm.createdTimestamp.format(DATE_TIME_FORMAT) : undefined,
      updatedTimestamp: demoOrm.updatedTimestamp ? demoOrm.updatedTimestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
