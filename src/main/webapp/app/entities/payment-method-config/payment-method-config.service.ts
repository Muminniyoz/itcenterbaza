import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';

type EntityResponseType = HttpResponse<IPaymentMethodConfig>;
type EntityArrayResponseType = HttpResponse<IPaymentMethodConfig[]>;

@Injectable({ providedIn: 'root' })
export class PaymentMethodConfigService {
  public resourceUrl = SERVER_API_URL + 'api/payment-method-configs';

  constructor(protected http: HttpClient) {}

  create(paymentMethodConfig: IPaymentMethodConfig): Observable<EntityResponseType> {
    return this.http.post<IPaymentMethodConfig>(this.resourceUrl, paymentMethodConfig, { observe: 'response' });
  }

  update(paymentMethodConfig: IPaymentMethodConfig): Observable<EntityResponseType> {
    return this.http.put<IPaymentMethodConfig>(this.resourceUrl, paymentMethodConfig, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentMethodConfig>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentMethodConfig[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
