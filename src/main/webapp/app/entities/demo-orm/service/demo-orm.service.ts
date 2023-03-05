import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDemoOrm, NewDemoOrm } from '../demo-orm.model';

export type PartialUpdateDemoOrm = Partial<IDemoOrm> & Pick<IDemoOrm, 'id'>;

type RestOf<T extends IDemoOrm | NewDemoOrm> = Omit<T, 'createdTimestamp' | 'updatedTimestamp'> & {
  createdTimestamp?: string | null;
  updatedTimestamp?: string | null;
};

export type RestDemoOrm = RestOf<IDemoOrm>;

export type NewRestDemoOrm = RestOf<NewDemoOrm>;

export type PartialUpdateRestDemoOrm = RestOf<PartialUpdateDemoOrm>;

export type EntityResponseType = HttpResponse<IDemoOrm>;
export type EntityArrayResponseType = HttpResponse<IDemoOrm[]>;

@Injectable({ providedIn: 'root' })
export class DemoOrmService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demo-orms');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/demo-orms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demoOrm: NewDemoOrm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demoOrm);
    return this.http
      .post<RestDemoOrm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(demoOrm: IDemoOrm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demoOrm);
    return this.http
      .put<RestDemoOrm>(`${this.resourceUrl}/${this.getDemoOrmIdentifier(demoOrm)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(demoOrm: PartialUpdateDemoOrm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(demoOrm);
    return this.http
      .patch<RestDemoOrm>(`${this.resourceUrl}/${this.getDemoOrmIdentifier(demoOrm)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDemoOrm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDemoOrm[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDemoOrm[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getDemoOrmIdentifier(demoOrm: Pick<IDemoOrm, 'id'>): number {
    return demoOrm.id;
  }

  compareDemoOrm(o1: Pick<IDemoOrm, 'id'> | null, o2: Pick<IDemoOrm, 'id'> | null): boolean {
    return o1 && o2 ? this.getDemoOrmIdentifier(o1) === this.getDemoOrmIdentifier(o2) : o1 === o2;
  }

  addDemoOrmToCollectionIfMissing<Type extends Pick<IDemoOrm, 'id'>>(
    demoOrmCollection: Type[],
    ...demoOrmsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const demoOrms: Type[] = demoOrmsToCheck.filter(isPresent);
    if (demoOrms.length > 0) {
      const demoOrmCollectionIdentifiers = demoOrmCollection.map(demoOrmItem => this.getDemoOrmIdentifier(demoOrmItem)!);
      const demoOrmsToAdd = demoOrms.filter(demoOrmItem => {
        const demoOrmIdentifier = this.getDemoOrmIdentifier(demoOrmItem);
        if (demoOrmCollectionIdentifiers.includes(demoOrmIdentifier)) {
          return false;
        }
        demoOrmCollectionIdentifiers.push(demoOrmIdentifier);
        return true;
      });
      return [...demoOrmsToAdd, ...demoOrmCollection];
    }
    return demoOrmCollection;
  }

  protected convertDateFromClient<T extends IDemoOrm | NewDemoOrm | PartialUpdateDemoOrm>(demoOrm: T): RestOf<T> {
    return {
      ...demoOrm,
      createdTimestamp: demoOrm.createdTimestamp?.toJSON() ?? null,
      updatedTimestamp: demoOrm.updatedTimestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDemoOrm: RestDemoOrm): IDemoOrm {
    return {
      ...restDemoOrm,
      createdTimestamp: restDemoOrm.createdTimestamp ? dayjs(restDemoOrm.createdTimestamp) : undefined,
      updatedTimestamp: restDemoOrm.updatedTimestamp ? dayjs(restDemoOrm.updatedTimestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDemoOrm>): HttpResponse<IDemoOrm> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDemoOrm[]>): HttpResponse<IDemoOrm[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
