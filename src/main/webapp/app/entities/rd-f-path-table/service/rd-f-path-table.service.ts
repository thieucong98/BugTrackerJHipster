import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRdFPathTable, NewRdFPathTable } from '../rd-f-path-table.model';

export type PartialUpdateRdFPathTable = Partial<IRdFPathTable> & Pick<IRdFPathTable, 'id'>;

export type EntityResponseType = HttpResponse<IRdFPathTable>;
export type EntityArrayResponseType = HttpResponse<IRdFPathTable[]>;

@Injectable({ providedIn: 'root' })
export class RdFPathTableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rd-f-path-tables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rd-f-path-tables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rdFPathTable: NewRdFPathTable): Observable<EntityResponseType> {
    return this.http.post<IRdFPathTable>(this.resourceUrl, rdFPathTable, { observe: 'response' });
  }

  update(rdFPathTable: IRdFPathTable): Observable<EntityResponseType> {
    return this.http.put<IRdFPathTable>(`${this.resourceUrl}/${this.getRdFPathTableIdentifier(rdFPathTable)}`, rdFPathTable, {
      observe: 'response',
    });
  }

  partialUpdate(rdFPathTable: PartialUpdateRdFPathTable): Observable<EntityResponseType> {
    return this.http.patch<IRdFPathTable>(`${this.resourceUrl}/${this.getRdFPathTableIdentifier(rdFPathTable)}`, rdFPathTable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRdFPathTable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRdFPathTable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRdFPathTable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getRdFPathTableIdentifier(rdFPathTable: Pick<IRdFPathTable, 'id'>): number {
    return rdFPathTable.id;
  }

  compareRdFPathTable(o1: Pick<IRdFPathTable, 'id'> | null, o2: Pick<IRdFPathTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getRdFPathTableIdentifier(o1) === this.getRdFPathTableIdentifier(o2) : o1 === o2;
  }

  addRdFPathTableToCollectionIfMissing<Type extends Pick<IRdFPathTable, 'id'>>(
    rdFPathTableCollection: Type[],
    ...rdFPathTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rdFPathTables: Type[] = rdFPathTablesToCheck.filter(isPresent);
    if (rdFPathTables.length > 0) {
      const rdFPathTableCollectionIdentifiers = rdFPathTableCollection.map(
        rdFPathTableItem => this.getRdFPathTableIdentifier(rdFPathTableItem)!
      );
      const rdFPathTablesToAdd = rdFPathTables.filter(rdFPathTableItem => {
        const rdFPathTableIdentifier = this.getRdFPathTableIdentifier(rdFPathTableItem);
        if (rdFPathTableCollectionIdentifiers.includes(rdFPathTableIdentifier)) {
          return false;
        }
        rdFPathTableCollectionIdentifiers.push(rdFPathTableIdentifier);
        return true;
      });
      return [...rdFPathTablesToAdd, ...rdFPathTableCollection];
    }
    return rdFPathTableCollection;
  }
}
