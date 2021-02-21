import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { RegionsUpdateComponent } from 'app/entities/regions/regions-update.component';
import { RegionsService } from 'app/entities/regions/regions.service';
import { Regions } from 'app/shared/model/regions.model';

describe('Component Tests', () => {
  describe('Regions Management Update Component', () => {
    let comp: RegionsUpdateComponent;
    let fixture: ComponentFixture<RegionsUpdateComponent>;
    let service: RegionsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [RegionsUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RegionsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RegionsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RegionsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Regions(123);
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
        const entity = new Regions();
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
