import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRdFBatchRegister, NewRdFBatchRegister } from '../rd-f-batch-register.model';

export type PartialUpdateRdFBatchRegister = Partial<IRdFBatchRegister> & Pick<IRdFBatchRegister, 'id'>;

type RestOf<T extends IRdFBatchRegister | NewRdFBatchRegister> = Omit<T, 'createdTimestamp' | 'updatedTimestamp'> & {
  createdTimestamp?: string | null;
  updatedTimestamp?: string | null;
};

export type RestRdFBatchRegister = RestOf<IRdFBatchRegister>;

export type NewRestRdFBatchRegister = RestOf<NewRdFBatchRegister>;

export type PartialUpdateRestRdFBatchRegister = RestOf<PartialUpdateRdFBatchRegister>;

export type EntityResponseType = HttpResponse<IRdFBatchRegister>;
export type EntityArrayResponseType = HttpResponse<IRdFBatchRegister[]>;

@Injectable({ providedIn: 'root' })
export class RdFBatchRegisterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rd-f-batch-registers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rd-f-batch-registers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rdFBatchRegister: NewRdFBatchRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rdFBatchRegister);
    return this.http
      .post<RestRdFBatchRegister>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rdFBatchRegister: IRdFBatchRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rdFBatchRegister);
    return this.http
      .put<RestRdFBatchRegister>(`${this.resourceUrl}/${this.getRdFBatchRegisterIdentifier(rdFBatchRegister)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rdFBatchRegister: PartialUpdateRdFBatchRegister): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rdFBatchRegister);
    return this.http
      .patch<RestRdFBatchRegister>(`${this.resourceUrl}/${this.getRdFBatchRegisterIdentifier(rdFBatchRegister)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRdFBatchRegister>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRdFBatchRegister[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRdFBatchRegister[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getRdFBatchRegisterIdentifier(rdFBatchRegister: Pick<IRdFBatchRegister, 'id'>): number {
    return rdFBatchRegister.id;
  }

  compareRdFBatchRegister(o1: Pick<IRdFBatchRegister, 'id'> | null, o2: Pick<IRdFBatchRegister, 'id'> | null): boolean {
    return o1 && o2 ? this.getRdFBatchRegisterIdentifier(o1) === this.getRdFBatchRegisterIdentifier(o2) : o1 === o2;
  }

  addRdFBatchRegisterToCollectionIfMissing<Type extends Pick<IRdFBatchRegister, 'id'>>(
    rdFBatchRegisterCollection: Type[],
    ...rdFBatchRegistersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rdFBatchRegisters: Type[] = rdFBatchRegistersToCheck.filter(isPresent);
    if (rdFBatchRegisters.length > 0) {
      const rdFBatchRegisterCollectionIdentifiers = rdFBatchRegisterCollection.map(
        rdFBatchRegisterItem => this.getRdFBatchRegisterIdentifier(rdFBatchRegisterItem)!
      );
      const rdFBatchRegistersToAdd = rdFBatchRegisters.filter(rdFBatchRegisterItem => {
        const rdFBatchRegisterIdentifier = this.getRdFBatchRegisterIdentifier(rdFBatchRegisterItem);
        if (rdFBatchRegisterCollectionIdentifiers.includes(rdFBatchRegisterIdentifier)) {
          return false;
        }
        rdFBatchRegisterCollectionIdentifiers.push(rdFBatchRegisterIdentifier);
        return true;
      });
      return [...rdFBatchRegistersToAdd, ...rdFBatchRegisterCollection];
    }
    return rdFBatchRegisterCollection;
  }

  protected convertDateFromClient<T extends IRdFBatchRegister | NewRdFBatchRegister | PartialUpdateRdFBatchRegister>(
    rdFBatchRegister: T
  ): RestOf<T> {
    return {
      ...rdFBatchRegister,
      createdTimestamp: rdFBatchRegister.createdTimestamp?.toJSON() ?? null,
      updatedTimestamp: rdFBatchRegister.updatedTimestamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRdFBatchRegister: RestRdFBatchRegister): IRdFBatchRegister {
    return {
      ...restRdFBatchRegister,
      createdTimestamp: restRdFBatchRegister.createdTimestamp ? dayjs(restRdFBatchRegister.createdTimestamp) : undefined,
      updatedTimestamp: restRdFBatchRegister.updatedTimestamp ? dayjs(restRdFBatchRegister.updatedTimestamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRdFBatchRegister>): HttpResponse<IRdFBatchRegister> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRdFBatchRegister[]>): HttpResponse<IRdFBatchRegister[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
