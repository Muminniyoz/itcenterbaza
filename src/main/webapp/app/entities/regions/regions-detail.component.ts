import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegions } from 'app/shared/model/regions.model';

@Component({
  selector: 'jhi-regions-detail',
  templateUrl: './regions-detail.component.html',
})
export class RegionsDetailComponent implements OnInit {
  regions: IRegions | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regions }) => (this.regions = regions));
  }

  previousState(): void {
    window.history.back();
  }
}
