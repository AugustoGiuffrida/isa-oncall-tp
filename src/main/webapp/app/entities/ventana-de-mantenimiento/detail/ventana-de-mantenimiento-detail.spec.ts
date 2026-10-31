import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { VentanaDeMantenimientoDetail } from './ventana-de-mantenimiento-detail';

describe('VentanaDeMantenimiento Management Detail Component', () => {
  let comp: VentanaDeMantenimientoDetail;
  let fixture: ComponentFixture<VentanaDeMantenimientoDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./ventana-de-mantenimiento-detail').then(m => m.VentanaDeMantenimientoDetail),
              resolve: { ventanaDeMantenimiento: () => of({ id: 31419 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VentanaDeMantenimientoDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load ventanaDeMantenimiento on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VentanaDeMantenimientoDetail);

      // THEN
      expect(instance.ventanaDeMantenimiento()).toEqual(expect.objectContaining({ id: 31419 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vitest.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
