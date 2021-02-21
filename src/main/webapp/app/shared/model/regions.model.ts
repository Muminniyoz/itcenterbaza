export interface IRegions {
  id?: number;
  title?: string;
  info?: string;
  googleUrl?: string;
  directorId?: number;
}

export class Regions implements IRegions {
  constructor(public id?: number, public title?: string, public info?: string, public googleUrl?: string, public directorId?: number) {}
}
