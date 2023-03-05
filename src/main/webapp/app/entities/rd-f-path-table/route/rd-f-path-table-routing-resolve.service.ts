import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRdFPathTable } from '../rd-f-path-table.model';
import { RdFPathTableService } from '../service/rd-f-path-table.service';

@Injectable({ providedIn: 'root' })
export class RdFPathTableRoutingResolveService implements Resolve<IRdFPathTable | null> {
  constructor(protected service: RdFPathTableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRdFPathTable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rdFPathTable: HttpResponse<IRdFPathTable>) => {
          if (rdFPathTable.body) {
            return of(rdFPathTable.body);
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
