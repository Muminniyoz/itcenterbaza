import { Moment } from 'moment';
import { ParticipantStatus } from 'app/shared/model/enumerations/participant-status.model';

export interface IParticipant {
  id?: number;
  startingDate?: Moment;
  active?: boolean;
  status?: ParticipantStatus;
  contractNumber?: string;
  studentId?: number;
  courseId?: number;
}

export class Participant implements IParticipant {
  constructor(
    public id?: number,
    public startingDate?: Moment,
    public active?: boolean,
    public status?: ParticipantStatus,
    public contractNumber?: string,
    public studentId?: number,
    public courseId?: number
  ) {
    this.active = this.active || false;
  }
}
