import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ServicioEnVentanaService } from '../service/servicio-en-ventana.service';
import { IServicioEnVentana } from '../servicio-en-ventana.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './servicio-en-ventana-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ServicioEnVentanaDeleteDialog {
  servicioEnVentana?: IServicioEnVentana;

  protected readonly servicioEnVentanaService = inject(ServicioEnVentanaService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.servicioEnVentanaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
