import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEventHistory } from 'app/shared/model/event-history.model';
import { EventHistoryService } from './event-history.service';

@Component({
  templateUrl: './event-history-delete-dialog.component.html',
})
export class EventHistoryDeleteDialogComponent {
  eventHistory?: IEventHistory;

  constructor(
    protected eventHistoryService: EventHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('eventHistoryListModification');
      this.activeModal.close();
    });
  }
}
