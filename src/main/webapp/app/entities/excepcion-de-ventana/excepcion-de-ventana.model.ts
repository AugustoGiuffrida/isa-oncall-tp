import dayjs from 'dayjs/esm';

import { TipoExcepcionDeVentana } from 'app/entities/enumerations/tipo-excepcion-de-ventana.model';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';

export interface IExcepcionDeVentana {
  id: number;
  fecha?: dayjs.Dayjs | null;
  tipo?: keyof typeof TipoExcepcionDeVentana | null;
  nuevoDesde?: dayjs.Dayjs | null;
  nuevoHasta?: dayjs.Dayjs | null;
  motivo?: string | null;
  ventana?: Pick<IVentanaDeMantenimiento, 'id' | 'nombre'> | null;
}

export type NewExcepcionDeVentana = Omit<IExcepcionDeVentana, 'id'> & { id: null };
