import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { VentanaDeMantenimientoService } from '../service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

import { VentanaDeMantenimientoFormService } from './ventana-de-mantenimiento-form.service';
import { VentanaDeMantenimientoUpdate } from './ventana-de-mantenimiento-update';

describe('VentanaDeMantenimiento Management Update Component', () => {
  let comp: VentanaDeMantenimientoUpdate;
  let fixture: ComponentFixture<VentanaDeMantenimientoUpdate>;
  let activatedRoute: ActivatedRoute;
  let ventanaDeMantenimientoFormService: VentanaDeMantenimientoFormService;
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

    fixture = TestBed.createComponent(VentanaDeMantenimientoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventanaDeMantenimientoFormService = TestBed.inject(VentanaDeMantenimientoFormService);
    ventanaDeMantenimientoService = TestBed.inject(VentanaDeMantenimientoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const ventanaDeMantenimiento: IVentanaDeMantenimiento = { id: 27219 };

      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      expect(comp.ventanaDeMantenimiento).toEqual(ventanaDeMantenimiento);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IVentanaDeMantenimiento>();
      const ventanaDeMantenimiento = { id: 31419 };
      vitest.spyOn(ventanaDeMantenimientoFormService, 'getVentanaDeMantenimiento').mockReturnValue(ventanaDeMantenimiento);
      vitest.spyOn(ventanaDeMantenimientoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(ventanaDeMantenimiento);
      saveSubject.complete();

      // THEN
      expect(ventanaDeMantenimientoFormService.getVentanaDeMantenimiento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventanaDeMantenimientoService.update).toHaveBeenCalledWith(expect.objectContaining(ventanaDeMantenimiento));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IVentanaDeMantenimiento>();
      const ventanaDeMantenimiento = { id: 31419 };
      vitest.spyOn(ventanaDeMantenimientoFormService, 'getVentanaDeMantenimiento').mockReturnValue({ id: null });
      vitest.spyOn(ventanaDeMantenimientoService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventanaDeMantenimiento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(ventanaDeMantenimiento);
      saveSubject.complete();

      // THEN
      expect(ventanaDeMantenimientoFormService.getVentanaDeMantenimiento).toHaveBeenCalled();
      expect(ventanaDeMantenimientoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IVentanaDeMantenimiento>();
      const ventanaDeMantenimiento = { id: 31419 };
      vitest.spyOn(ventanaDeMantenimientoService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventanaDeMantenimiento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventanaDeMantenimientoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
