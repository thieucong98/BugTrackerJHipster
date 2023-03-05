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

describe('RdFBatchRegister e2e test', () => {
  const rdFBatchRegisterPageUrl = '/rd-f-batch-register';
  const rdFBatchRegisterPageUrlPattern = new RegExp('/rd-f-batch-register(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rdFBatchRegisterSample = {
    workflowId: 'reboot',
    dbname: 'Salad',
    feedId: 'Licensed Intranet',
    func: 'Profound',
    reqDatetime: 'Checking Tanza',
    execUser: 'connect invoice',
    systemIds: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    mode: 'Tasty Chicken',
    done: 'F',
    createdTimestamp: '2023-03-05T03:26:43.932Z',
    updatedTimestamp: '2023-03-04T14:04:12.805Z',
  };

  let rdFBatchRegister;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/rd-f-batch-registers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/rd-f-batch-registers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/rd-f-batch-registers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rdFBatchRegister) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/rd-f-batch-registers/${rdFBatchRegister.id}`,
      }).then(() => {
        rdFBatchRegister = undefined;
      });
    }
  });

  it('RdFBatchRegisters menu should load RdFBatchRegisters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('rd-f-batch-register');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RdFBatchRegister').should('exist');
    cy.url().should('match', rdFBatchRegisterPageUrlPattern);
  });

  describe('RdFBatchRegister page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rdFBatchRegisterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RdFBatchRegister page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/rd-f-batch-register/new$'));
        cy.getEntityCreateUpdateHeading('RdFBatchRegister');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFBatchRegisterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/rd-f-batch-registers',
          body: rdFBatchRegisterSample,
        }).then(({ body }) => {
          rdFBatchRegister = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/rd-f-batch-registers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/rd-f-batch-registers?page=0&size=20>; rel="last",<http://localhost/api/rd-f-batch-registers?page=0&size=20>; rel="first"',
              },
              body: [rdFBatchRegister],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rdFBatchRegisterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RdFBatchRegister page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rdFBatchRegister');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFBatchRegisterPageUrlPattern);
      });

      it('edit button click should load edit RdFBatchRegister page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RdFBatchRegister');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFBatchRegisterPageUrlPattern);
      });

      it('edit button click should load edit RdFBatchRegister page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RdFBatchRegister');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFBatchRegisterPageUrlPattern);
      });

      it('last delete button click should delete instance of RdFBatchRegister', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rdFBatchRegister').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rdFBatchRegisterPageUrlPattern);

        rdFBatchRegister = undefined;
      });
    });
  });

  describe('new RdFBatchRegister page', () => {
    beforeEach(() => {
      cy.visit(`${rdFBatchRegisterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RdFBatchRegister');
    });

    it('should create an instance of RdFBatchRegister', () => {
      cy.get(`[data-cy="workflowId"]`).type('bottom-line Brand Baht').should('have.value', 'bottom-line Brand Baht');

      cy.get(`[data-cy="dbname"]`).type('Angola').should('have.value', 'Angola');

      cy.get(`[data-cy="feedId"]`).type('Frozen Small').should('have.value', 'Frozen Small');

      cy.get(`[data-cy="func"]`).type('card Account Wiscons').should('have.value', 'card Account Wiscons');

      cy.get(`[data-cy="reqDatetime"]`).type('connecting Sum').should('have.value', 'connecting Sum');

      cy.get(`[data-cy="execUser"]`).type('circuit Marketing').should('have.value', 'circuit Marketing');

      cy.get(`[data-cy="systemIds"]`)
        .type('../fake-data/blob/hipster.txt')
        .invoke('val')
        .should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="mode"]`).type('Home Cove Marketing').should('have.value', 'Home Cove Marketing');

      cy.get(`[data-cy="done"]`).type('J').should('have.value', 'J');

      cy.get(`[data-cy="createdTimestamp"]`).type('2023-03-05T07:13').blur().should('have.value', '2023-03-05T07:13');

      cy.get(`[data-cy="updatedTimestamp"]`).type('2023-03-05T01:33').blur().should('have.value', '2023-03-05T01:33');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        rdFBatchRegister = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', rdFBatchRegisterPageUrlPattern);
    });
  });
});
