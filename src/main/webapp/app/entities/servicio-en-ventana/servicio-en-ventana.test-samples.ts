import { IServicioEnVentana, NewServicioEnVentana } from './servicio-en-ventana.model';

export const sampleWithRequiredData: IServicioEnVentana = {
  id: 21331,
};

export const sampleWithPartialData: IServicioEnVentana = {
  id: 27649,
  nota: 'swelter sentimental',
};

export const sampleWithFullData: IServicioEnVentana = {
  id: 6395,
  nota: 'tragic brr quaintly',
};

export const sampleWithNewData: NewServicioEnVentana = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
