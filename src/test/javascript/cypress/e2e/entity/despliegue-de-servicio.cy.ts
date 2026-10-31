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

describe('DespliegueDeServicio e2e test', () => {
  const despliegueDeServicioPageUrl = '/despliegue-de-servicio';
  let username: string;
  let password: string;
  // const despliegueDeServicioSample = {"version":"aw","desplegadoEn":"2026-10-30T17:46:38.771Z"};

  let despliegueDeServicio;
  // let cambio;
  // let servicio;

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
      url: '/api/cambios',
      body: {"titulo":"however intermesh","descripcion":"fortunately","ticketUrl":"thrifty","creadoEn":"2026-10-30T14:18:33.291Z"},
    }).then(({ body }) => {
      cambio = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicios',
      body: {"nombre":"gloomy who psst","descripcion":"aha","criticidad":"TIER1","entorno":"DESARROLLO","repositorioUrl":"overfeed pfft violently","activo":true},
    }).then(({ body }) => {
      servicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/despliegue-de-servicios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/despliegue-de-servicios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/despliegue-de-servicios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cambios', {
      statusCode: 200,
      body: [cambio],
    });

    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

    cy.intercept('GET', '/api/correlacion-cambio-incidentes', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (despliegueDeServicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/despliegue-de-servicios/${despliegueDeServicio.id}`,
      }).then(() => {
        despliegueDeServicio = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (cambio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cambios/${cambio.id}`,
      }).then(() => {
        cambio = undefined;
      });
    }
    if (servicio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicios/${servicio.id}`,
      }).then(() => {
        servicio = undefined;
      });
    }
  });
   */

  it('DespliegueDeServicios menu should load DespliegueDeServicios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('despliegue-de-servicio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DespliegueDeServicio').should('exist');
    cy.location('pathname').should('eq', despliegueDeServicioPageUrl);
  });

  describe('DespliegueDeServicio page', () => {
    it('should have translated page title', () => {
      cy.visit(despliegueDeServicioPageUrl);
      cy.getEntityHeading('DespliegueDeServicio').should('not.contain', 'oncallApp.despliegueDeServicio.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(despliegueDeServicioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DespliegueDeServicio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${despliegueDeServicioPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('DespliegueDeServicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', despliegueDeServicioPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/despliegue-de-servicios',
          body: {
            ...despliegueDeServicioSample,
            cambio: cambio,
            servicio: servicio,
          },
        }).then(({ body }) => {
          despliegueDeServicio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/despliegue-de-servicios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [despliegueDeServicio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(despliegueDeServicioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(despliegueDeServicioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details DespliegueDeServicio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('despliegueDeServicio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', despliegueDeServicioPageUrl);
      });

      it('edit button click should load edit DespliegueDeServicio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DespliegueDeServicio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', despliegueDeServicioPageUrl);
      });

      it('edit button click should load edit DespliegueDeServicio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DespliegueDeServicio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', despliegueDeServicioPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of DespliegueDeServicio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('despliegueDeServicio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', despliegueDeServicioPageUrl);

        despliegueDeServicio = undefined;
      });
    });
  });

  describe('new DespliegueDeServicio page', () => {
    beforeEach(() => {
      cy.visit(despliegueDeServicioPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DespliegueDeServicio');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of DespliegueDeServicio', () => {
      cy.get(`[data-cy="version"]`).type('beyond');
      cy.get(`[data-cy="version"]`).should('have.value', 'beyond');

      cy.get(`[data-cy="desplegadoEn"]`).type('2026-10-31T03:03');
      cy.get(`[data-cy="desplegadoEn"]`).blur();
      cy.get(`[data-cy="desplegadoEn"]`).should('have.value', '2026-10-31T03:03');

      cy.get(`[data-cy="cambio"]`).select(1);
      cy.get(`[data-cy="servicio"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        despliegueDeServicio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', despliegueDeServicioPageUrl);
    });
  });
});
