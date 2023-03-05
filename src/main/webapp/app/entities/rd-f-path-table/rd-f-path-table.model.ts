export interface IRdFPathTable {
  id: number;
  path?: string | null;
  contentsXslt?: string | null;
  description?: string | null;
}

export type NewRdFPathTable = Omit<IRdFPathTable, 'id'> & { id: null };
