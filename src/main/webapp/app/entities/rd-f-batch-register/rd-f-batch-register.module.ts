import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RdFBatchRegisterComponent } from './list/rd-f-batch-register.component';
import { RdFBatchRegisterDetailComponent } from './detail/rd-f-batch-register-detail.component';
import { RdFBatchRegisterUpdateComponent } from './update/rd-f-batch-register-update.component';
import { RdFBatchRegisterDeleteDialogComponent } from './delete/rd-f-batch-register-delete-dialog.component';
import { RdFBatchRegisterRoutingModule } from './route/rd-f-batch-register-routing.module';

@NgModule({
  imports: [SharedModule, RdFBatchRegisterRoutingModule],
  declarations: [
    RdFBatchRegisterComponent,
    RdFBatchRegisterDetailComponent,
    RdFBatchRegisterUpdateComponent,
    RdFBatchRegisterDeleteDialogComponent,
  ],
})
export class RdFBatchRegisterModule {}
