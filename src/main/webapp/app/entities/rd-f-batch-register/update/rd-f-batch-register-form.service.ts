import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRdFBatchRegister, NewRdFBatchRegister } from '../rd-f-batch-register.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRdFBatchRegister for edit and NewRdFBatchRegisterFormGroupInput for create.
 */
type RdFBatchRegisterFormGroupInput = IRdFBatchRegister | PartialWithRequiredKeyOf<NewRdFBatchRegister>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRdFBatchRegister | NewRdFBatchRegister> = Omit<T, 'createdTimestamp' | 'updatedTimestamp'> & {
  createdTimestamp?: string | null;
  updatedTimestamp?: string | null;
};

type RdFBatchRegisterFormRawValue = FormValueOf<IRdFBatchRegister>;

type NewRdFBatchRegisterFormRawValue = FormValueOf<NewRdFBatchRegister>;

type RdFBatchRegisterFormDefaults = Pick<NewRdFBatchRegister, 'id' | 'createdTimestamp' | 'updatedTimestamp'>;

type RdFBatchRegisterFormGroupContent = {
  id: FormControl<RdFBatchRegisterFormRawValue['id'] | NewRdFBatchRegister['id']>;
  workflowId: FormControl<RdFBatchRegisterFormRawValue['workflowId']>;
  dbname: FormControl<RdFBatchRegisterFormRawValue['dbname']>;
  feedId: FormControl<RdFBatchRegisterFormRawValue['feedId']>;
  func: FormControl<RdFBatchRegisterFormRawValue['func']>;
  reqDatetime: FormControl<RdFBatchRegisterFormRawValue['reqDatetime']>;
  execUser: FormControl<RdFBatchRegisterFormRawValue['execUser']>;
  systemIds: FormControl<RdFBatchRegisterFormRawValue['systemIds']>;
  mode: FormControl<RdFBatchRegisterFormRawValue['mode']>;
  done: FormControl<RdFBatchRegisterFormRawValue['done']>;
  createdTimestamp: FormControl<RdFBatchRegisterFormRawValue['createdTimestamp']>;
  updatedTimestamp: FormControl<RdFBatchRegisterFormRawValue['updatedTimestamp']>;
};

export type RdFBatchRegisterFormGroup = FormGroup<RdFBatchRegisterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RdFBatchRegisterFormService {
  createRdFBatchRegisterFormGroup(rdFBatchRegister: RdFBatchRegisterFormGroupInput = { id: null }): RdFBatchRegisterFormGroup {
    const rdFBatchRegisterRawValue = this.convertRdFBatchRegisterToRdFBatchRegisterRawValue({
      ...this.getFormDefaults(),
      ...rdFBatchRegister,
    });
    return new FormGroup<RdFBatchRegisterFormGroupContent>({
      id: new FormControl(
        { value: rdFBatchRegisterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      workflowId: new FormControl(rdFBatchRegisterRawValue.workflowId, {
        validators: [Validators.required, Validators.maxLength(65535)],
      }),
      dbname: new FormControl(rdFBatchRegisterRawValue.dbname, {
        validators: [Validators.required, Validators.maxLength(45)],
      }),
      feedId: new FormControl(rdFBatchRegisterRawValue.feedId, {
        validators: [Validators.required, Validators.maxLength(45)],
      }),
      func: new FormControl(rdFBatchRegisterRawValue.func, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      reqDatetime: new FormControl(rdFBatchRegisterRawValue.reqDatetime, {
        validators: [Validators.required, Validators.maxLength(14)],
      }),
      execUser: new FormControl(rdFBatchRegisterRawValue.execUser, {
        validators: [Validators.required, Validators.maxLength(45)],
      }),
      systemIds: new FormControl(rdFBatchRegisterRawValue.systemIds, {
        validators: [Validators.required],
      }),
      mode: new FormControl(rdFBatchRegisterRawValue.mode, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      done: new FormControl(rdFBatchRegisterRawValue.done, {
        validators: [Validators.required, Validators.maxLength(1)],
      }),
      createdTimestamp: new FormControl(rdFBatchRegisterRawValue.createdTimestamp, {
        validators: [Validators.required],
      }),
      updatedTimestamp: new FormControl(rdFBatchRegisterRawValue.updatedTimestamp, {
        validators: [Validators.required],
      }),
    });
  }

  getRdFBatchRegister(form: RdFBatchRegisterFormGroup): IRdFBatchRegister | NewRdFBatchRegister {
    return this.convertRdFBatchRegisterRawValueToRdFBatchRegister(
      form.getRawValue() as RdFBatchRegisterFormRawValue | NewRdFBatchRegisterFormRawValue
    );
  }

  resetForm(form: RdFBatchRegisterFormGroup, rdFBatchRegister: RdFBatchRegisterFormGroupInput): void {
    const rdFBatchRegisterRawValue = this.convertRdFBatchRegisterToRdFBatchRegisterRawValue({
      ...this.getFormDefaults(),
      ...rdFBatchRegister,
    });
    form.reset(
      {
        ...rdFBatchRegisterRawValue,
        id: { value: rdFBatchRegisterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RdFBatchRegisterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdTimestamp: currentTime,
      updatedTimestamp: currentTime,
    };
  }

  private convertRdFBatchRegisterRawValueToRdFBatchRegister(
    rawRdFBatchRegister: RdFBatchRegisterFormRawValue | NewRdFBatchRegisterFormRawValue
  ): IRdFBatchRegister | NewRdFBatchRegister {
    return {
      ...rawRdFBatchRegister,
      createdTimestamp: dayjs(rawRdFBatchRegister.createdTimestamp, DATE_TIME_FORMAT),
      updatedTimestamp: dayjs(rawRdFBatchRegister.updatedTimestamp, DATE_TIME_FORMAT),
    };
  }

  private convertRdFBatchRegisterToRdFBatchRegisterRawValue(
    rdFBatchRegister: IRdFBatchRegister | (Partial<NewRdFBatchRegister> & RdFBatchRegisterFormDefaults)
  ): RdFBatchRegisterFormRawValue | PartialWithRequiredKeyOf<NewRdFBatchRegisterFormRawValue> {
    return {
      ...rdFBatchRegister,
      createdTimestamp: rdFBatchRegister.createdTimestamp ? rdFBatchRegister.createdTimestamp.format(DATE_TIME_FORMAT) : undefined,
      updatedTimestamp: rdFBatchRegister.updatedTimestamp ? rdFBatchRegister.updatedTimestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
