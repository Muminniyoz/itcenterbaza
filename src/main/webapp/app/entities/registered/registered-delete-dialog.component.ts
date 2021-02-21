import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRegistered } from 'app/shared/model/registered.model';
import { RegisteredService } from './registered.service';

@Component({
  templateUrl: './registered-delete-dialog.component.html',
})
export class RegisteredDeleteDialogComponent {
  registered?: IRegistered;

  constructor(
    protected registeredService: RegisteredService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.registeredService.delete(id).subscribe(() => {
      this.eventManager.broadcast('registeredListModification');
      this.activeModal.close();
    });
  }
}
