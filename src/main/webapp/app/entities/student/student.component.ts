import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStudent } from 'app/shared/model/student.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { StudentService } from './student.service';
import { StudentDeleteDialogComponent } from './student-delete-dialog.component';

@Component({
  selector: 'jhi-student',
  templateUrl: './student.component.html',
})
export class StudentComponent implements OnInit, OnDestroy {
  students: IStudent[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected studentService: StudentService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.students = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.studentService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IStudent[]>) => this.paginateStudents(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.students = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStudents();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStudent): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStudents(): void {
    this.eventSubscriber = this.eventManager.subscribe('studentListModification', () => this.reset());
  }

  delete(student: IStudent): void {
    const modalRef = this.modalService.open(StudentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.student = student;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateStudents(data: IStudent[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.students.push(data[i]);
      }
    }
  }
}
