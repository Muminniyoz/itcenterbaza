import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITeacher } from 'app/shared/model/teacher.model';
import { TeacherService } from 'app/entities/teacher/teacher.service';
import { ICenter } from 'app/shared/model/center.model';
import { CenterService } from 'app/entities/center/center.service';
import { ISkill } from 'app/shared/model/skill.model';
import { SkillService } from 'app/entities/skill/skill.service';

type SelectableEntity = ITeacher | ICenter | ISkill;

@Component({
  selector: 'jhi-course-update',
  templateUrl: './course-update.component.html',
})
export class CourseUpdateComponent implements OnInit {
  isSaving = false;
  teachers: ITeacher[] = [];
  centers: ICenter[] = [];
  skills: ISkill[] = [];
  startDateDp: any;

  editForm = this.fb.group({
    id: [],
    title: [],
    price: [],
    startDate: [],
    status: [],
    duration: [],
    planFile: [],
    planFileContentType: [],
    teacherId: [],
    centerId: [],
    skillId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected courseService: CourseService,
    protected teacherService: TeacherService,
    protected centerService: CenterService,
    protected skillService: SkillService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ course }) => {
      this.updateForm(course);

      this.teacherService.query().subscribe((res: HttpResponse<ITeacher[]>) => (this.teachers = res.body || []));

      this.centerService.query().subscribe((res: HttpResponse<ICenter[]>) => (this.centers = res.body || []));

      this.skillService.query().subscribe((res: HttpResponse<ISkill[]>) => (this.skills = res.body || []));
    });
  }

  updateForm(course: ICourse): void {
    this.editForm.patchValue({
      id: course.id,
      title: course.title,
      price: course.price,
      startDate: course.startDate,
      status: course.status,
      duration: course.duration,
      planFile: course.planFile,
      planFileContentType: course.planFileContentType,
      teacherId: course.teacherId,
      centerId: course.centerId,
      skillId: course.skillId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('itcenterbazaApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const course = this.createFromForm();
    if (course.id !== undefined) {
      this.subscribeToSaveResponse(this.courseService.update(course));
    } else {
      this.subscribeToSaveResponse(this.courseService.create(course));
    }
  }

  private createFromForm(): ICourse {
    return {
      ...new Course(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      price: this.editForm.get(['price'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      status: this.editForm.get(['status'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      planFileContentType: this.editForm.get(['planFileContentType'])!.value,
      planFile: this.editForm.get(['planFile'])!.value,
      teacherId: this.editForm.get(['teacherId'])!.value,
      centerId: this.editForm.get(['centerId'])!.value,
      skillId: this.editForm.get(['skillId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourse>>): void {
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
