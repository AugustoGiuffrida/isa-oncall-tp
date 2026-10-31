import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServicioService } from 'app/entities/servicio/service/servicio.service';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { VentanaDeMantenimientoService } from 'app/entities/ventana-de-mantenimiento/service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';
import { ServicioEnVentanaService } from '../service/servicio-en-ventana.service';
import { IServicioEnVentana } from '../servicio-en-ventana.model';

import { ServicioEnVentanaFormService } from './servicio-en-ventana-form.service';
import { ServicioEnVentanaUpdate } from './servicio-en-ventana-update';

describe('ServicioEnVentana Management Update Component', () => {
  let comp: ServicioEnVentanaUpdate;
  let fixture: ComponentFixture<ServicioEnVentanaUpdate>;
  let activatedRoute: ActivatedRoute;
  let servicioEnVentanaFormService: ServicioEnVentanaFormService;
  let servicioEnVentanaService: ServicioEnVentanaService;
  let ventanaDeMantenimientoService: VentanaDeMantenimientoService;
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

    fixture = TestBed.createComponent(ServicioEnVentanaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    servicioEnVentanaFormService = TestBed.inject(ServicioEnVentanaFormService);
    servicioEnVentanaService = TestBed.inject(ServicioEnVentanaService);
    ventanaDeMantenimientoService = TestBed.inject(VentanaDeMantenimientoService);
    servicioService = TestBed.inject(ServicioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call VentanaDeMantenimiento query and add missing value', () => {
      const servicioEnVentana: IServicioEnVentana = { id: 21922 };
      const ventana: IVentanaDeMantenimiento = { id: 31419 };
      servicioEnVentana.ventana = ventana;

      const ventanaDeMantenimientoCollection: IVentanaDeMantenimiento[] = [{ id: 31419 }];
      vitest
        .spyOn(ventanaDeMantenimientoService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: ventanaDeMantenimientoCollection })));
      const additionalVentanaDeMantenimientos = [ventana];
      const expectedCollection: IVentanaDeMantenimiento[] = [...additionalVentanaDeMantenimientos, ...ventanaDeMantenimientoCollection];
      vitest.spyOn(ventanaDeMantenimientoService, 'addVentanaDeMantenimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicioEnVentana });
      comp.ngOnInit();

      expect(ventanaDeMantenimientoService.query).toHaveBeenCalled();
      expect(ventanaDeMantenimientoService.addVentanaDeMantenimientoToCollectionIfMissing).toHaveBeenCalledWith(
        ventanaDeMantenimientoCollection,
        ...additionalVentanaDeMantenimientos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ventanaDeMantenimientosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Servicio query and add missing value', () => {
      const servicioEnVentana: IServicioEnVentana = { id: 21922 };
      const servicio: IServicio = { id: 24037 };
      servicioEnVentana.servicio = servicio;

      const servicioCollection: IServicio[] = [{ id: 24037 }];
      vitest.spyOn(servicioService, 'query').mockReturnValue(of(new HttpResponse({ body: servicioCollection })));
      const additionalServicios = [servicio];
      const expectedCollection: IServicio[] = [...additionalServicios, ...servicioCollection];
      vitest.spyOn(servicioService, 'addServicioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ servicioEnVentana });
      comp.ngOnInit();

      expect(servicioService.query).toHaveBeenCalled();
      expect(servicioService.addServicioToCollectionIfMissing).toHaveBeenCalledWith(
        servicioCollection,
        ...additionalServicios.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviciosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const servicioEnVentana: IServicioEnVentana = { id: 21922 };
      const ventana: IVentanaDeMantenimiento = { id: 31419 };
      servicioEnVentana.ventana = ventana;
      const servicio: IServicio = { id: 24037 };
      servicioEnVentana.servicio = servicio;

      activatedRoute.data = of({ servicioEnVentana });
      comp.ngOnInit();

      expect(comp.ventanaDeMantenimientosSharedCollection()).toContainEqual(ventana);
      expect(comp.serviciosSharedCollection()).toContainEqual(servicio);
      expect(comp.servicioEnVentana).toEqual(servicioEnVentana);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioEnVentana>();
      const servicioEnVentana = { id: 3195 };
      vitest.spyOn(servicioEnVentanaFormService, 'getServicioEnVentana').mockReturnValue(servicioEnVentana);
      vitest.spyOn(servicioEnVentanaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioEnVentana });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicioEnVentana);
      saveSubject.complete();

      // THEN
      expect(servicioEnVentanaFormService.getServicioEnVentana).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(servicioEnVentanaService.update).toHaveBeenCalledWith(expect.objectContaining(servicioEnVentana));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioEnVentana>();
      const servicioEnVentana = { id: 3195 };
      vitest.spyOn(servicioEnVentanaFormService, 'getServicioEnVentana').mockReturnValue({ id: null });
      vitest.spyOn(servicioEnVentanaService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioEnVentana: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(servicioEnVentana);
      saveSubject.complete();

      // THEN
      expect(servicioEnVentanaFormService.getServicioEnVentana).toHaveBeenCalled();
      expect(servicioEnVentanaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IServicioEnVentana>();
      const servicioEnVentana = { id: 3195 };
      vitest.spyOn(servicioEnVentanaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ servicioEnVentana });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(servicioEnVentanaService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVentanaDeMantenimiento', () => {
      it('should forward to ventanaDeMantenimientoService', () => {
        const entity = { id: 31419 };
        const entity2 = { id: 27219 };
        vitest.spyOn(ventanaDeMantenimientoService, 'compareVentanaDeMantenimiento');
        comp.compareVentanaDeMantenimiento(entity, entity2);
        expect(ventanaDeMantenimientoService.compareVentanaDeMantenimiento).toHaveBeenCalledWith(entity, entity2);
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
