import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { VentanaDeMantenimientoService } from '../service/ventana-de-mantenimiento.service';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './ventana-de-mantenimiento-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class VentanaDeMantenimientoDeleteDialog {
  ventanaDeMantenimiento?: IVentanaDeMantenimiento;

  protected readonly ventanaDeMantenimientoService = inject(VentanaDeMantenimientoService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ventanaDeMantenimientoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
