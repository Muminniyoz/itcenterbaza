import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRegistered } from 'app/shared/model/registered.model';

type EntityResponseType = HttpResponse<IRegistered>;
type EntityArrayResponseType = HttpResponse<IRegistered[]>;

@Injectable({ providedIn: 'root' })
export class RegisteredService {
  public resourceUrl = SERVER_API_URL + 'api/registereds';

  constructor(protected http: HttpClient) {}

  create(registered: IRegistered): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registered);
    return this.http
      .post<IRegistered>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(registered: IRegistered): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(registered);
    return this.http
      .put<IRegistered>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRegistered>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRegistered[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(registered: IRegistered): IRegistered {
    const copy: IRegistered = Object.assign({}, registered, {
      dateOfBirth: registered.dateOfBirth && registered.dateOfBirth.isValid() ? registered.dateOfBirth.format(DATE_FORMAT) : undefined,
      registerationDate:
        registered.registerationDate && registered.registerationDate.isValid()
          ? registered.registerationDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? moment(res.body.dateOfBirth) : undefined;
      res.body.registerationDate = res.body.registerationDate ? moment(res.body.registerationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((registered: IRegistered) => {
        registered.dateOfBirth = registered.dateOfBirth ? moment(registered.dateOfBirth) : undefined;
        registered.registerationDate = registered.registerationDate ? moment(registered.registerationDate) : undefined;
      });
    }
    return res;
  }
}
