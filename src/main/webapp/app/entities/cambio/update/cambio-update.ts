import { HttpResponse } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICambio } from '../cambio.model';
import { CambioService } from '../service/cambio.service';

import { CambioFormGroup, CambioFormService } from './cambio-form.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-cambio-update',
  templateUrl: './cambio-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CambioUpdate implements OnInit {
  readonly isSaving = signal(false);
  cambio: ICambio | null = null;

  usersSharedCollection = signal<IUser[]>([]);

  protected cambioService = inject(CambioService);
  protected cambioFormService = inject(CambioFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CambioFormGroup = this.cambioFormService.createCambioFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cambio }) => {
      this.cambio = cambio;
      if (cambio) {
        this.updateForm(cambio);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const cambio = this.cambioFormService.getCambio(this.editForm);
    if (cambio.id === null) {
      this.subscribeToSaveResponse(this.cambioService.create(cambio));
    } else {
      this.subscribeToSaveResponse(this.cambioService.update(cambio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICambio | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(cambio: ICambio): void {
    this.cambio = cambio;
    this.cambioFormService.resetForm(this.editForm, cambio);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, cambio.autor));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.cambio?.autor)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
