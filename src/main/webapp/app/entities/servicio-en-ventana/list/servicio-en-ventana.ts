import { ChangeDetectionStrategy, Component, OnInit, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { TranslatePipe } from '@ngx-translate/core';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { ServicioEnVentanaDeleteDialog } from '../delete/servicio-en-ventana-delete-dialog';
import { ServicioEnVentanaService } from '../service/servicio-en-ventana.service';
import { IServicioEnVentana } from '../servicio-en-ventana.model';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'jhi-servicio-en-ventana',
  templateUrl: './servicio-en-ventana.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    SortDirective,
    SortByDirective,
    TranslateDirective,
    TranslatePipe,
  ],
})
export class ServicioEnVentana implements OnInit {
  subscription: Subscription | null = null;
  readonly servicioEnVentanas = signal<IServicioEnVentana[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly servicioEnVentanaService = inject(ServicioEnVentanaService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.servicioEnVentanaService.servicioEnVentanasResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.servicioEnVentanas.set(this.fillComponentAttributesFromResponseBody([...this.servicioEnVentanaService.servicioEnVentanas()]));
    });
  }

  trackId = (item: IServicioEnVentana): number => this.servicioEnVentanaService.getServicioEnVentanaIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (this.servicioEnVentanas().length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }

  delete(servicioEnVentana: IServicioEnVentana): void {
    const modalRef = this.modalService.open(ServicioEnVentanaDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.servicioEnVentana = servicioEnVentana;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IServicioEnVentana[]): IServicioEnVentana[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IServicioEnVentana[]): IServicioEnVentana[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.servicioEnVentanaService.servicioEnVentanasParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
