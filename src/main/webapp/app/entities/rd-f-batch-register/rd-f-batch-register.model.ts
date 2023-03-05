import dayjs from 'dayjs/esm';

export interface IRdFBatchRegister {
  id: number;
  workflowId?: string | null;
  dbname?: string | null;
  feedId?: string | null;
  func?: string | null;
  reqDatetime?: string | null;
  execUser?: string | null;
  systemIds?: string | null;
  mode?: string | null;
  done?: string | null;
  createdTimestamp?: dayjs.Dayjs | null;
  updatedTimestamp?: dayjs.Dayjs | null;
}

export type NewRdFBatchRegister = Omit<IRdFBatchRegister, 'id'> & { id: null };
