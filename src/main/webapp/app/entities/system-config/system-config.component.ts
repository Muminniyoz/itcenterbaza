import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemConfig } from 'app/shared/model/system-config.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { SystemConfigService } from './system-config.service';
import { SystemConfigDeleteDialogComponent } from './system-config-delete-dialog.component';

@Component({
  selector: 'jhi-system-config',
  templateUrl: './system-config.component.html',
})
export class SystemConfigComponent implements OnInit, OnDestroy {
  systemConfigs: ISystemConfig[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected systemConfigService: SystemConfigService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.systemConfigs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.systemConfigService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ISystemConfig[]>) => this.paginateSystemConfigs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.systemConfigs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSystemConfigs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISystemConfig): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSystemConfigs(): void {
    this.eventSubscriber = this.eventManager.subscribe('systemConfigListModification', () => this.reset());
  }

  delete(systemConfig: ISystemConfig): void {
    const modalRef = this.modalService.open(SystemConfigDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.systemConfig = systemConfig;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateSystemConfigs(data: ISystemConfig[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.systemConfigs.push(data[i]);
      }
    }
  }
}
