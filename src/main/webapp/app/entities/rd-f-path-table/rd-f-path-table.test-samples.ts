import { IRdFPathTable, NewRdFPathTable } from './rd-f-path-table.model';

export const sampleWithRequiredData: IRdFPathTable = {
  id: 45676,
};

export const sampleWithPartialData: IRdFPathTable = {
  id: 67346,
  description: 'and',
};

export const sampleWithFullData: IRdFPathTable = {
  id: 59084,
  path: 'Bedfordshire',
  contentsXslt: 'deposit',
  description: 'Bedfordshire Wooden',
};

export const sampleWithNewData: NewRdFPathTable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
