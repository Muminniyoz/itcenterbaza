import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { ISkill, Skill } from 'app/shared/model/skill.model';
import { SkillService } from './skill.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-skill-update',
  templateUrl: './skill-update.component.html',
})
export class SkillUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    titleUz: [null, [Validators.required]],
    titleRu: [],
    titleEn: [],
    about: [],
    planFile: [],
    planFileContentType: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected skillService: SkillService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ skill }) => {
      this.updateForm(skill);
    });
  }

  updateForm(skill: ISkill): void {
    this.editForm.patchValue({
      id: skill.id,
      titleUz: skill.titleUz,
      titleRu: skill.titleRu,
      titleEn: skill.titleEn,
      about: skill.about,
      planFile: skill.planFile,
      planFileContentType: skill.planFileContentType,
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
    const skill = this.createFromForm();
    if (skill.id !== undefined) {
      this.subscribeToSaveResponse(this.skillService.update(skill));
    } else {
      this.subscribeToSaveResponse(this.skillService.create(skill));
    }
  }

  private createFromForm(): ISkill {
    return {
      ...new Skill(),
      id: this.editForm.get(['id'])!.value,
      titleUz: this.editForm.get(['titleUz'])!.value,
      titleRu: this.editForm.get(['titleRu'])!.value,
      titleEn: this.editForm.get(['titleEn'])!.value,
      about: this.editForm.get(['about'])!.value,
      planFileContentType: this.editForm.get(['planFileContentType'])!.value,
      planFile: this.editForm.get(['planFile'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISkill>>): void {
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
