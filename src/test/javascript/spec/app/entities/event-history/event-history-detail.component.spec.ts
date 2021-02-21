import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { EventHistoryDetailComponent } from 'app/entities/event-history/event-history-detail.component';
import { EventHistory } from 'app/shared/model/event-history.model';

describe('Component Tests', () => {
  describe('EventHistory Management Detail Component', () => {
    let comp: EventHistoryDetailComponent;
    let fixture: ComponentFixture<EventHistoryDetailComponent>;
    const route = ({ data: of({ eventHistory: new EventHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [EventHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EventHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EventHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load eventHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.eventHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
