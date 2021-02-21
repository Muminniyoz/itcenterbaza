import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEventHistory } from 'app/shared/model/event-history.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EventHistoryService } from './event-history.service';
import { EventHistoryDeleteDialogComponent } from './event-history-delete-dialog.component';

@Component({
  selector: 'jhi-event-history',
  templateUrl: './event-history.component.html',
})
export class EventHistoryComponent implements OnInit, OnDestroy {
  eventHistories: IEventHistory[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected eventHistoryService: EventHistoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.eventHistories = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.eventHistoryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IEventHistory[]>) => this.paginateEventHistories(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.eventHistories = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEventHistories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEventHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEventHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('eventHistoryListModification', () => this.reset());
  }

  delete(eventHistory: IEventHistory): void {
    const modalRef = this.modalService.open(EventHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.eventHistory = eventHistory;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEventHistories(data: IEventHistory[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.eventHistories.push(data[i]);
      }
    }
  }
}
