import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICenter } from 'app/shared/model/center.model';

type EntityResponseType = HttpResponse<ICenter>;
type EntityArrayResponseType = HttpResponse<ICenter[]>;

@Injectable({ providedIn: 'root' })
export class CenterService {
  public resourceUrl = SERVER_API_URL + 'api/centers';

  constructor(protected http: HttpClient) {}

  create(center: ICenter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(center);
    return this.http
      .post<ICenter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(center: ICenter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(center);
    return this.http
      .put<ICenter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICenter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICenter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(center: ICenter): ICenter {
    const copy: ICenter = Object.assign({}, center, {
      startDate: center.startDate && center.startDate.isValid() ? center.startDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((center: ICenter) => {
        center.startDate = center.startDate ? moment(center.startDate) : undefined;
      });
    }
    return res;
  }
}
