import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { ICorrelacionCambioIncidente } from '../correlacion-cambio-incidente.model';
import { CorrelacionCambioIncidenteService } from '../service/correlacion-cambio-incidente.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './correlacion-cambio-incidente-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class CorrelacionCambioIncidenteDeleteDialog {
  correlacionCambioIncidente?: ICorrelacionCambioIncidente;

  protected readonly correlacionCambioIncidenteService = inject(CorrelacionCambioIncidenteService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.correlacionCambioIncidenteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
