import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemoOrm } from '../demo-orm.model';
import { DemoOrmService } from '../service/demo-orm.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './demo-orm-delete-dialog.component.html',
})
export class DemoOrmDeleteDialogComponent {
  demoOrm?: IDemoOrm;

  constructor(protected demoOrmService: DemoOrmService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demoOrmService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
