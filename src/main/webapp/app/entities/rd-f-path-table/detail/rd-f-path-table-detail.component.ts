import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRdFPathTable } from '../rd-f-path-table.model';

@Component({
  selector: 'jhi-rd-f-path-table-detail',
  templateUrl: './rd-f-path-table-detail.component.html',
})
export class RdFPathTableDetailComponent implements OnInit {
  rdFPathTable: IRdFPathTable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rdFPathTable }) => {
      this.rdFPathTable = rdFPathTable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
