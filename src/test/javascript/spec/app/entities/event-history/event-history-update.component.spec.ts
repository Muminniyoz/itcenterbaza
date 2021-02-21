import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { EventHistoryUpdateComponent } from 'app/entities/event-history/event-history-update.component';
import { EventHistoryService } from 'app/entities/event-history/event-history.service';
import { EventHistory } from 'app/shared/model/event-history.model';

describe('Component Tests', () => {
  describe('EventHistory Management Update Component', () => {
    let comp: EventHistoryUpdateComponent;
    let fixture: ComponentFixture<EventHistoryUpdateComponent>;
    let service: EventHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [EventHistoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EventHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EventHistoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EventHistoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EventHistory(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new EventHistory();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
