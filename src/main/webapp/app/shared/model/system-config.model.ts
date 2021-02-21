export interface ISystemConfig {
  id?: number;
  key?: string;
  value?: string;
  note?: string;
  enabled?: boolean;
}

export class SystemConfig implements ISystemConfig {
  constructor(public id?: number, public key?: string, public value?: string, public note?: string, public enabled?: boolean) {
    this.enabled = this.enabled || false;
  }
}
