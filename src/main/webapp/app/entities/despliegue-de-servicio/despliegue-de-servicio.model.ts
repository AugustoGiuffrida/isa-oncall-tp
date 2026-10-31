import dayjs from 'dayjs/esm';

import { ICambio } from 'app/entities/cambio/cambio.model';
import { IServicio } from 'app/entities/servicio/servicio.model';

export interface IDespliegueDeServicio {
  id: number;
  version?: string | null;
  desplegadoEn?: dayjs.Dayjs | null;
  cambio?: Pick<ICambio, 'id' | 'titulo'> | null;
  servicio?: Pick<IServicio, 'id' | 'nombre'> | null;
}

export type NewDespliegueDeServicio = Omit<IDespliegueDeServicio, 'id'> & { id: null };
