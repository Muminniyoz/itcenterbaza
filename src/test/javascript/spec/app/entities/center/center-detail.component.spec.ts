import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { CenterDetailComponent } from 'app/entities/center/center-detail.component';
import { Center } from 'app/shared/model/center.model';

describe('Component Tests', () => {
  describe('Center Management Detail Component', () => {
    let comp: CenterDetailComponent;
    let fixture: ComponentFixture<CenterDetailComponent>;
    const route = ({ data: of({ center: new Center(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [CenterDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CenterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CenterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load center on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.center).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
