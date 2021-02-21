import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPayment, Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IParticipant } from 'app/shared/model/participant.model';
import { ParticipantService } from 'app/entities/participant/participant.service';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { PaymentMethodService } from 'app/entities/payment-method/payment-method.service';

type SelectableEntity = IUser | IParticipant | IPaymentMethod;

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html',
})
export class PaymentUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  participants: IParticipant[] = [];
  paymentmethods: IPaymentMethod[] = [];

  editForm = this.fb.group({
    id: [],
    paymentDate: [],
    paymentProvider: [],
    amount: [],
    paymentStatus: [],
    curency: [],
    customerName: [],
    isEnough: [],
    isConfirmed: [],
    modifiedById: [],
    studentId: [],
    methodId: [],
  });

  constructor(
    protected paymentService: PaymentService,
    protected userService: UserService,
    protected participantService: ParticipantService,
    protected paymentMethodService: PaymentMethodService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payment }) => {
      if (!payment.id) {
        const today = moment().startOf('day');
        payment.paymentDate = today;
      }

      this.updateForm(payment);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.participantService.query().subscribe((res: HttpResponse<IParticipant[]>) => (this.participants = res.body || []));

      this.paymentMethodService.query().subscribe((res: HttpResponse<IPaymentMethod[]>) => (this.paymentmethods = res.body || []));
    });
  }

  updateForm(payment: IPayment): void {
    this.editForm.patchValue({
      id: payment.id,
      paymentDate: payment.paymentDate ? payment.paymentDate.format(DATE_TIME_FORMAT) : null,
      paymentProvider: payment.paymentProvider,
      amount: payment.amount,
      paymentStatus: payment.paymentStatus,
      curency: payment.curency,
      customerName: payment.customerName,
      isEnough: payment.isEnough,
      isConfirmed: payment.isConfirmed,
      modifiedById: payment.modifiedById,
      studentId: payment.studentId,
      methodId: payment.methodId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    return {
      ...new Payment(),
      id: this.editForm.get(['id'])!.value,
      paymentDate: this.editForm.get(['paymentDate'])!.value
        ? moment(this.editForm.get(['paymentDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      paymentProvider: this.editForm.get(['paymentProvider'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      paymentStatus: this.editForm.get(['paymentStatus'])!.value,
      curency: this.editForm.get(['curency'])!.value,
      customerName: this.editForm.get(['customerName'])!.value,
      isEnough: this.editForm.get(['isEnough'])!.value,
      isConfirmed: this.editForm.get(['isConfirmed'])!.value,
      modifiedById: this.editForm.get(['modifiedById'])!.value,
      studentId: this.editForm.get(['studentId'])!.value,
      methodId: this.editForm.get(['methodId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
