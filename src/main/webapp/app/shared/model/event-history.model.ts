import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { EventType } from 'app/shared/model/enumerations/event-type.model';

export interface IEventHistory {
  id?: number;
  type?: EventType;
  text?: string;
  time?: Moment;
  centerId?: number;
  userId?: number;
  openedUsers?: IUser[];
}

export class EventHistory implements IEventHistory {
  constructor(
    public id?: number,
    public type?: EventType,
    public text?: string,
    public time?: Moment,
    public centerId?: number,
    public userId?: number,
    public openedUsers?: IUser[]
  ) {}
}
