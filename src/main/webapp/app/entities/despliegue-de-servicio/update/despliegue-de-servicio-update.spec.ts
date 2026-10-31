import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICambio } from 'app/entities/cambio/cambio.model';
import { CambioService } from 'app/entities/cambio/service/cambio.service';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { IDespliegueDeServicio } from '../despliegue-de-servicio.model';
import { DespliegueDeServicioService } from '../service/despliegue-de-servicio.service';

import { DespliegueDeServicioFormService } from './despliegue-de-servicio-form.service';
import { DespliegueDeServicioUpdate } from './despliegue-de-servicio-update';

describe('DespliegueDeServicio Management Update Component', () => {
  let comp: DespliegueDeServicioUpdate;
  let fixture: ComponentFixture<DespliegueDeServicioUpdate>;
  let activatedRoute: ActivatedRoute;
  let despliegueDeServicioFormService: DespliegueDeServicioFormService;
  let despliegueDeServicioService: DespliegueDeServicioService;
  let cambioService: CambioService;
  let servicioService: ServicioService;

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

    fixture = TestBed.createComponent(DespliegueDeServicioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    despliegueDeServicioFormService = TestBed.inject(DespliegueDeServicioFormService);
    despliegueDeServicioService = TestBed.inject(DespliegueDeServicioService);
    cambioService = TestBed.inject(CambioService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Cambio query and add missing value', () => {
      const despliegueDeServicio: IDespliegueDeServicio = { id: 32496 };
      const cambio: ICambio = { id: 11471 };
      despliegueDeServicio.cambio = cambio;

      const cambioCollection: ICambio[] = [{ id: 11471 }];
      vitest.spyOn(cambioService, 'query').mockReturnValue(of(new HttpResponse({ body: cambioCollection })));
      const additionalCambios = [cambio];
      const expectedCollection: ICambio[] = [...additionalCambios, ...cambioCollection];
      vitest.spyOn(cambioService, 'addCambioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ despliegueDeServicio });
      comp.ngOnInit();

      expect(cambioService.query).toHaveBeenCalled();
      expect(cambioService.addCambioToCollectionIfMissing).toHaveBeenCalledWith(
        cambioCollection,
        ...additionalCambios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.cambiosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Servicio query and add missing value', () => {
      const despliegueDeServicio: IDespliegueDeServicio = { id: 32496 };
      const servicio: IServicio = { id: 24037 };
      despliegueDeServicio.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ despliegueDeServicio });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const despliegueDeServicio: IDespliegueDeServicio = { id: 32496 };
      const cambio: ICambio = { id: 11471 };
      despliegueDeServicio.cambio = cambio;
      const servicio: IServicio = { id: 24037 };
      despliegueDeServicio.servicio = servicio;

      activatedRoute.data = of({ despliegueDeServicio });
      comp.ngOnInit();

      expect(comp.cambiosSharedCollection()).toContainEqual(cambio);
      expect(comp.serviciosSharedCollection()).toContainEqual(servicio);
      expect(comp.despliegueDeServicio).toEqual(despliegueDeServicio);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDespliegueDeServicio>();
      const despliegueDeServicio = { id: 6111 };
      vitest.spyOn(despliegueDeServicioFormService, 'getDespliegueDeServicio').mockReturnValue(despliegueDeServicio);
      vitest.spyOn(despliegueDeServicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ despliegueDeServicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(despliegueDeServicio);
      saveSubject.complete();

      // THEN
      expect(despliegueDeServicioFormService.getDespliegueDeServicio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(despliegueDeServicioService.update).toHaveBeenCalledWith(expect.objectContaining(despliegueDeServicio));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IDespliegueDeServicio>();
      const despliegueDeServicio = { id: 6111 };
      vitest.spyOn(despliegueDeServicioFormService, 'getDespliegueDeServicio').mockReturnValue({ id: null });
      vitest.spyOn(despliegueDeServicioService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ despliegueDeServicio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(despliegueDeServicio);
      saveSubject.complete();

      // THEN
      expect(despliegueDeServicioFormService.getDespliegueDeServicio).toHaveBeenCalled();
      expect(despliegueDeServicioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IDespliegueDeServicio>();
      const despliegueDeServicio = { id: 6111 };
      vitest.spyOn(despliegueDeServicioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ despliegueDeServicio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(despliegueDeServicioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCambio', () => {
      it('should forward to cambioService', () => {
        const entity = { id: 11471 };
        const entity2 = { id: 30774 };
        vitest.spyOn(cambioService, 'compareCambio');
        comp.compareCambio(entity, entity2);
        expect(cambioService.compareCambio).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareServicio', () => {
      it('should forward to servicioService', () => {
        const entity = { id: 24037 };
        const entity2 = { id: 644 };
        vitest.spyOn(servicioService, 'compareServicio');
        comp.compareServicio(entity, entity2);
        expect(servicioService.compareServicio).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
