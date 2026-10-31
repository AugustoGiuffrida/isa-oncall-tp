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

describe('Cambio e2e test', () => {
  const cambioPageUrl = '/cambio';
  let username: string;
  let password: string;
  // const cambioSample = {"titulo":"rust object calculating","creadoEn":"2026-10-31T11:36:34.431Z"};

  let cambio;
  // let user;

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
      url: '/api/users',
      body: {"login":"Andres49","firstName":"María Eugenia","lastName":"Abreu Miranda","email":"Angel.OroscoAlejandro@gmail.com","langKey":"um yum","imageUrl":"eek"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cambios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cambios').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cambios/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/despliegue-de-servicios', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (cambio) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cambios/${cambio.id}`,
      }).then(() => {
        cambio = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Cambios menu should load Cambios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cambio');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cambio').should('exist');
    cy.location('pathname').should('eq', cambioPageUrl);
  });

  describe('Cambio page', () => {
    it('should have translated page title', () => {
      cy.visit(cambioPageUrl);
      cy.getEntityHeading('Cambio').should('not.contain', 'oncallApp.cambio.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cambioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cambio page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${cambioPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Cambio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cambioPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cambios',
          body: {
            ...cambioSample,
            autor: user,
          },
        }).then(({ body }) => {
          cambio = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cambios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [cambio],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(cambioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(cambioPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Cambio page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cambio');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cambioPageUrl);
      });

      it('edit button click should load edit Cambio page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cambio');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cambioPageUrl);
      });

      it('edit button click should load edit Cambio page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cambio');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cambioPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Cambio', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cambio').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', cambioPageUrl);

        cambio = undefined;
      });
    });
  });

  describe('new Cambio page', () => {
    beforeEach(() => {
      cy.visit(cambioPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cambio');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Cambio', () => {
      cy.get(`[data-cy="titulo"]`).type('unlike');
      cy.get(`[data-cy="titulo"]`).should('have.value', 'unlike');

      cy.get(`[data-cy="descripcion"]`).type('extent ick psst');
      cy.get(`[data-cy="descripcion"]`).should('have.value', 'extent ick psst');

      cy.get(`[data-cy="ticketUrl"]`).type('continually');
      cy.get(`[data-cy="ticketUrl"]`).should('have.value', 'continually');

      cy.get(`[data-cy="creadoEn"]`).type('2026-10-31T06:36');
      cy.get(`[data-cy="creadoEn"]`).blur();
      cy.get(`[data-cy="creadoEn"]`).should('have.value', '2026-10-31T06:36');

      cy.get(`[data-cy="autor"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        cambio = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', cambioPageUrl);
    });
  });
});
