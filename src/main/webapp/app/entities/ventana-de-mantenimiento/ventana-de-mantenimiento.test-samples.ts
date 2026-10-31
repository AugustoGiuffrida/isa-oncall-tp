import dayjs from 'dayjs/esm';

import { IVentanaDeMantenimiento, NewVentanaDeMantenimiento } from './ventana-de-mantenimiento.model';

export const sampleWithRequiredData: IVentanaDeMantenimiento = {
  id: 27630,
  nombre: 'yet debit',
  desde: dayjs('2026-10-31T11:59'),
  hasta: dayjs('2026-10-30T19:45'),
  recurrencia: 'UNICA',
  zonaHoraria: 'whereas',
  activa: true,
};

export const sampleWithPartialData: IVentanaDeMantenimiento = {
  id: 11027,
  nombre: 'fathom which huzzah',
  desde: dayjs('2026-10-30T22:14'),
  hasta: dayjs('2026-10-31T12:09'),
  recurrencia: 'UNICA',
  zonaHoraria: 'in',
  activa: false,
};

export const sampleWithFullData: IVentanaDeMantenimiento = {
  id: 21275,
  nombre: 'ha while',
  descripcion: 'where aha engage',
  desde: dayjs('2026-10-31T10:42'),
  hasta: dayjs('2026-10-31T01:58'),
  recurrencia: 'DIARIA',
  zonaHoraria: 'aha cheap including',
  repetirHasta: dayjs('2026-10-30'),
  activa: false,
};

export const sampleWithNewData: NewVentanaDeMantenimiento = {
  nombre: 'as yawningly excluding',
  desde: dayjs('2026-10-31T05:32'),
  hasta: dayjs('2026-10-31T01:51'),
  recurrencia: 'UNICA',
  zonaHoraria: 'whirlwind',
  activa: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
