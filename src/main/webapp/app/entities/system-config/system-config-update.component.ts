import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISystemConfig, SystemConfig } from 'app/shared/model/system-config.model';
import { SystemConfigService } from './system-config.service';

@Component({
  selector: 'jhi-system-config-update',
  templateUrl: './system-config-update.component.html',
})
export class SystemConfigUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    key: [],
    value: [],
    note: [],
    enabled: [],
  });

  constructor(protected systemConfigService: SystemConfigService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemConfig }) => {
      this.updateForm(systemConfig);
    });
  }

  updateForm(systemConfig: ISystemConfig): void {
    this.editForm.patchValue({
      id: systemConfig.id,
      key: systemConfig.key,
      value: systemConfig.value,
      note: systemConfig.note,
      enabled: systemConfig.enabled,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const systemConfig = this.createFromForm();
    if (systemConfig.id !== undefined) {
      this.subscribeToSaveResponse(this.systemConfigService.update(systemConfig));
    } else {
      this.subscribeToSaveResponse(this.systemConfigService.create(systemConfig));
    }
  }

  private createFromForm(): ISystemConfig {
    return {
      ...new SystemConfig(),
      id: this.editForm.get(['id'])!.value,
      key: this.editForm.get(['key'])!.value,
      value: this.editForm.get(['value'])!.value,
      note: this.editForm.get(['note'])!.value,
      enabled: this.editForm.get(['enabled'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISystemConfig>>): void {
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
}
