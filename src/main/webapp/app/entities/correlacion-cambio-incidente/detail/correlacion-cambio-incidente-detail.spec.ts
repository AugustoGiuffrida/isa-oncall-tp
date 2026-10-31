import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CorrelacionCambioIncidenteDetail } from './correlacion-cambio-incidente-detail';

describe('CorrelacionCambioIncidente Management Detail Component', () => {
  let comp: CorrelacionCambioIncidenteDetail;
  let fixture: ComponentFixture<CorrelacionCambioIncidenteDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./correlacion-cambio-incidente-detail').then(m => m.CorrelacionCambioIncidenteDetail),
              resolve: { correlacionCambioIncidente: () => of({ id: 30799 }) },
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
    fixture = TestBed.createComponent(CorrelacionCambioIncidenteDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load correlacionCambioIncidente on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CorrelacionCambioIncidenteDetail);

      // THEN
      expect(instance.correlacionCambioIncidente()).toEqual(expect.objectContaining({ id: 30799 }));
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
