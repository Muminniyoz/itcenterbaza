import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRegistered, Registered } from 'app/shared/model/registered.model';
import { RegisteredService } from './registered.service';
import { RegisteredComponent } from './registered.component';
import { RegisteredDetailComponent } from './registered-detail.component';
import { RegisteredUpdateComponent } from './registered-update.component';

@Injectable({ providedIn: 'root' })
export class RegisteredResolve implements Resolve<IRegistered> {
  constructor(private service: RegisteredService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegistered> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((registered: HttpResponse<Registered>) => {
          if (registered.body) {
            return of(registered.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Registered());
  }
}

export const registeredRoute: Routes = [
  {
    path: '',
    component: RegisteredComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.registered.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegisteredDetailComponent,
    resolve: {
      registered: RegisteredResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.registered.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegisteredUpdateComponent,
    resolve: {
      registered: RegisteredResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.registered.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegisteredUpdateComponent,
    resolve: {
      registered: RegisteredResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.registered.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
