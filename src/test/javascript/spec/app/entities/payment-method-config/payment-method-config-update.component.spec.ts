import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { PaymentMethodConfigUpdateComponent } from 'app/entities/payment-method-config/payment-method-config-update.component';
import { PaymentMethodConfigService } from 'app/entities/payment-method-config/payment-method-config.service';
import { PaymentMethodConfig } from 'app/shared/model/payment-method-config.model';

describe('Component Tests', () => {
  describe('PaymentMethodConfig Management Update Component', () => {
    let comp: PaymentMethodConfigUpdateComponent;
    let fixture: ComponentFixture<PaymentMethodConfigUpdateComponent>;
    let service: PaymentMethodConfigService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [PaymentMethodConfigUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PaymentMethodConfigUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentMethodConfigUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PaymentMethodConfigService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PaymentMethodConfig(123);
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
        const entity = new PaymentMethodConfig();
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
