import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICenter } from 'app/shared/model/center.model';
import { CenterService } from './center.service';

@Component({
  templateUrl: './center-delete-dialog.component.html',
})
export class CenterDeleteDialogComponent {
  center?: ICenter;

  constructor(protected centerService: CenterService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.centerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('centerListModification');
      this.activeModal.close();
    });
  }
}
