import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { CenterUpdateComponent } from 'app/entities/center/center-update.component';
import { CenterService } from 'app/entities/center/center.service';
import { Center } from 'app/shared/model/center.model';

describe('Component Tests', () => {
  describe('Center Management Update Component', () => {
    let comp: CenterUpdateComponent;
    let fixture: ComponentFixture<CenterUpdateComponent>;
    let service: CenterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [CenterUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(CenterUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CenterUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CenterService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Center(123);
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
        const entity = new Center();
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
