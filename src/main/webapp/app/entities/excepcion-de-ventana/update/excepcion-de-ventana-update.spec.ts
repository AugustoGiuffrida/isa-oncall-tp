import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { VentanaDeMantenimientoService } from 'app/entities/ventana-de-mantenimiento/service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from 'app/entities/ventana-de-mantenimiento/ventana-de-mantenimiento.model';
import { IExcepcionDeVentana } from '../excepcion-de-ventana.model';
import { ExcepcionDeVentanaService } from '../service/excepcion-de-ventana.service';

import { ExcepcionDeVentanaFormService } from './excepcion-de-ventana-form.service';
import { ExcepcionDeVentanaUpdate } from './excepcion-de-ventana-update';

describe('ExcepcionDeVentana Management Update Component', () => {
  let comp: ExcepcionDeVentanaUpdate;
  let fixture: ComponentFixture<ExcepcionDeVentanaUpdate>;
  let activatedRoute: ActivatedRoute;
  let excepcionDeVentanaFormService: ExcepcionDeVentanaFormService;
  let excepcionDeVentanaService: ExcepcionDeVentanaService;
  let ventanaDeMantenimientoService: VentanaDeMantenimientoService;

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

    fixture = TestBed.createComponent(ExcepcionDeVentanaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    excepcionDeVentanaFormService = TestBed.inject(ExcepcionDeVentanaFormService);
    excepcionDeVentanaService = TestBed.inject(ExcepcionDeVentanaService);
    ventanaDeMantenimientoService = TestBed.inject(VentanaDeMantenimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call VentanaDeMantenimiento query and add missing value', () => {
      const excepcionDeVentana: IExcepcionDeVentana = { id: 32009 };
      const ventana: IVentanaDeMantenimiento = { id: 31419 };
      excepcionDeVentana.ventana = ventana;

      const ventanaDeMantenimientoCollection: IVentanaDeMantenimiento[] = [{ id: 31419 }];
      vitest
        .spyOn(ventanaDeMantenimientoService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: ventanaDeMantenimientoCollection })));
      const additionalVentanaDeMantenimientos = [ventana];
      const expectedCollection: IVentanaDeMantenimiento[] = [...additionalVentanaDeMantenimientos, ...ventanaDeMantenimientoCollection];
      vitest.spyOn(ventanaDeMantenimientoService, 'addVentanaDeMantenimientoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ excepcionDeVentana });
      comp.ngOnInit();

      expect(ventanaDeMantenimientoService.query).toHaveBeenCalled();
      expect(ventanaDeMantenimientoService.addVentanaDeMantenimientoToCollectionIfMissing).toHaveBeenCalledWith(
        ventanaDeMantenimientoCollection,
        ...additionalVentanaDeMantenimientos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ventanaDeMantenimientosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const excepcionDeVentana: IExcepcionDeVentana = { id: 32009 };
      const ventana: IVentanaDeMantenimiento = { id: 31419 };
      excepcionDeVentana.ventana = ventana;

      activatedRoute.data = of({ excepcionDeVentana });
      comp.ngOnInit();

      expect(comp.ventanaDeMantenimientosSharedCollection()).toContainEqual(ventana);
      expect(comp.excepcionDeVentana).toEqual(excepcionDeVentana);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IExcepcionDeVentana>();
      const excepcionDeVentana = { id: 8669 };
      vitest.spyOn(excepcionDeVentanaFormService, 'getExcepcionDeVentana').mockReturnValue(excepcionDeVentana);
      vitest.spyOn(excepcionDeVentanaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ excepcionDeVentana });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(excepcionDeVentana);
      saveSubject.complete();

      // THEN
      expect(excepcionDeVentanaFormService.getExcepcionDeVentana).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(excepcionDeVentanaService.update).toHaveBeenCalledWith(expect.objectContaining(excepcionDeVentana));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IExcepcionDeVentana>();
      const excepcionDeVentana = { id: 8669 };
      vitest.spyOn(excepcionDeVentanaFormService, 'getExcepcionDeVentana').mockReturnValue({ id: null });
      vitest.spyOn(excepcionDeVentanaService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ excepcionDeVentana: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(excepcionDeVentana);
      saveSubject.complete();

      // THEN
      expect(excepcionDeVentanaFormService.getExcepcionDeVentana).toHaveBeenCalled();
      expect(excepcionDeVentanaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IExcepcionDeVentana>();
      const excepcionDeVentana = { id: 8669 };
      vitest.spyOn(excepcionDeVentanaService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ excepcionDeVentana });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(excepcionDeVentanaService.update).toHaveBeenCalled();
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
  });
});
