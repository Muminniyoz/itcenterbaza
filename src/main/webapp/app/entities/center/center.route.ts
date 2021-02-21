import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICenter, Center } from 'app/shared/model/center.model';
import { CenterService } from './center.service';
import { CenterComponent } from './center.component';
import { CenterDetailComponent } from './center-detail.component';
import { CenterUpdateComponent } from './center-update.component';

@Injectable({ providedIn: 'root' })
export class CenterResolve implements Resolve<ICenter> {
  constructor(private service: CenterService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICenter> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((center: HttpResponse<Center>) => {
          if (center.body) {
            return of(center.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Center());
  }
}

export const centerRoute: Routes = [
  {
    path: '',
    component: CenterComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.center.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CenterDetailComponent,
    resolve: {
      center: CenterResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.center.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CenterUpdateComponent,
    resolve: {
      center: CenterResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.center.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CenterUpdateComponent,
    resolve: {
      center: CenterResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.center.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
