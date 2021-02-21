import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { RegisteredUpdateComponent } from 'app/entities/registered/registered-update.component';
import { RegisteredService } from 'app/entities/registered/registered.service';
import { Registered } from 'app/shared/model/registered.model';

describe('Component Tests', () => {
  describe('Registered Management Update Component', () => {
    let comp: RegisteredUpdateComponent;
    let fixture: ComponentFixture<RegisteredUpdateComponent>;
    let service: RegisteredService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [RegisteredUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RegisteredUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RegisteredUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RegisteredService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Registered(123);
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
        const entity = new Registered();
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
