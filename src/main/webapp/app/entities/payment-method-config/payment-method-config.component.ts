import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentMethodConfig } from 'app/shared/model/payment-method-config.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PaymentMethodConfigService } from './payment-method-config.service';
import { PaymentMethodConfigDeleteDialogComponent } from './payment-method-config-delete-dialog.component';

@Component({
  selector: 'jhi-payment-method-config',
  templateUrl: './payment-method-config.component.html',
})
export class PaymentMethodConfigComponent implements OnInit, OnDestroy {
  paymentMethodConfigs: IPaymentMethodConfig[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected paymentMethodConfigService: PaymentMethodConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.paymentMethodConfigs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.paymentMethodConfigService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IPaymentMethodConfig[]>) => this.paginatePaymentMethodConfigs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.paymentMethodConfigs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPaymentMethodConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPaymentMethodConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPaymentMethodConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('paymentMethodConfigListModification', () => this.reset());
  }

  delete(paymentMethodConfig: IPaymentMethodConfig): void {
    const modalRef = this.modalService.open(PaymentMethodConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentMethodConfig = paymentMethodConfig;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePaymentMethodConfigs(data: IPaymentMethodConfig[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.paymentMethodConfigs.push(data[i]);
      }
    }
  }
}
