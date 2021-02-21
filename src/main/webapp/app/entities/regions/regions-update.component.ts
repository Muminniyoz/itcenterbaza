import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IRegions, Regions } from 'app/shared/model/regions.model';
import { RegionsService } from './regions.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-regions-update',
  templateUrl: './regions-update.component.html',
})
export class RegionsUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    info: [],
    googleUrl: [],
    directorId: [],
  });

  constructor(
    protected regionsService: RegionsService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regions }) => {
      this.updateForm(regions);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(regions: IRegions): void {
    this.editForm.patchValue({
      id: regions.id,
      title: regions.title,
      info: regions.info,
      googleUrl: regions.googleUrl,
      directorId: regions.directorId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const regions = this.createFromForm();
    if (regions.id !== undefined) {
      this.subscribeToSaveResponse(this.regionsService.update(regions));
    } else {
      this.subscribeToSaveResponse(this.regionsService.create(regions));
    }
  }

  private createFromForm(): IRegions {
    return {
      ...new Regions(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      info: this.editForm.get(['info'])!.value,
      googleUrl: this.editForm.get(['googleUrl'])!.value,
      directorId: this.editForm.get(['directorId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegions>>): void {
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

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
