import { Moment } from 'moment';
import { IRegistered } from 'app/shared/model/registered.model';
import { CourseStatus } from 'app/shared/model/enumerations/course-status.model';

export interface ICourse {
  id?: number;
  title?: string;
  price?: number;
  startDate?: Moment;
  status?: CourseStatus;
  duration?: number;
  planFileContentType?: string;
  planFile?: any;
  registereds?: IRegistered[];
  teacherId?: number;
  centerId?: number;
  skillId?: number;
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public title?: string,
    public price?: number,
    public startDate?: Moment,
    public status?: CourseStatus,
    public duration?: number,
    public planFileContentType?: string,
    public planFile?: any,
    public registereds?: IRegistered[],
    public teacherId?: number,
    public centerId?: number,
    public skillId?: number
  ) {}
}
