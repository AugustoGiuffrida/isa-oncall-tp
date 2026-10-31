import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { DespliegueDeServicioDetail } from './despliegue-de-servicio-detail';

describe('DespliegueDeServicio Management Detail Component', () => {
  let comp: DespliegueDeServicioDetail;
  let fixture: ComponentFixture<DespliegueDeServicioDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./despliegue-de-servicio-detail').then(m => m.DespliegueDeServicioDetail),
              resolve: { despliegueDeServicio: () => of({ id: 6111 }) },
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
    fixture = TestBed.createComponent(DespliegueDeServicioDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load despliegueDeServicio on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DespliegueDeServicioDetail);

      // THEN
      expect(instance.despliegueDeServicio()).toEqual(expect.objectContaining({ id: 6111 }));
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
