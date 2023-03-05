import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RdFPathTableComponent } from '../list/rd-f-path-table.component';
import { RdFPathTableDetailComponent } from '../detail/rd-f-path-table-detail.component';
import { RdFPathTableUpdateComponent } from '../update/rd-f-path-table-update.component';
import { RdFPathTableRoutingResolveService } from './rd-f-path-table-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const rdFPathTableRoute: Routes = [
  {
    path: '',
    component: RdFPathTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RdFPathTableDetailComponent,
    resolve: {
      rdFPathTable: RdFPathTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RdFPathTableUpdateComponent,
    resolve: {
      rdFPathTable: RdFPathTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RdFPathTableUpdateComponent,
    resolve: {
      rdFPathTable: RdFPathTableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rdFPathTableRoute)],
  exports: [RouterModule],
})
export class RdFPathTableRoutingModule {}
