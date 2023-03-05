import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DemoOrmFormService, DemoOrmFormGroup } from './demo-orm-form.service';
import { IDemoOrm } from '../demo-orm.model';
import { DemoOrmService } from '../service/demo-orm.service';

@Component({
  selector: 'jhi-demo-orm-update',
  templateUrl: './demo-orm-update.component.html',
})
export class DemoOrmUpdateComponent implements OnInit {
  isSaving = false;
  demoOrm: IDemoOrm | null = null;

  editForm: DemoOrmFormGroup = this.demoOrmFormService.createDemoOrmFormGroup();

  constructor(
    protected demoOrmService: DemoOrmService,
    protected demoOrmFormService: DemoOrmFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demoOrm }) => {
      this.demoOrm = demoOrm;
      if (demoOrm) {
        this.updateForm(demoOrm);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demoOrm = this.demoOrmFormService.getDemoOrm(this.editForm);
    if (demoOrm.id !== null) {
      this.subscribeToSaveResponse(this.demoOrmService.update(demoOrm));
    } else {
      this.subscribeToSaveResponse(this.demoOrmService.create(demoOrm));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemoOrm>>): void {
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

  protected updateForm(demoOrm: IDemoOrm): void {
    this.demoOrm = demoOrm;
    this.demoOrmFormService.resetForm(this.editForm, demoOrm);
  }
}
