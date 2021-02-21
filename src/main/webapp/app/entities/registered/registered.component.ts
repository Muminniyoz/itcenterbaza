import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegistered } from 'app/shared/model/registered.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RegisteredService } from './registered.service';
import { RegisteredDeleteDialogComponent } from './registered-delete-dialog.component';

@Component({
  selector: 'jhi-registered',
  templateUrl: './registered.component.html',
})
export class RegisteredComponent implements OnInit, OnDestroy {
  registereds: IRegistered[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected registeredService: RegisteredService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.registereds = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.registeredService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRegistered[]>) => this.paginateRegistereds(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.registereds = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRegistereds();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRegistered): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRegistereds(): void {
    this.eventSubscriber = this.eventManager.subscribe('registeredListModification', () => this.reset());
  }

  delete(registered: IRegistered): void {
    const modalRef = this.modalService.open(RegisteredDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.registered = registered;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRegistereds(data: IRegistered[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.registereds.push(data[i]);
      }
    }
  }
}
