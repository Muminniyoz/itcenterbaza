import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeacher } from 'app/shared/model/teacher.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TeacherService } from './teacher.service';
import { TeacherDeleteDialogComponent } from './teacher-delete-dialog.component';

@Component({
  selector: 'jhi-teacher',
  templateUrl: './teacher.component.html',
})
export class TeacherComponent implements OnInit, OnDestroy {
  teachers: ITeacher[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected teacherService: TeacherService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.teachers = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.teacherService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<ITeacher[]>) => this.paginateTeachers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.teachers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTeachers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITeacher): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInTeachers(): void {
    this.eventSubscriber = this.eventManager.subscribe('teacherListModification', () => this.reset());
  }

  delete(teacher: ITeacher): void {
    const modalRef = this.modalService.open(TeacherDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.teacher = teacher;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTeachers(data: ITeacher[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.teachers.push(data[i]);
      }
    }
  }
}
