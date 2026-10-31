import dayjs from 'dayjs/esm';

import { ICorrelacionCambioIncidente, NewCorrelacionCambioIncidente } from './correlacion-cambio-incidente.model';

export const sampleWithRequiredData: ICorrelacionCambioIncidente = {
  id: 7579,
  origen: 'AUTOMATICA',
  estado: 'SUGERIDA',
  detectadaEn: dayjs('2026-10-30T15:05'),
};

export const sampleWithPartialData: ICorrelacionCambioIncidente = {
  id: 14571,
  origen: 'MANUAL',
  estado: 'DESCARTADA',
  detectadaEn: dayjs('2026-10-30T20:42'),
  nota: 'darling',
};

export const sampleWithFullData: ICorrelacionCambioIncidente = {
  id: 4541,
  origen: 'MANUAL',
  estado: 'DESCARTADA',
  detectadaEn: dayjs('2026-10-30T14:09'),
  nota: 'while times',
};

export const sampleWithNewData: NewCorrelacionCambioIncidente = {
  origen: 'AUTOMATICA',
  estado: 'CONFIRMADA',
  detectadaEn: dayjs('2026-10-31T09:03'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
