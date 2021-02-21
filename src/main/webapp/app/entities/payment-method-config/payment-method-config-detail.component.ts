import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';

@Component({
  selector: 'jhi-payment-method-config-detail',
  templateUrl: './payment-method-config-detail.component.html',
})
export class PaymentMethodConfigDetailComponent implements OnInit {
  paymentMethodConfig: IPaymentMethodConfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethodConfig }) => (this.paymentMethodConfig = paymentMethodConfig));
  }

  previousState(): void {
    window.history.back();
  }
}
