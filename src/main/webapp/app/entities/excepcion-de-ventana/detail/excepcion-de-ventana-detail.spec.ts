import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ExcepcionDeVentanaDetail } from './excepcion-de-ventana-detail';

describe('ExcepcionDeVentana Management Detail Component', () => {
  let comp: ExcepcionDeVentanaDetail;
  let fixture: ComponentFixture<ExcepcionDeVentanaDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./excepcion-de-ventana-detail').then(m => m.ExcepcionDeVentanaDetail),
              resolve: { excepcionDeVentana: () => of({ id: 8669 }) },
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
    fixture = TestBed.createComponent(ExcepcionDeVentanaDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load excepcionDeVentana on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ExcepcionDeVentanaDetail);

      // THEN
      expect(instance.excepcionDeVentana()).toEqual(expect.objectContaining({ id: 8669 }));
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
