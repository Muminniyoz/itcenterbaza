import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { PaymentMethodConfigService } from 'app/entities/payment-method-config/payment-method-config.service';
import { IPaymentMethodConfig, PaymentMethodConfig } from 'app/shared/model/payment-method-config.model';

describe('Service Tests', () => {
  describe('PaymentMethodConfig Service', () => {
    let injector: TestBed;
    let service: PaymentMethodConfigService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaymentMethodConfig;
    let expectedResult: IPaymentMethodConfig | IPaymentMethodConfig[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PaymentMethodConfigService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new PaymentMethodConfig(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PaymentMethodConfig', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PaymentMethodConfig()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PaymentMethodConfig', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            value: 'BBBBBB',
            note: 'BBBBBB',
            enabled: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PaymentMethodConfig', () => {
        const returnedFromService = Object.assign(
          {
            key: 'BBBBBB',
            value: 'BBBBBB',
            note: 'BBBBBB',
            enabled: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PaymentMethodConfig', () => {
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
