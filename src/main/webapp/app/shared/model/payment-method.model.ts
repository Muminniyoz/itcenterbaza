import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';

export interface IPaymentMethod {
  id?: number;
  paymentMethod?: string;
  description?: string;
  active?: boolean;
  confs?: IPaymentMethodConfig[];
}

export class PaymentMethod implements IPaymentMethod {
  constructor(
    public id?: number,
    public paymentMethod?: string,
    public description?: string,
    public active?: boolean,
    public confs?: IPaymentMethodConfig[]
  ) {
    this.active = this.active || false;
  }
}
