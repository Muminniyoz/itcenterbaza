import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';
import { PaymentMethodConfigService } from './payment-method-config.service';

@Component({
  templateUrl: './payment-method-config-delete-dialog.component.html',
})
export class PaymentMethodConfigDeleteDialogComponent {
  paymentMethodConfig?: IPaymentMethodConfig;

  constructor(
    protected paymentMethodConfigService: PaymentMethodConfigService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentMethodConfigService.delete(id).subscribe(() => {
      this.eventManager.broadcast('paymentMethodConfigListModification');
      this.activeModal.close();
    });
  }
}
