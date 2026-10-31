import dayjs from 'dayjs/esm';

import { IDespliegueDeServicio } from 'app/entities/despliegue-de-servicio/despliegue-de-servicio.model';
import { EstadoCorrelacion } from 'app/entities/enumerations/estado-correlacion.model';
import { OrigenCorrelacion } from 'app/entities/enumerations/origen-correlacion.model';
import { IIncidente } from 'app/entities/incidente/incidente.model';

export interface ICorrelacionCambioIncidente {
  id: number;
  origen?: keyof typeof OrigenCorrelacion | null;
  estado?: keyof typeof EstadoCorrelacion | null;
  detectadaEn?: dayjs.Dayjs | null;
  nota?: string | null;
  incidente?: Pick<IIncidente, 'id' | 'titulo'> | null;
  despliegue?: Pick<IDespliegueDeServicio, 'id' | 'version'> | null;
}

export type NewCorrelacionCambioIncidente = Omit<ICorrelacionCambioIncidente, 'id'> & { id: null };
