import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRegions } from 'app/shared/model/regions.model';

type EntityResponseType = HttpResponse<IRegions>;
type EntityArrayResponseType = HttpResponse<IRegions[]>;

@Injectable({ providedIn: 'root' })
export class RegionsService {
  public resourceUrl = SERVER_API_URL + 'api/regions';

  constructor(protected http: HttpClient) {}

  create(regions: IRegions): Observable<EntityResponseType> {
    return this.http.post<IRegions>(this.resourceUrl, regions, { observe: 'response' });
  }

  update(regions: IRegions): Observable<EntityResponseType> {
    return this.http.put<IRegions>(this.resourceUrl, regions, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRegions>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRegions[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
