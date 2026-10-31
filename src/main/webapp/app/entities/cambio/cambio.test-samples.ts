import dayjs from 'dayjs/esm';

import { ICambio, NewCambio } from './cambio.model';

export const sampleWithRequiredData: ICambio = {
  id: 28701,
  titulo: 'bleakly',
  creadoEn: dayjs('2026-10-30T15:08'),
};

export const sampleWithPartialData: ICambio = {
  id: 18787,
  titulo: 'someplace ripe shakily',
  ticketUrl: 'heavy video mid',
  creadoEn: dayjs('2026-10-31T03:16'),
};

export const sampleWithFullData: ICambio = {
  id: 498,
  titulo: 'phooey imaginative shocked',
  descripcion: 'but hoarse',
  ticketUrl: 'loyally',
  creadoEn: dayjs('2026-10-31T10:15'),
};

export const sampleWithNewData: NewCambio = {
  titulo: 'yuck scale',
  creadoEn: dayjs('2026-10-31T11:22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
