import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRdFPathTable } from '../rd-f-path-table.model';
import { RdFPathTableService } from '../service/rd-f-path-table.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './rd-f-path-table-delete-dialog.component.html',
})
export class RdFPathTableDeleteDialogComponent {
  rdFPathTable?: IRdFPathTable;

  constructor(protected rdFPathTableService: RdFPathTableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rdFPathTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
