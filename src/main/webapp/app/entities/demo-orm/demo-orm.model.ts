import dayjs from 'dayjs/esm';

export interface IDemoOrm {
  id: number;
  codeId?: string | null;
  codeName?: string | null;
  itemKey?: string | null;
  itemValueJa?: string | null;
  itemValueEn?: string | null;
  itemValuePair?: string | null;
  parentCodeId?: string | null;
  parentItemKey?: string | null;
  parentItemKeyBackup?: string | null;
  parentItemKeyNew?: string | null;
  createdTimestamp?: dayjs.Dayjs | null;
  updatedTimestamp?: dayjs.Dayjs | null;
}

export type NewDemoOrm = Omit<IDemoOrm, 'id'> & { id: null };
