import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRdFPathTable, NewRdFPathTable } from '../rd-f-path-table.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRdFPathTable for edit and NewRdFPathTableFormGroupInput for create.
 */
type RdFPathTableFormGroupInput = IRdFPathTable | PartialWithRequiredKeyOf<NewRdFPathTable>;

type RdFPathTableFormDefaults = Pick<NewRdFPathTable, 'id'>;

type RdFPathTableFormGroupContent = {
  id: FormControl<IRdFPathTable['id'] | NewRdFPathTable['id']>;
  path: FormControl<IRdFPathTable['path']>;
  contentsXslt: FormControl<IRdFPathTable['contentsXslt']>;
  description: FormControl<IRdFPathTable['description']>;
};

export type RdFPathTableFormGroup = FormGroup<RdFPathTableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RdFPathTableFormService {
  createRdFPathTableFormGroup(rdFPathTable: RdFPathTableFormGroupInput = { id: null }): RdFPathTableFormGroup {
    const rdFPathTableRawValue = {
      ...this.getFormDefaults(),
      ...rdFPathTable,
    };
    return new FormGroup<RdFPathTableFormGroupContent>({
      id: new FormControl(
        { value: rdFPathTableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      path: new FormControl(rdFPathTableRawValue.path, {
        validators: [Validators.maxLength(512)],
      }),
      contentsXslt: new FormControl(rdFPathTableRawValue.contentsXslt, {
        validators: [Validators.maxLength(65535)],
      }),
      description: new FormControl(rdFPathTableRawValue.description, {
        validators: [Validators.maxLength(256)],
      }),
    });
  }

  getRdFPathTable(form: RdFPathTableFormGroup): IRdFPathTable | NewRdFPathTable {
    return form.getRawValue() as IRdFPathTable | NewRdFPathTable;
  }

  resetForm(form: RdFPathTableFormGroup, rdFPathTable: RdFPathTableFormGroupInput): void {
    const rdFPathTableRawValue = { ...this.getFormDefaults(), ...rdFPathTable };
    form.reset(
      {
        ...rdFPathTableRawValue,
        id: { value: rdFPathTableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RdFPathTableFormDefaults {
    return {
      id: null,
    };
  }
}
