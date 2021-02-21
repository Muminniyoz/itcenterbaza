import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEventHistory, EventHistory } from 'app/shared/model/event-history.model';
import { EventHistoryService } from './event-history.service';
import { EventHistoryComponent } from './event-history.component';
import { EventHistoryDetailComponent } from './event-history-detail.component';
import { EventHistoryUpdateComponent } from './event-history-update.component';

@Injectable({ providedIn: 'root' })
export class EventHistoryResolve implements Resolve<IEventHistory> {
  constructor(private service: EventHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEventHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((eventHistory: HttpResponse<EventHistory>) => {
          if (eventHistory.body) {
            return of(eventHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EventHistory());
  }
}

export const eventHistoryRoute: Routes = [
  {
    path: '',
    component: EventHistoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.eventHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventHistoryDetailComponent,
    resolve: {
      eventHistory: EventHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.eventHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventHistoryUpdateComponent,
    resolve: {
      eventHistory: EventHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.eventHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventHistoryUpdateComponent,
    resolve: {
      eventHistory: EventHistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'itcenterbazaApp.eventHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
