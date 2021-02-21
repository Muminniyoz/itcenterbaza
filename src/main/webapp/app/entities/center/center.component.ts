import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICenter } from 'app/shared/model/center.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CenterService } from './center.service';
import { CenterDeleteDialogComponent } from './center-delete-dialog.component';

@Component({
  selector: 'jhi-center',
  templateUrl: './center.component.html',
})
export class CenterComponent implements OnInit, OnDestroy {
  centers: ICenter[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected centerService: CenterService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.centers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.centerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ICenter[]>) => this.paginateCenters(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.centers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCenters();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICenter): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCenters(): void {
    this.eventSubscriber = this.eventManager.subscribe('centerListModification', () => this.reset());
  }

  delete(center: ICenter): void {
    const modalRef = this.modalService.open(CenterDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.center = center;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCenters(data: ICenter[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.centers.push(data[i]);
      }
    }
  }
}
