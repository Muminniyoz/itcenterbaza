import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEventHistory, EventHistory } from 'app/shared/model/event-history.model';
import { EventHistoryService } from './event-history.service';
import { ICenter } from 'app/shared/model/center.model';
import { CenterService } from 'app/entities/center/center.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = ICenter | IUser;

@Component({
  selector: 'jhi-event-history-update',
  templateUrl: './event-history-update.component.html',
})
export class EventHistoryUpdateComponent implements OnInit {
  isSaving = false;
  centers: ICenter[] = [];
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    type: [],
    text: [],
    time: [],
    centerId: [],
    userId: [],
    openedUsers: [],
  });

  constructor(
    protected eventHistoryService: EventHistoryService,
    protected centerService: CenterService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventHistory }) => {
      if (!eventHistory.id) {
        const today = moment().startOf('day');
        eventHistory.time = today;
      }

      this.updateForm(eventHistory);

      this.centerService.query().subscribe((res: HttpResponse<ICenter[]>) => (this.centers = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(eventHistory: IEventHistory): void {
    this.editForm.patchValue({
      id: eventHistory.id,
      type: eventHistory.type,
      text: eventHistory.text,
      time: eventHistory.time ? eventHistory.time.format(DATE_TIME_FORMAT) : null,
      centerId: eventHistory.centerId,
      userId: eventHistory.userId,
      openedUsers: eventHistory.openedUsers,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventHistory = this.createFromForm();
    if (eventHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.eventHistoryService.update(eventHistory));
    } else {
      this.subscribeToSaveResponse(this.eventHistoryService.create(eventHistory));
    }
  }

  private createFromForm(): IEventHistory {
    return {
      ...new EventHistory(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      text: this.editForm.get(['text'])!.value,
      time: this.editForm.get(['time'])!.value ? moment(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      centerId: this.editForm.get(['centerId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      openedUsers: this.editForm.get(['openedUsers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventHistory>>): void {
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

  getSelected(selectedVals: IUser[], option: IUser): IUser {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
