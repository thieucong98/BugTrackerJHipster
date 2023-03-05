import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemoOrmComponent } from './list/demo-orm.component';
import { DemoOrmDetailComponent } from './detail/demo-orm-detail.component';
import { DemoOrmUpdateComponent } from './update/demo-orm-update.component';
import { DemoOrmDeleteDialogComponent } from './delete/demo-orm-delete-dialog.component';
import { DemoOrmRoutingModule } from './route/demo-orm-routing.module';

@NgModule({
  imports: [SharedModule, DemoOrmRoutingModule],
  declarations: [DemoOrmComponent, DemoOrmDetailComponent, DemoOrmUpdateComponent, DemoOrmDeleteDialogComponent],
})
export class DemoOrmModule {}
