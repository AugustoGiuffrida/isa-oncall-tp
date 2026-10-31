import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IDespliegueDeServicio } from 'app/entities/despliegue-de-servicio/despliegue-de-servicio.model';
import { DespliegueDeServicioService } from 'app/entities/despliegue-de-servicio/service/despliegue-de-servicio.service';
import { IIncidente } from 'app/entities/incidente/incidente.model';
import { IncidenteService } from 'app/entities/incidente/service/incidente.service';
import { ICorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';
import { CorrelacionCambioIncidenteService } from '../service/correlacion-cambio-incidente.service';

import { CorrelacionCambioIncidenteFormService } from './correlacion-cambio-incidente-form.service';
import { CorrelacionCambioIncidenteUpdate } from './correlacion-cambio-incidente-update';

describe('CorrelacionCambioIncidente Management Update Component', () => {
  let comp: CorrelacionCambioIncidenteUpdate;
  let fixture: ComponentFixture<CorrelacionCambioIncidenteUpdate>;
  let activatedRoute: ActivatedRoute;
  let correlacionCambioIncidenteFormService: CorrelacionCambioIncidenteFormService;
  let correlacionCambioIncidenteService: CorrelacionCambioIncidenteService;
  let incidenteService: IncidenteService;
  let despliegueDeServicioService: DespliegueDeServicioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(CorrelacionCambioIncidenteUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    correlacionCambioIncidenteFormService = TestBed.inject(CorrelacionCambioIncidenteFormService);
    correlacionCambioIncidenteService = TestBed.inject(CorrelacionCambioIncidenteService);
    incidenteService = TestBed.inject(IncidenteService);
    despliegueDeServicioService = TestBed.inject(DespliegueDeServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Incidente query and add missing value', () => {
      const correlacionCambioIncidente: ICorrelacionCambioIncidente = { id: 24177 };
      const incidente: IIncidente = { id: 31968 };
      correlacionCambioIncidente.incidente = incidente;

      const incidenteCollection: IIncidente[] = [{ id: 31968 }];
      vitest.spyOn(incidenteService, 'query').mockReturnValue(of(new HttpResponse({ body: incidenteCollection })));
      const additionalIncidentes = [incidente];
      const expectedCollection: IIncidente[] = [...additionalIncidentes, ...incidenteCollection];
      vitest.spyOn(incidenteService, 'addIncidenteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ correlacionCambioIncidente });
      comp.ngOnInit();

      expect(incidenteService.query).toHaveBeenCalled();
      expect(incidenteService.addIncidenteToCollectionIfMissing).toHaveBeenCalledWith(
        incidenteCollection,
        ...additionalIncidentes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.incidentesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call DespliegueDeServicio query and add missing value', () => {
      const correlacionCambioIncidente: ICorrelacionCambioIncidente = { id: 24177 };
      const despliegue: IDespliegueDeServicio = { id: 6111 };
      correlacionCambioIncidente.despliegue = despliegue;

      const despliegueDeServicioCollection: IDespliegueDeServicio[] = [{ id: 6111 }];
      vitest.spyOn(despliegueDeServicioService, 'query').mockReturnValue(of(new HttpResponse({ body: despliegueDeServicioCollection })));
      const additionalDespliegueDeServicios = [despliegue];
      const expectedCollection: IDespliegueDeServicio[] = [...additionalDespliegueDeServicios, ...despliegueDeServicioCollection];
      vitest.spyOn(despliegueDeServicioService, 'addDespliegueDeServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ correlacionCambioIncidente });
      comp.ngOnInit();

      expect(despliegueDeServicioService.query).toHaveBeenCalled();
      expect(despliegueDeServicioService.addDespliegueDeServicioToCollectionIfMissing).toHaveBeenCalledWith(
        despliegueDeServicioCollection,
        ...additionalDespliegueDeServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.despliegueDeServiciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const correlacionCambioIncidente: ICorrelacionCambioIncidente = { id: 24177 };
      const incidente: IIncidente = { id: 31968 };
      correlacionCambioIncidente.incidente = incidente;
      const despliegue: IDespliegueDeServicio = { id: 6111 };
      correlacionCambioIncidente.despliegue = despliegue;

      activatedRoute.data = of({ correlacionCambioIncidente });
      comp.ngOnInit();

      expect(comp.incidentesSharedCollection()).toContainEqual(incidente);
      expect(comp.despliegueDeServiciosSharedCollection()).toContainEqual(despliegue);
      expect(comp.correlacionCambioIncidente).toEqual(correlacionCambioIncidente);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICorrelacionCambioIncidente>();
      const correlacionCambioIncidente = { id: 30799 };
      vitest.spyOn(correlacionCambioIncidenteFormService, 'getCorrelacionCambioIncidente').mockReturnValue(correlacionCambioIncidente);
      vitest.spyOn(correlacionCambioIncidenteService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correlacionCambioIncidente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(correlacionCambioIncidente);
      saveSubject.complete();

      // THEN
      expect(correlacionCambioIncidenteFormService.getCorrelacionCambioIncidente).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(correlacionCambioIncidenteService.update).toHaveBeenCalledWith(expect.objectContaining(correlacionCambioIncidente));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICorrelacionCambioIncidente>();
      const correlacionCambioIncidente = { id: 30799 };
      vitest.spyOn(correlacionCambioIncidenteFormService, 'getCorrelacionCambioIncidente').mockReturnValue({ id: null });
      vitest.spyOn(correlacionCambioIncidenteService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correlacionCambioIncidente: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(correlacionCambioIncidente);
      saveSubject.complete();

      // THEN
      expect(correlacionCambioIncidenteFormService.getCorrelacionCambioIncidente).toHaveBeenCalled();
      expect(correlacionCambioIncidenteService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICorrelacionCambioIncidente>();
      const correlacionCambioIncidente = { id: 30799 };
      vitest.spyOn(correlacionCambioIncidenteService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ correlacionCambioIncidente });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(correlacionCambioIncidenteService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIncidente', () => {
      it('should forward to incidenteService', () => {
        const entity = { id: 31968 };
        const entity2 = { id: 10195 };
        vitest.spyOn(incidenteService, 'compareIncidente');
        comp.compareIncidente(entity, entity2);
        expect(incidenteService.compareIncidente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDespliegueDeServicio', () => {
      it('should forward to despliegueDeServicioService', () => {
        const entity = { id: 6111 };
        const entity2 = { id: 32496 };
        vitest.spyOn(despliegueDeServicioService, 'compareDespliegueDeServicio');
        comp.compareDespliegueDeServicio(entity, entity2);
        expect(despliegueDeServicioService.compareDespliegueDeServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
