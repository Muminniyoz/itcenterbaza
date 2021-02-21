import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICenter, Center } from 'app/shared/model/center.model';
import { CenterService } from './center.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IRegions } from 'app/shared/model/regions.model';
import { RegionsService } from 'app/entities/regions/regions.service';

type SelectableEntity = IUser | IRegions;

@Component({
  selector: 'jhi-center-update',
  templateUrl: './center-update.component.html',
})
export class CenterUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  regions: IRegions[] = [];
  startDateDp: any;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    info: [],
    startDate: [],
    googleMapUrl: [],
    modifiedById: [],
    regionsId: [],
    managerId: [],
  });

  constructor(
    protected centerService: CenterService,
    protected userService: UserService,
    protected regionsService: RegionsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ center }) => {
      this.updateForm(center);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.regionsService.query().subscribe((res: HttpResponse<IRegions[]>) => (this.regions = res.body || []));
    });
  }

  updateForm(center: ICenter): void {
    this.editForm.patchValue({
      id: center.id,
      title: center.title,
      info: center.info,
      startDate: center.startDate,
      googleMapUrl: center.googleMapUrl,
      modifiedById: center.modifiedById,
      regionsId: center.regionsId,
      managerId: center.managerId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const center = this.createFromForm();
    if (center.id !== undefined) {
      this.subscribeToSaveResponse(this.centerService.update(center));
    } else {
      this.subscribeToSaveResponse(this.centerService.create(center));
    }
  }

  private createFromForm(): ICenter {
    return {
      ...new Center(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      info: this.editForm.get(['info'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      googleMapUrl: this.editForm.get(['googleMapUrl'])!.value,
      modifiedById: this.editForm.get(['modifiedById'])!.value,
      regionsId: this.editForm.get(['regionsId'])!.value,
      managerId: this.editForm.get(['managerId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICenter>>): void {
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
