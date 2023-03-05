import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemoOrm } from '../demo-orm.model';
import { DemoOrmService } from '../service/demo-orm.service';

@Injectable({ providedIn: 'root' })
export class DemoOrmRoutingResolveService implements Resolve<IDemoOrm | null> {
  constructor(protected service: DemoOrmService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemoOrm | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demoOrm: HttpResponse<IDemoOrm>) => {
          if (demoOrm.body) {
            return of(demoOrm.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
