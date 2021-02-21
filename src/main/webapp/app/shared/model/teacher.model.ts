import { Moment } from 'moment';
import { ISkill } from 'app/shared/model/skill.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';

export interface ITeacher {
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
  fullPhotoUrl?: string;
  active?: boolean;
  key?: string;
  about?: any;
  portfolia?: any;
  infoContentType?: string;
  info?: any;
  leaveDate?: Moment;
  isShowingHome?: boolean;
  imageContentType?: string;
  image?: any;
  modifiedById?: number;
  userId?: number;
  skills?: ISkill[];
}

export class Teacher implements ITeacher {
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
    public fullPhotoUrl?: string,
    public active?: boolean,
    public key?: string,
    public about?: any,
    public portfolia?: any,
    public infoContentType?: string,
    public info?: any,
    public leaveDate?: Moment,
    public isShowingHome?: boolean,
    public imageContentType?: string,
    public image?: any,
    public modifiedById?: number,
    public userId?: number,
    public skills?: ISkill[]
  ) {
    this.active = this.active || false;
    this.isShowingHome = this.isShowingHome || false;
  }
}
