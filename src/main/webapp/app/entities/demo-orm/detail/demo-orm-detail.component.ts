import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemoOrm } from '../demo-orm.model';

@Component({
  selector: 'jhi-demo-orm-detail',
  templateUrl: './demo-orm-detail.component.html',
})
export class DemoOrmDetailComponent implements OnInit {
  demoOrm: IDemoOrm | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demoOrm }) => {
      this.demoOrm = demoOrm;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
