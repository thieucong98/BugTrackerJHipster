import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RdFPathTableComponent } from './list/rd-f-path-table.component';
import { RdFPathTableDetailComponent } from './detail/rd-f-path-table-detail.component';
import { RdFPathTableUpdateComponent } from './update/rd-f-path-table-update.component';
import { RdFPathTableDeleteDialogComponent } from './delete/rd-f-path-table-delete-dialog.component';
import { RdFPathTableRoutingModule } from './route/rd-f-path-table-routing.module';

@NgModule({
  imports: [SharedModule, RdFPathTableRoutingModule],
  declarations: [RdFPathTableComponent, RdFPathTableDetailComponent, RdFPathTableUpdateComponent, RdFPathTableDeleteDialogComponent],
})
export class RdFPathTableModule {}
