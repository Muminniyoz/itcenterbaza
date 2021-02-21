import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegistered } from 'app/shared/model/registered.model';

@Component({
  selector: 'jhi-registered-detail',
  templateUrl: './registered-detail.component.html',
})
export class RegisteredDetailComponent implements OnInit {
  registered: IRegistered | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ registered }) => (this.registered = registered));
  }

  previousState(): void {
    window.history.back();
  }
}
