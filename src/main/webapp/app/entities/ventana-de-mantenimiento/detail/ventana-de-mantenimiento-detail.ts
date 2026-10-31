import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IVentanaDeMantenimiento } from '../ventana-de-mantenimiento.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-ventana-de-mantenimiento-detail',
  templateUrl: './ventana-de-mantenimiento-detail.html',
  imports: [
    FontAwesomeModule,
    Alert,
    AlertError,
    TranslateDirective,
    TranslatePipe,
    RouterLink,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export class VentanaDeMantenimientoDetail {
  readonly ventanaDeMantenimiento = input<IVentanaDeMantenimiento | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
