import dayjs from 'dayjs/esm';

import { IExcepcionDeVentana, NewExcepcionDeVentana } from './excepcion-de-ventana.model';

export const sampleWithRequiredData: IExcepcionDeVentana = {
  id: 25752,
  fecha: dayjs('2026-10-31'),
  tipo: 'REPROGRAMADA',
};

export const sampleWithPartialData: IExcepcionDeVentana = {
  id: 3919,
  fecha: dayjs('2026-10-31'),
  tipo: 'CANCELADA',
  nuevoHasta: dayjs('2026-10-31T04:03'),
};

export const sampleWithFullData: IExcepcionDeVentana = {
  id: 16446,
  fecha: dayjs('2026-10-30'),
  tipo: 'REPROGRAMADA',
  nuevoDesde: dayjs('2026-10-30T15:31'),
  nuevoHasta: dayjs('2026-10-30T17:33'),
  motivo: 'faithfully pfft',
};

export const sampleWithNewData: NewExcepcionDeVentana = {
  fecha: dayjs('2026-10-30'),
  tipo: 'CANCELADA',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
