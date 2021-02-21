import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPaymentMethodConfig, PaymentMethodConfig } from 'app/shared/model/payment-method-config.model';
import { PaymentMethodConfigService } from './payment-method-config.service';
import { PaymentMethodConfigComponent } from './payment-method-config.component';
import { PaymentMethodConfigDetailComponent } from './payment-method-config-detail.component';
import { PaymentMethodConfigUpdateComponent } from './payment-method-config-update.component';

@Injectable({ providedIn: 'root' })
export class PaymentMethodConfigResolve implements Resolve<IPaymentMethodConfig> {
  constructor(private service: PaymentMethodConfigService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaymentMethodConfig> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paymentMethodConfig: HttpResponse<PaymentMethodConfig>) => {
          if (paymentMethodConfig.body) {
            return of(paymentMethodConfig.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PaymentMethodConfig());
  }
}

export const paymentMethodConfigRoute: Routes = [
  {
    path: '',
    component: PaymentMethodConfigComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.paymentMethodConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaymentMethodConfigDetailComponent,
    resolve: {
      paymentMethodConfig: PaymentMethodConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.paymentMethodConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaymentMethodConfigUpdateComponent,
    resolve: {
      paymentMethodConfig: PaymentMethodConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.paymentMethodConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaymentMethodConfigUpdateComponent,
    resolve: {
      paymentMethodConfig: PaymentMethodConfigResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.paymentMethodConfig.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
