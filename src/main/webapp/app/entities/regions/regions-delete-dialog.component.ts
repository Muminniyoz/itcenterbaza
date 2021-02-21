import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRegions } from 'app/shared/model/regions.model';
import { RegionsService } from './regions.service';

@Component({
  templateUrl: './regions-delete-dialog.component.html',
})
export class RegionsDeleteDialogComponent {
  regions?: IRegions;

  constructor(protected regionsService: RegionsService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.regionsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('regionsListModification');
      this.activeModal.close();
    });
  }
}
