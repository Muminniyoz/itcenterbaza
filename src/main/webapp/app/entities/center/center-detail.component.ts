import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICenter } from 'app/shared/model/center.model';

@Component({
  selector: 'jhi-center-detail',
  templateUrl: './center-detail.component.html',
})
export class CenterDetailComponent implements OnInit {
  center: ICenter | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ center }) => (this.center = center));
  }

  previousState(): void {
    window.history.back();
  }
}
