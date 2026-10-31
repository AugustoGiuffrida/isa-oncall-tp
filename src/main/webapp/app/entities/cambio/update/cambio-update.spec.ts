import { beforeEach, describe, expect, it, vitest } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { ICambio } from '../cambio.model';
import { CambioService } from '../service/cambio.service';

import { CambioFormService } from './cambio-form.service';
import { CambioUpdate } from './cambio-update';

describe('Cambio Management Update Component', () => {
  let comp: CambioUpdate;
  let fixture: ComponentFixture<CambioUpdate>;
  let activatedRoute: ActivatedRoute;
  let cambioFormService: CambioFormService;
  let cambioService: CambioService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(CambioUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cambioFormService = TestBed.inject(CambioFormService);
    cambioService = TestBed.inject(CambioService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const cambio: ICambio = { id: 30774 };
      const autor: IUser = { id: 3944 };
      cambio.autor = autor;

      const userCollection: IUser[] = [{ id: 3944 }];
      vitest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [autor];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vitest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cambio });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cambio: ICambio = { id: 30774 };
      const autor: IUser = { id: 3944 };
      cambio.autor = autor;

      activatedRoute.data = of({ cambio });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(autor);
      expect(comp.cambio).toEqual(cambio);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICambio>();
      const cambio = { id: 11471 };
      vitest.spyOn(cambioFormService, 'getCambio').mockReturnValue(cambio);
      vitest.spyOn(cambioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cambio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cambio);
      saveSubject.complete();

      // THEN
      expect(cambioFormService.getCambio).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cambioService.update).toHaveBeenCalledWith(expect.objectContaining(cambio));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICambio>();
      const cambio = { id: 11471 };
      vitest.spyOn(cambioFormService, 'getCambio').mockReturnValue({ id: null });
      vitest.spyOn(cambioService, 'create').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cambio: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(cambio);
      saveSubject.complete();

      // THEN
      expect(cambioFormService.getCambio).toHaveBeenCalled();
      expect(cambioService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICambio>();
      const cambio = { id: 11471 };
      vitest.spyOn(cambioService, 'update').mockReturnValue(saveSubject);
      vitest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cambio });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cambioService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vitest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
