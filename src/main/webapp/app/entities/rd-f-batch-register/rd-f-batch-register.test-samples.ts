import dayjs from 'dayjs/esm';

import { IRdFBatchRegister, NewRdFBatchRegister } from './rd-f-batch-register.model';

export const sampleWithRequiredData: IRdFBatchRegister = {
  id: 88427,
  workflowId: 'Belarus Maryland',
  dbname: 'Federation',
  feedId: 'schemas',
  func: 'Sleek',
  reqDatetime: 'Buckinghamshir',
  execUser: 'Sleek time-frame out-of-the-box',
  systemIds: '../fake-data/blob/hipster.txt',
  mode: 'Savings Connecticut ',
  done: 'f',
  createdTimestamp: dayjs('2023-03-04T20:10'),
  updatedTimestamp: dayjs('2023-03-04T14:54'),
};

export const sampleWithPartialData: IRdFBatchRegister = {
  id: 80643,
  workflowId: 'Chair benchmark global',
  dbname: 'Village New copying',
  feedId: 'Agent',
  func: '1080p Rubber primary',
  reqDatetime: 'Cheese users',
  execUser: 'Cambridgeshire index',
  systemIds: '../fake-data/blob/hipster.txt',
  mode: 'reintermediate',
  done: 'A',
  createdTimestamp: dayjs('2023-03-05T10:34'),
  updatedTimestamp: dayjs('2023-03-04T12:55'),
};

export const sampleWithFullData: IRdFBatchRegister = {
  id: 75457,
  workflowId: 'North',
  dbname: 'deposit wireless Garden',
  feedId: 'grey deposit',
  func: 'Representative Alaba',
  reqDatetime: 'applications',
  execUser: 'panel',
  systemIds: '../fake-data/blob/hipster.txt',
  mode: 'Checking leverage bl',
  done: 'C',
  createdTimestamp: dayjs('2023-03-05T08:20'),
  updatedTimestamp: dayjs('2023-03-05T07:16'),
};

export const sampleWithNewData: NewRdFBatchRegister = {
  workflowId: 'value-added',
  dbname: 'intelligence',
  feedId: 'Integration composite haptic',
  func: 'Multi-lateral system',
  reqDatetime: 'bypass salmon ',
  execUser: 'full-range',
  systemIds: '../fake-data/blob/hipster.txt',
  mode: 'target Kids transiti',
  done: 'c',
  createdTimestamp: dayjs('2023-03-04T22:27'),
  updatedTimestamp: dayjs('2023-03-04T15:28'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
