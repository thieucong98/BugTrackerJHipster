import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RdFPathTableFormService, RdFPathTableFormGroup } from './rd-f-path-table-form.service';
import { IRdFPathTable } from '../rd-f-path-table.model';
import { RdFPathTableService } from '../service/rd-f-path-table.service';

@Component({
  selector: 'jhi-rd-f-path-table-update',
  templateUrl: './rd-f-path-table-update.component.html',
})
export class RdFPathTableUpdateComponent implements OnInit {
  isSaving = false;
  rdFPathTable: IRdFPathTable | null = null;

  editForm: RdFPathTableFormGroup = this.rdFPathTableFormService.createRdFPathTableFormGroup();

  constructor(
    protected rdFPathTableService: RdFPathTableService,
    protected rdFPathTableFormService: RdFPathTableFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rdFPathTable }) => {
      this.rdFPathTable = rdFPathTable;
      if (rdFPathTable) {
        this.updateForm(rdFPathTable);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rdFPathTable = this.rdFPathTableFormService.getRdFPathTable(this.editForm);
    if (rdFPathTable.id !== null) {
      this.subscribeToSaveResponse(this.rdFPathTableService.update(rdFPathTable));
    } else {
      this.subscribeToSaveResponse(this.rdFPathTableService.create(rdFPathTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRdFPathTable>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(rdFPathTable: IRdFPathTable): void {
    this.rdFPathTable = rdFPathTable;
    this.rdFPathTableFormService.resetForm(this.editForm, rdFPathTable);
  }
}
