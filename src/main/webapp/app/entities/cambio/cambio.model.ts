import dayjs from 'dayjs/esm';

import { IUser } from 'app/entities/user/user.model';

export interface ICambio {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
  ticketUrl?: string | null;
  creadoEn?: dayjs.Dayjs | null;
  autor?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewCambio = Omit<ICambio, 'id'> & { id: null };
