import { ITeacher } from 'app/shared/model/teacher.model';

export interface ISkill {
  id?: number;
  titleUz?: string;
  titleRu?: string;
  titleEn?: string;
  about?: any;
  planFileContentType?: string;
  planFile?: any;
  teachers?: ITeacher[];
}

export class Skill implements ISkill {
  constructor(
    public id?: number,
    public titleUz?: string,
    public titleRu?: string,
    public titleEn?: string,
    public about?: any,
    public planFileContentType?: string,
    public planFile?: any,
    public teachers?: ITeacher[]
  ) {}
}
