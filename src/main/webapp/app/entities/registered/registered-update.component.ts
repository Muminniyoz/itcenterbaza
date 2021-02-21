import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRegistered, Registered } from 'app/shared/model/registered.model';
import { RegisteredService } from './registered.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

type SelectableEntity = IUser | ICourse;

@Component({
  selector: 'jhi-registered-update',
  templateUrl: './registered-update.component.html',
})
export class RegisteredUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  courses: ICourse[] = [];
  dateOfBirthDp: any;
  registerationDateDp: any;

  editForm = this.fb.group({
    id: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    middleName: [],
    email: [],
    dateOfBirth: [],
    gender: [],
    registerationDate: [],
    telephone: [],
    mobile: [],
    modifiedById: [],
    courseId: [],
  });

  constructor(
    protected registeredService: RegisteredService,
    protected userService: UserService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registered }) => {
      this.updateForm(registered);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.courseService.query().subscribe((res: HttpResponse<ICourse[]>) => (this.courses = res.body || []));
    });
  }

  updateForm(registered: IRegistered): void {
    this.editForm.patchValue({
      id: registered.id,
      firstName: registered.firstName,
      lastName: registered.lastName,
      middleName: registered.middleName,
      email: registered.email,
      dateOfBirth: registered.dateOfBirth,
      gender: registered.gender,
      registerationDate: registered.registerationDate,
      telephone: registered.telephone,
      mobile: registered.mobile,
      modifiedById: registered.modifiedById,
      courseId: registered.courseId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const registered = this.createFromForm();
    if (registered.id !== undefined) {
      this.subscribeToSaveResponse(this.registeredService.update(registered));
    } else {
      this.subscribeToSaveResponse(this.registeredService.create(registered));
    }
  }

  private createFromForm(): IRegistered {
    return {
      ...new Registered(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      middleName: this.editForm.get(['middleName'])!.value,
      email: this.editForm.get(['email'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      registerationDate: this.editForm.get(['registerationDate'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      modifiedById: this.editForm.get(['modifiedById'])!.value,
      courseId: this.editForm.get(['courseId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegistered>>): void {
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
