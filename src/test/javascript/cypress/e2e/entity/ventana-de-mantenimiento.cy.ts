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

describe('VentanaDeMantenimiento e2e test', () => {
  const ventanaDeMantenimientoPageUrl = '/ventana-de-mantenimiento';
  let username: string;
  let password: string;
  const ventanaDeMantenimientoSample = {
    nombre: 'less witty and',
    desde: '2026-10-30T20:25:52.521Z',
    hasta: '2026-10-31T12:59:13.274Z',
    recurrencia: 'DIARIA',
    zonaHoraria: 'whenever hastily splendid',
    activa: false,
  };

  let ventanaDeMantenimiento;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ventana-de-mantenimientos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ventana-de-mantenimientos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ventana-de-mantenimientos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ventanaDeMantenimiento) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ventana-de-mantenimientos/${ventanaDeMantenimiento.id}`,
      }).then(() => {
        ventanaDeMantenimiento = undefined;
      });
    }
  });

  it('VentanaDeMantenimientos menu should load VentanaDeMantenimientos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ventana-de-mantenimiento');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VentanaDeMantenimiento').should('exist');
    cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
  });

  describe('VentanaDeMantenimiento page', () => {
    it('should have translated page title', () => {
      cy.visit(ventanaDeMantenimientoPageUrl);
      cy.getEntityHeading('VentanaDeMantenimiento').should('not.contain', 'oncallApp.ventanaDeMantenimiento.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ventanaDeMantenimientoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VentanaDeMantenimiento page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${ventanaDeMantenimientoPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ventana-de-mantenimientos',
          body: ventanaDeMantenimientoSample,
        }).then(({ body }) => {
          ventanaDeMantenimiento = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ventana-de-mantenimientos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ventanaDeMantenimiento],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ventanaDeMantenimientoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VentanaDeMantenimiento page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ventanaDeMantenimiento');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });

      it('edit button click should load edit VentanaDeMantenimiento page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });

      it('edit button click should load edit VentanaDeMantenimiento page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
      });

      it('last delete button click should delete instance of VentanaDeMantenimiento', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ventanaDeMantenimiento').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);

        ventanaDeMantenimiento = undefined;
      });
    });
  });

  describe('new VentanaDeMantenimiento page', () => {
    beforeEach(() => {
      cy.visit(ventanaDeMantenimientoPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VentanaDeMantenimiento');
    });

    it('should create an instance of VentanaDeMantenimiento', () => {
      cy.get(`[data-cy="nombre"]`).type('loyally wide although');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'loyally wide although');

      cy.get(`[data-cy="descripcion"]`).type('distinction large than');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'distinction large than');

      cy.get(`[data-cy="desde"]`).type('2026-10-30T20:58');
      cy.get(`[data-cy="desde"]`).blur();
      cy.get(`[data-cy="desde"]`).should('have.value', '2026-10-30T20:58');

      cy.get(`[data-cy="hasta"]`).type('2026-10-31T04:08');
      cy.get(`[data-cy="hasta"]`).blur();
      cy.get(`[data-cy="hasta"]`).should('have.value', '2026-10-31T04:08');

      cy.get(`[data-cy="recurrencia"]`).select('DIARIA');

      cy.get(`[data-cy="zonaHoraria"]`).type('amend provided');
      cy.get(`[data-cy="zonaHoraria"]`).should('have.value', 'amend provided');

      cy.get(`[data-cy="repetirHasta"]`).type('2026-10-30');
      cy.get(`[data-cy="repetirHasta"]`).blur();
      cy.get(`[data-cy="repetirHasta"]`).should('have.value', '2026-10-30');

      cy.get(`[data-cy="activa"]`).should('not.be.checked');
      cy.get(`[data-cy="activa"]`).click();
      cy.get(`[data-cy="activa"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        ventanaDeMantenimiento = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', ventanaDeMantenimientoPageUrl);
    });
  });
});
