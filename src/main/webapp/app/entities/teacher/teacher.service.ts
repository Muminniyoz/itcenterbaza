import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITeacher } from 'app/shared/model/teacher.model';

type EntityResponseType = HttpResponse<ITeacher>;
type EntityArrayResponseType = HttpResponse<ITeacher[]>;

@Injectable({ providedIn: 'root' })
export class TeacherService {
  public resourceUrl = SERVER_API_URL + 'api/teachers';

  constructor(protected http: HttpClient) {}

  create(teacher: ITeacher): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(teacher);
    return this.http
      .post<ITeacher>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(teacher: ITeacher): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(teacher);
    return this.http
      .put<ITeacher>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITeacher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITeacher[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(teacher: ITeacher): ITeacher {
    const copy: ITeacher = Object.assign({}, teacher, {
      dateOfBirth: teacher.dateOfBirth && teacher.dateOfBirth.isValid() ? teacher.dateOfBirth.format(DATE_FORMAT) : undefined,
      registerationDate:
        teacher.registerationDate && teacher.registerationDate.isValid() ? teacher.registerationDate.format(DATE_FORMAT) : undefined,
      leaveDate: teacher.leaveDate && teacher.leaveDate.isValid() ? teacher.leaveDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOfBirth = res.body.dateOfBirth ? moment(res.body.dateOfBirth) : undefined;
      res.body.registerationDate = res.body.registerationDate ? moment(res.body.registerationDate) : undefined;
      res.body.leaveDate = res.body.leaveDate ? moment(res.body.leaveDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((teacher: ITeacher) => {
        teacher.dateOfBirth = teacher.dateOfBirth ? moment(teacher.dateOfBirth) : undefined;
        teacher.registerationDate = teacher.registerationDate ? moment(teacher.registerationDate) : undefined;
        teacher.leaveDate = teacher.leaveDate ? moment(teacher.leaveDate) : undefined;
      });
    }
    return res;
  }
}
