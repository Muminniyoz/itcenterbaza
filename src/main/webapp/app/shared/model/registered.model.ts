import { Moment } from 'moment';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface IRegistered {
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
  modifiedById?: number;
  courseId?: number;
}

export class Registered implements IRegistered {
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
    public modifiedById?: number,
    public courseId?: number
  ) {}
}
