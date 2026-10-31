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

describe('ServicioEnVentana e2e test', () => {
  const servicioEnVentanaPageUrl = '/servicio-en-ventana';
  let username: string;
  let password: string;
  // const servicioEnVentanaSample = {};

  let servicioEnVentana;
  // let ventanaDeMantenimiento;
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
      url: '/api/ventana-de-mantenimientos',
      body: {"nombre":"within ha arrogantly","descripcion":"dramatic linseed","desde":"2026-10-31T07:34:30.941Z","hasta":"2026-10-31T01:38:26.904Z","recurrencia":"UNICA","zonaHoraria":"unkempt","repetirHasta":"2026-10-30","activa":true},
    }).then(({ body }) => {
      ventanaDeMantenimiento = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/servicios',
      body: {"nombre":"fatally even","descripcion":"finally bootleg","criticidad":"TIER1","entorno":"DESARROLLO","repositorioUrl":"coarse","activo":false},
    }).then(({ body }) => {
      servicio = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/servicio-en-ventanas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/servicio-en-ventanas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/servicio-en-ventanas/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/ventana-de-mantenimientos', {
      statusCode: 200,
      body: [ventanaDeMantenimiento],
    });

    cy.intercept('GET', '/api/servicios', {
      statusCode: 200,
      body: [servicio],
    });

  });
   */

  afterEach(() => {
    if (servicioEnVentana) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/servicio-en-ventanas/${servicioEnVentana.id}`,
      }).then(() => {
        servicioEnVentana = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (ventanaDeMantenimiento) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ventana-de-mantenimientos/${ventanaDeMantenimiento.id}`,
      }).then(() => {
        ventanaDeMantenimiento = undefined;
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

  it('ServicioEnVentanas menu should load ServicioEnVentanas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('servicio-en-ventana');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServicioEnVentana').should('exist');
    cy.location('pathname').should('eq', servicioEnVentanaPageUrl);
  });

  describe('ServicioEnVentana page', () => {
    it('should have translated page title', () => {
      cy.visit(servicioEnVentanaPageUrl);
      cy.getEntityHeading('ServicioEnVentana').should('not.contain', 'oncallApp.servicioEnVentana.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(servicioEnVentanaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServicioEnVentana page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${servicioEnVentanaPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ServicioEnVentana');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioEnVentanaPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/servicio-en-ventanas',
          body: {
            ...servicioEnVentanaSample,
            ventana: ventanaDeMantenimiento,
            servicio: servicio,
          },
        }).then(({ body }) => {
          servicioEnVentana = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/servicio-en-ventanas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [servicioEnVentana],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(servicioEnVentanaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(servicioEnVentanaPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ServicioEnVentana page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('servicioEnVentana');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioEnVentanaPageUrl);
      });

      it('edit button click should load edit ServicioEnVentana page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServicioEnVentana');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioEnVentanaPageUrl);
      });

      it('edit button click should load edit ServicioEnVentana page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServicioEnVentana');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioEnVentanaPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of ServicioEnVentana', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('servicioEnVentana').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', servicioEnVentanaPageUrl);

        servicioEnVentana = undefined;
      });
    });
  });

  describe('new ServicioEnVentana page', () => {
    beforeEach(() => {
      cy.visit(servicioEnVentanaPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServicioEnVentana');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of ServicioEnVentana', () => {
      cy.get(`[data-cy="nota"]`).type('fooey ick pish');
      cy.get(`[data-cy="nota"]`).should('have.value', 'fooey ick pish');

      cy.get(`[data-cy="ventana"]`).select(1);
      cy.get(`[data-cy="servicio"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        servicioEnVentana = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', servicioEnVentanaPageUrl);
    });
  });
});
