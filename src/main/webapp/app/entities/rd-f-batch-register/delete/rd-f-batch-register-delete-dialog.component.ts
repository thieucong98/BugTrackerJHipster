import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRdFBatchRegister } from '../rd-f-batch-register.model';
import { RdFBatchRegisterService } from '../service/rd-f-batch-register.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './rd-f-batch-register-delete-dialog.component.html',
})
export class RdFBatchRegisterDeleteDialogComponent {
  rdFBatchRegister?: IRdFBatchRegister;

  constructor(protected rdFBatchRegisterService: RdFBatchRegisterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rdFBatchRegisterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
