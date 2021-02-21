import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRegions, Regions } from 'app/shared/model/regions.model';
import { RegionsService } from './regions.service';
import { RegionsComponent } from './regions.component';
import { RegionsDetailComponent } from './regions-detail.component';
import { RegionsUpdateComponent } from './regions-update.component';

@Injectable({ providedIn: 'root' })
export class RegionsResolve implements Resolve<IRegions> {
  constructor(private service: RegionsService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((regions: HttpResponse<Regions>) => {
          if (regions.body) {
            return of(regions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Regions());
  }
}

export const regionsRoute: Routes = [
  {
    path: '',
    component: RegionsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.regions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegionsDetailComponent,
    resolve: {
      regions: RegionsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.regions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegionsUpdateComponent,
    resolve: {
      regions: RegionsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.regions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegionsUpdateComponent,
    resolve: {
      regions: RegionsResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.regions.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
