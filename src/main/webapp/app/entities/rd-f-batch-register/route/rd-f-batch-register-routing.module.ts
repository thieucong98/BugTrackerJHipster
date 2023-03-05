import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RdFBatchRegisterComponent } from '../list/rd-f-batch-register.component';
import { RdFBatchRegisterDetailComponent } from '../detail/rd-f-batch-register-detail.component';
import { RdFBatchRegisterUpdateComponent } from '../update/rd-f-batch-register-update.component';
import { RdFBatchRegisterRoutingResolveService } from './rd-f-batch-register-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const rdFBatchRegisterRoute: Routes = [
  {
    path: '',
    component: RdFBatchRegisterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RdFBatchRegisterDetailComponent,
    resolve: {
      rdFBatchRegister: RdFBatchRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RdFBatchRegisterUpdateComponent,
    resolve: {
      rdFBatchRegister: RdFBatchRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RdFBatchRegisterUpdateComponent,
    resolve: {
      rdFBatchRegister: RdFBatchRegisterRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rdFBatchRegisterRoute)],
  exports: [RouterModule],
})
export class RdFBatchRegisterRoutingModule {}
