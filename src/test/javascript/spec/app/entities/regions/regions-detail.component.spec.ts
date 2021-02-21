import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItcenterbazaTestModule } from '../../../test.module';
import { RegionsDetailComponent } from 'app/entities/regions/regions-detail.component';
import { Regions } from 'app/shared/model/regions.model';

describe('Component Tests', () => {
  describe('Regions Management Detail Component', () => {
    let comp: RegionsDetailComponent;
    let fixture: ComponentFixture<RegionsDetailComponent>;
    const route = ({ data: of({ regions: new Regions(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [RegionsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RegionsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RegionsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load regions on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.regions).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
