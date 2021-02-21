import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPaymentMethodConfig, PaymentMethodConfig } from 'app/shared/model/payment-method-config.model';
import { PaymentMethodConfigService } from './payment-method-config.service';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';

@Component({
  selector: 'jhi-payment-method-config-update',
  templateUrl: './payment-method-config-update.component.html',
})
export class PaymentMethodConfigUpdateComponent implements OnInit {
  isSaving = false;
  paymentmethods: IPaymentMethod[] = [];

  editForm = this.fb.group({
    id: [],
    key: [],
    value: [],
    note: [],
    enabled: [],
    methodId: [],
  });

  constructor(
    protected paymentMethodConfigService: PaymentMethodConfigService,
    protected paymentMethodService: PaymentMethodService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paymentMethodConfig }) => {
      this.updateForm(paymentMethodConfig);

      this.paymentMethodService.query().subscribe((res: HttpResponse<IPaymentMethod[]>) => (this.paymentmethods = res.body || []));
    });
  }

  updateForm(paymentMethodConfig: IPaymentMethodConfig): void {
    this.editForm.patchValue({
      id: paymentMethodConfig.id,
      key: paymentMethodConfig.key,
      value: paymentMethodConfig.value,
      note: paymentMethodConfig.note,
      enabled: paymentMethodConfig.enabled,
      methodId: paymentMethodConfig.methodId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paymentMethodConfig = this.createFromForm();
    if (paymentMethodConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentMethodConfigService.update(paymentMethodConfig));
    } else {
      this.subscribeToSaveResponse(this.paymentMethodConfigService.create(paymentMethodConfig));
    }
  }

  private createFromForm(): IPaymentMethodConfig {
    return {
      ...new PaymentMethodConfig(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      value: this.editForm.get(['value'])!.value,
      note: this.editForm.get(['note'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
      methodId: this.editForm.get(['methodId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaymentMethodConfig>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IPaymentMethod): any {
    return item.id;
  }
}
