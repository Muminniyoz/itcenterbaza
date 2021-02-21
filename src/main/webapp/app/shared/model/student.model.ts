import { Moment } from 'moment';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IStudent {
  id?: number;
  firstName?: string;
  lastName?: string;
  middleName?: string;
  email?: string;
  dateOfBirth?: Moment;
  gender?: Gender;
  registerationDate?: Moment;
  telephone?: string;
  mobile?: string;
  thumbnailPhotoUrl?: string;
  fullPhotoUrl?: string;
  idNumber?: number;
  modifiedById?: number;
}

export class Student implements IStudent {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public middleName?: string,
    public email?: string,
    public dateOfBirth?: Moment,
    public gender?: Gender,
    public registerationDate?: Moment,
    public telephone?: string,
    public mobile?: string,
    public thumbnailPhotoUrl?: string,
    public fullPhotoUrl?: string,
    public idNumber?: number,
    public modifiedById?: number
  ) {}
}
