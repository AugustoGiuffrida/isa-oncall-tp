import { IServicio } from 'app/entities/servicio/servicio.model';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';

export interface IServicioEnVentana {
  id: number;
  nota?: string | null;
  ventana?: Pick<IVentanaDeMantenimiento, 'id' | 'nombre'> | null;
  servicio?: Pick<IServicio, 'id' | 'nombre'> | null;
}

export type NewServicioEnVentana = Omit<IServicioEnVentana, 'id'> & { id: null };
