import dayjs from 'dayjs/esm';

import { Recurrencia } from 'app/entities/enumerations/recurrencia.model';

export interface IVentanaDeMantenimiento {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  desde?: dayjs.Dayjs | null;
  hasta?: dayjs.Dayjs | null;
  recurrencia?: keyof typeof Recurrencia | null;
  zonaHoraria?: string | null;
  repetirHasta?: dayjs.Dayjs | null;
  activa?: boolean | null;
}

export type NewVentanaDeMantenimiento = Omit<IVentanaDeMantenimiento, 'id'> & { id: null };
