export interface IPaymentMethodConfig {
  id?: number;
  key?: string;
  value?: string;
  note?: string;
  enabled?: boolean;
  methodId?: number;
}

export class PaymentMethodConfig implements IPaymentMethodConfig {
  constructor(
    public id?: number,
    public key?: string,
    public value?: string,
    public note?: string,
    public enabled?: boolean,
    public methodId?: number
  ) {
    this.enabled = this.enabled || false;
  }
}
