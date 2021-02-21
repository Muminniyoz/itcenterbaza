import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEventHistory } from 'app/shared/model/event-history.model';

@Component({
  selector: 'jhi-event-history-detail',
  templateUrl: './event-history-detail.component.html',
})
export class EventHistoryDetailComponent implements OnInit {
  eventHistory: IEventHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventHistory }) => (this.eventHistory = eventHistory));
  }

  previousState(): void {
    window.history.back();
  }
}
