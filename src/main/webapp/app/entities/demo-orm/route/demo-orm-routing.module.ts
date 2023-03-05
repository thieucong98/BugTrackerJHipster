import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemoOrmComponent } from '../list/demo-orm.component';
import { DemoOrmDetailComponent } from '../detail/demo-orm-detail.component';
import { DemoOrmUpdateComponent } from '../update/demo-orm-update.component';
import { DemoOrmRoutingResolveService } from './demo-orm-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const demoOrmRoute: Routes = [
  {
    path: '',
    component: DemoOrmComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemoOrmDetailComponent,
    resolve: {
      demoOrm: DemoOrmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemoOrmUpdateComponent,
    resolve: {
      demoOrm: DemoOrmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemoOrmUpdateComponent,
    resolve: {
      demoOrm: DemoOrmRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demoOrmRoute)],
  exports: [RouterModule],
})
export class DemoOrmRoutingModule {}
