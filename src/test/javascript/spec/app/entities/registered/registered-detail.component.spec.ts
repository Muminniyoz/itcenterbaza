import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { RegisteredDetailComponent } from 'app/entities/registered/registered-detail.component';
import { Registered } from 'app/shared/model/registered.model';

describe('Component Tests', () => {
  describe('Registered Management Detail Component', () => {
    let comp: RegisteredDetailComponent;
    let fixture: ComponentFixture<RegisteredDetailComponent>;
    const route = ({ data: of({ registered: new Registered(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [RegisteredDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RegisteredDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RegisteredDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load registered on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.registered).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
