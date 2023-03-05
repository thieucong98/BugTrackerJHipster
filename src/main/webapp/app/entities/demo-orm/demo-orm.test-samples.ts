import dayjs from 'dayjs/esm';

import { IDemoOrm, NewDemoOrm } from './demo-orm.model';

export const sampleWithRequiredData: IDemoOrm = {
  id: 88088,
  codeId: 'Focused',
  codeName: "d'Ivoire global",
  itemKey: 'even-keeled bypass reboot',
};

export const sampleWithPartialData: IDemoOrm = {
  id: 84123,
  codeId: 'Product So',
  codeName: 'embrace dot-com',
  itemKey: 'B2C primary Home',
  itemValueJa: '1080p',
  parentItemKeyBackup: 'application',
  updatedTimestamp: dayjs('2023-03-04T21:04'),
};

export const sampleWithFullData: IDemoOrm = {
  id: 32632,
  codeId: 'compress V',
  codeName: 'Borders optical',
  itemKey: 'ADP optical USB',
  itemValueJa: 'communities Indiana Dynamic',
  itemValueEn: 'copying Rustic technologies',
  itemValuePair: 'Bedfordshire high-level',
  parentCodeId: 'Bangl',
  parentItemKey: 'withdrawal Nebraska Borders',
  parentItemKeyBackup: 'Rhode Loan Consultant',
  parentItemKeyNew: 'Integrated',
  createdTimestamp: dayjs('2023-03-04T18:18'),
  updatedTimestamp: dayjs('2023-03-05T03:16'),
};

export const sampleWithNewData: NewDemoOrm = {
  codeId: 'Awesome SA',
  codeName: 'Chips',
  itemKey: 'Market Mountain',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
