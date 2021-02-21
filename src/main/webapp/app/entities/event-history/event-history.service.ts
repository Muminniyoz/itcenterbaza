import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IEventHistory } from 'app/shared/model/event-history.model';

type EntityResponseType = HttpResponse<IEventHistory>;
type EntityArrayResponseType = HttpResponse<IEventHistory[]>;

@Injectable({ providedIn: 'root' })
export class EventHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/event-histories';

  constructor(protected http: HttpClient) {}

  create(eventHistory: IEventHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventHistory);
    return this.http
      .post<IEventHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(eventHistory: IEventHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eventHistory);
    return this.http
      .put<IEventHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEventHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEventHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(eventHistory: IEventHistory): IEventHistory {
    const copy: IEventHistory = Object.assign({}, eventHistory, {
      time: eventHistory.time && eventHistory.time.isValid() ? eventHistory.time.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.time = res.body.time ? moment(res.body.time) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((eventHistory: IEventHistory) => {
        eventHistory.time = eventHistory.time ? moment(eventHistory.time) : undefined;
      });
    }
    return res;
  }
}
