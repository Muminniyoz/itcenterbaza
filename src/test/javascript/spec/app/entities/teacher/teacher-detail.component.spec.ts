import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { ItcenterbazaTestModule } from '../../../test.module';
import { TeacherDetailComponent } from 'app/entities/teacher/teacher-detail.component';
import { Teacher } from 'app/shared/model/teacher.model';

describe('Component Tests', () => {
  describe('Teacher Management Detail Component', () => {
    let comp: TeacherDetailComponent;
    let fixture: ComponentFixture<TeacherDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ teacher: new Teacher(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ItcenterbazaTestModule],
        declarations: [TeacherDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TeacherDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeacherDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load teacher on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.teacher).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
