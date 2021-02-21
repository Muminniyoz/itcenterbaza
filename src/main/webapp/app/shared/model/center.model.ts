import { Moment } from 'moment';

export interface ICenter {
  id?: number;
  title?: string;
  info?: string;
  startDate?: Moment;
  googleMapUrl?: string;
  modifiedById?: number;
  regionsId?: number;
  managerId?: number;
}

export class Center implements ICenter {
  constructor(
    public id?: number,
    public title?: string,
    public info?: string,
    public startDate?: Moment,
    public googleMapUrl?: string,
    public modifiedById?: number,
    public regionsId?: number,
    public managerId?: number
  ) {}
}
