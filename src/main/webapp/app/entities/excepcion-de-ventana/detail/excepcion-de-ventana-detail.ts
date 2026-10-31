import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IExcepcionDeVentana } from '../excepcion-de-ventana.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-excepcion-de-ventana-detail',
  templateUrl: './excepcion-de-ventana-detail.html',
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
export class ExcepcionDeVentanaDetail {
  readonly excepcionDeVentana = input<IExcepcionDeVentana | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
