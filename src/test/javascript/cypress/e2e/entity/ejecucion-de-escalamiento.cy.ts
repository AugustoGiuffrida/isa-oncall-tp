import { entityDetailsBackButtonSelector, entityDetailsButtonSelector, entityTableSelector } from '../../support/entity';

describe('EjecucionDeEscalamiento e2e test', () => {
  const ejecucionDeEscalamientoPageUrl = '/ejecucion-de-escalamiento';
  let username: string;
  let password: string;

  let ejecucionDeEscalamiento;
  let incidente;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/incidentes',
      body: {
        titulo: 'yahoo that',
        descripcion: 'who what loosely',
        severidad: 'SEV4',
        estado: 'CERRADO',
        detectadoEn: '2023-12-04T19:11:41.114Z',
        reconocidoEn: '2023-12-04T01:03:17.274Z',
        mitigadoEn: '2023-12-04T15:11:18.120Z',
        resueltoEn: '2023-12-04T01:27:32.932Z',
        usuariosAfectados: 7696,
        cumplioObjetivo: true,
        silenciadoHasta: '2023-12-04T08:18:45.586Z',
      },
    }).then(({ body }) => {
      incidente = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ejecucion-de-escalamientos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ejecucion-de-escalamientos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ejecucion-de-escalamientos/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/incidentes', {
      statusCode: 200,
      body: [incidente],
    });

    cy.intercept('GET', '/api/paso-escalamientos', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (ejecucionDeEscalamiento) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ejecucion-de-escalamientos/${ejecucionDeEscalamiento.id}`,
      }).then(() => {
        ejecucionDeEscalamiento = undefined;
      });
    }
  });

  afterEach(() => {
    if (incidente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/incidentes/${incidente.id}`,
      }).then(() => {
        incidente = undefined;
      });
    }
  });

  it('EjecucionDeEscalamientos menu should load EjecucionDeEscalamientos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ejecucion-de-escalamiento');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EjecucionDeEscalamiento').should('exist');
    cy.location('pathname').should('eq', ejecucionDeEscalamientoPageUrl);
  });

  describe('EjecucionDeEscalamiento page', () => {
    it('should have translated page title', () => {
      cy.visit(ejecucionDeEscalamientoPageUrl);
      cy.getEntityHeading('EjecucionDeEscalamiento').should('not.contain', 'oncallApp.ejecucionDeEscalamiento.home.title');
    });

    describe('with existing value', () => {
      beforeEach(function () {
        cy.visit(ejecucionDeEscalamientoPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details EjecucionDeEscalamiento page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ejecucionDeEscalamiento');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ejecucionDeEscalamientoPageUrl);
      });
    });
  });
});
