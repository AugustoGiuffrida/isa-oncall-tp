import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ServicioEnVentanaDetail } from './servicio-en-ventana-detail';

describe('ServicioEnVentana Management Detail Component', () => {
  let comp: ServicioEnVentanaDetail;
  let fixture: ComponentFixture<ServicioEnVentanaDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./servicio-en-ventana-detail').then(m => m.ServicioEnVentanaDetail),
              resolve: { servicioEnVentana: () => of({ id: 3195 }) },
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
    fixture = TestBed.createComponent(ServicioEnVentanaDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load servicioEnVentana on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ServicioEnVentanaDetail);

      // THEN
      expect(instance.servicioEnVentana()).toEqual(expect.objectContaining({ id: 3195 }));
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
