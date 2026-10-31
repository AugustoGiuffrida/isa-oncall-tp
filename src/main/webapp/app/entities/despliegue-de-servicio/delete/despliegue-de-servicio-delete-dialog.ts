import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { IDespliegueDeServicio } from '../despliegue-de-servicio.model';
import { DespliegueDeServicioService } from '../service/despliegue-de-servicio.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './despliegue-de-servicio-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class DespliegueDeServicioDeleteDialog {
  despliegueDeServicio?: IDespliegueDeServicio;

  protected readonly despliegueDeServicioService = inject(DespliegueDeServicioService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.despliegueDeServicioService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
