import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TeacherService } from 'app/entities/teacher/teacher.service';
import { ITeacher, Teacher } from 'app/shared/model/teacher.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

describe('Service Tests', () => {
  describe('Teacher Service', () => {
    let injector: TestBed;
    let service: TeacherService;
    let httpMock: HttpTestingController;
    let elemDefault: ITeacher;
    let expectedResult: ITeacher | ITeacher[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TeacherService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Teacher(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        Gender.MALE,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        currentDate,
        false,
        'image/png',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateOfBirth: currentDate.format(DATE_FORMAT),
            registerationDate: currentDate.format(DATE_FORMAT),
            leaveDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Teacher', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateOfBirth: currentDate.format(DATE_FORMAT),
            registerationDate: currentDate.format(DATE_FORMAT),
            leaveDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            registerationDate: currentDate,
            leaveDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Teacher()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Teacher', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            middleName: 'BBBBBB',
            email: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            gender: 'BBBBBB',
            registerationDate: currentDate.format(DATE_FORMAT),
            telephone: 'BBBBBB',
            mobile: 'BBBBBB',
            fullPhotoUrl: 'BBBBBB',
            active: true,
            key: 'BBBBBB',
            about: 'BBBBBB',
            portfolia: 'BBBBBB',
            info: 'BBBBBB',
            leaveDate: currentDate.format(DATE_FORMAT),
            isShowingHome: true,
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            registerationDate: currentDate,
            leaveDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Teacher', () => {
        const returnedFromService = Object.assign(
          {
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            middleName: 'BBBBBB',
            email: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
            gender: 'BBBBBB',
            registerationDate: currentDate.format(DATE_FORMAT),
            telephone: 'BBBBBB',
            mobile: 'BBBBBB',
            fullPhotoUrl: 'BBBBBB',
            active: true,
            key: 'BBBBBB',
            about: 'BBBBBB',
            portfolia: 'BBBBBB',
            info: 'BBBBBB',
            leaveDate: currentDate.format(DATE_FORMAT),
            isShowingHome: true,
            image: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateOfBirth: currentDate,
            registerationDate: currentDate,
            leaveDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Teacher', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
