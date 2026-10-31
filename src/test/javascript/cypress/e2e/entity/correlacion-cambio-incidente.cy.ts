import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('CorrelacionCambioIncidente e2e test', () => {
  const correlacionCambioIncidentePageUrl = '/correlacion-cambio-incidente';
  let username: string;
  let password: string;
  // const correlacionCambioIncidenteSample = {"origen":"MANUAL","estado":"SUGERIDA","detectadaEn":"2026-10-31T07:29:53.212Z"};

  let correlacionCambioIncidente;
  // let incidente;
  // let despliegueDeServicio;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/incidentes',
      body: {"titulo":"redress put","descripcion":"hunger hm boastfully","severidad":"SEV3","estado":"MITIGADO","detectadoEn":"2023-12-04T07:37:31.608Z","reconocidoEn":"2023-12-04T21:27:46.592Z","mitigadoEn":"2023-12-04T09:47:23.690Z","resueltoEn":"2023-12-04T03:18:53.298Z","usuariosAfectados":24787,"cumplioObjetivo":false,"silenciadoHasta":"2023-12-04T04:52:27.376Z"},
    }).then(({ body }) => {
      incidente = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/despliegue-de-servicios',
      body: {"version":"halt shipper","desplegadoEn":"2026-10-31T00:00:36.087Z"},
    }).then(({ body }) => {
      despliegueDeServicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/correlacion-cambio-incidentes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/correlacion-cambio-incidentes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/correlacion-cambio-incidentes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/incidentes', {
      statusCode: 200,
      body: [incidente],
    });

    cy.intercept('GET', '/api/despliegue-de-servicios', {
      statusCode: 200,
      body: [despliegueDeServicio],
    });

  });
   */

  afterEach(() => {
    if (correlacionCambioIncidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/correlacion-cambio-incidentes/${correlacionCambioIncidente.id}`,
      }).then(() => {
        correlacionCambioIncidente = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (incidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/incidentes/${incidente.id}`,
      }).then(() => {
        incidente = undefined;
      });
    }
    if (despliegueDeServicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/despliegue-de-servicios/${despliegueDeServicio.id}`,
      }).then(() => {
        despliegueDeServicio = undefined;
      });
    }
  });
   */

  it('CorrelacionCambioIncidentes menu should load CorrelacionCambioIncidentes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('correlacion-cambio-incidente');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CorrelacionCambioIncidente').should('exist');
    cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);
  });

  describe('CorrelacionCambioIncidente page', () => {
    it('should have translated page title', () => {
      cy.visit(correlacionCambioIncidentePageUrl);
      cy.getEntityHeading('CorrelacionCambioIncidente').should('not.contain', 'oncallApp.correlacionCambioIncidente.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(correlacionCambioIncidentePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CorrelacionCambioIncidente page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${correlacionCambioIncidentePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CorrelacionCambioIncidente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/correlacion-cambio-incidentes',
          body: {
            ...correlacionCambioIncidenteSample,
            incidente: incidente,
            despliegue: despliegueDeServicio,
          },
        }).then(({ body }) => {
          correlacionCambioIncidente = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/correlacion-cambio-incidentes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [correlacionCambioIncidente],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(correlacionCambioIncidentePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(correlacionCambioIncidentePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CorrelacionCambioIncidente page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('correlacionCambioIncidente');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);
      });

      it('edit button click should load edit CorrelacionCambioIncidente page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CorrelacionCambioIncidente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);
      });

      it('edit button click should load edit CorrelacionCambioIncidente page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CorrelacionCambioIncidente');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CorrelacionCambioIncidente', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('correlacionCambioIncidente').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);

        correlacionCambioIncidente = undefined;
      });
    });
  });

  describe('new CorrelacionCambioIncidente page', () => {
    beforeEach(() => {
      cy.visit(correlacionCambioIncidentePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CorrelacionCambioIncidente');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CorrelacionCambioIncidente', () => {
      cy.get(`[data-cy="origen"]`).select('MANUAL');

      cy.get(`[data-cy="estado"]`).select('CONFIRMADA');

      cy.get(`[data-cy="detectadaEn"]`).type('2026-10-31T11:08');
      cy.get(`[data-cy="detectadaEn"]`).blur();
      cy.get(`[data-cy="detectadaEn"]`).should('have.value', '2026-10-31T11:08');

      cy.get(`[data-cy="nota"]`).type('floss now');
      cy.get(`[data-cy="nota"]`).should('have.value', 'floss now');

      cy.get(`[data-cy="incidente"]`).select(1);
      cy.get(`[data-cy="despliegue"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        correlacionCambioIncidente = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', correlacionCambioIncidentePageUrl);
    });
  });
});
