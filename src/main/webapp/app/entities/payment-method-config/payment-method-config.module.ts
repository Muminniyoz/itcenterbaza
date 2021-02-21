import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItcenterbazaSharedModule } from 'app/shared/shared.module';
import { PaymentMethodConfigComponent } from './payment-method-config.component';
import { PaymentMethodConfigDetailComponent } from './payment-method-config-detail.component';
import { PaymentMethodConfigUpdateComponent } from './payment-method-config-update.component';
import { PaymentMethodConfigDeleteDialogComponent } from './payment-method-config-delete-dialog.component';
import { paymentMethodConfigRoute } from './payment-method-config.route';

@NgModule({
  imports: [ItcenterbazaSharedModule, RouterModule.forChild(paymentMethodConfigRoute)],
  declarations: [
    PaymentMethodConfigComponent,
    PaymentMethodConfigDetailComponent,
    PaymentMethodConfigUpdateComponent,
    PaymentMethodConfigDeleteDialogComponent,
  ],
  entryComponents: [PaymentMethodConfigDeleteDialogComponent],
})
export class ItcenterbazaPaymentMethodConfigModule {}
