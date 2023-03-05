import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'demo-orm',
        data: { pageTitle: 'bugTrackerJHipsterApp.demoOrm.home.title' },
        loadChildren: () => import('./demo-orm/demo-orm.module').then(m => m.DemoOrmModule),
      },
      {
        path: 'rd-f-batch-register',
        data: { pageTitle: 'bugTrackerJHipsterApp.rdFBatchRegister.home.title' },
        loadChildren: () => import('./rd-f-batch-register/rd-f-batch-register.module').then(m => m.RdFBatchRegisterModule),
      },
      {
        path: 'rd-f-path-table',
        data: { pageTitle: 'bugTrackerJHipsterApp.rdFPathTable.home.title' },
        loadChildren: () => import('./rd-f-path-table/rd-f-path-table.module').then(m => m.RdFPathTableModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
