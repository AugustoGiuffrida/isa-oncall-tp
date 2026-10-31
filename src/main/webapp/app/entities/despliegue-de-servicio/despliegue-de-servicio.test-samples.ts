import dayjs from 'dayjs/esm';

import { IDespliegueDeServicio, NewDespliegueDeServicio } from './despliegue-de-servicio.model';

export const sampleWithRequiredData: IDespliegueDeServicio = {
  id: 25352,
  version: 'inasmuch',
  desplegadoEn: dayjs('2026-10-31T13:10'),
};

export const sampleWithPartialData: IDespliegueDeServicio = {
  id: 11255,
  version: 'restructure riser',
  desplegadoEn: dayjs('2026-10-31T01:45'),
};

export const sampleWithFullData: IDespliegueDeServicio = {
  id: 5871,
  version: 'frantically whose ick',
  desplegadoEn: dayjs('2026-10-31T03:30'),
};

export const sampleWithNewData: NewDespliegueDeServicio = {
  version: 'phew',
  desplegadoEn: dayjs('2026-10-30T14:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
