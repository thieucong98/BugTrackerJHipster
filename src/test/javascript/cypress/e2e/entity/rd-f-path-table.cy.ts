import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('RdFPathTable e2e test', () => {
  const rdFPathTablePageUrl = '/rd-f-path-table';
  const rdFPathTablePageUrlPattern = new RegExp('/rd-f-path-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rdFPathTableSample = { path: 'Operations' };

  let rdFPathTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rd-f-path-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rd-f-path-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rd-f-path-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rdFPathTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rd-f-path-tables/${rdFPathTable.id}`,
      }).then(() => {
        rdFPathTable = undefined;
      });
    }
  });

  it('RdFPathTables menu should load RdFPathTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rd-f-path-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RdFPathTable').should('exist');
    cy.url().should('match', rdFPathTablePageUrlPattern);
  });

  describe('RdFPathTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rdFPathTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RdFPathTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rd-f-path-table/new$'));
        cy.getEntityCreateUpdateHeading('RdFPathTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFPathTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rd-f-path-tables',
          body: rdFPathTableSample,
        }).then(({ body }) => {
          rdFPathTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rd-f-path-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/rd-f-path-tables?page=0&size=20>; rel="last",<http://localhost/api/rd-f-path-tables?page=0&size=20>; rel="first"',
              },
              body: [rdFPathTable],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rdFPathTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RdFPathTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rdFPathTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFPathTablePageUrlPattern);
      });

      it('edit button click should load edit RdFPathTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RdFPathTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFPathTablePageUrlPattern);
      });

      it('edit button click should load edit RdFPathTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RdFPathTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFPathTablePageUrlPattern);
      });

      it('last delete button click should delete instance of RdFPathTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rdFPathTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFPathTablePageUrlPattern);

        rdFPathTable = undefined;
      });
    });
  });

  describe('new RdFPathTable page', () => {
    beforeEach(() => {
      cy.visit(`${rdFPathTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RdFPathTable');
    });

    it('should create an instance of RdFPathTable', () => {
      cy.get(`[data-cy="path"]`).type('monitor').should('have.value', 'monitor');

      cy.get(`[data-cy="contentsXslt"]`).type('Ergonomic').should('have.value', 'Ergonomic');

      cy.get(`[data-cy="description"]`).type('leverage').should('have.value', 'leverage');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        rdFPathTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', rdFPathTablePageUrlPattern);
    });
  });
});
