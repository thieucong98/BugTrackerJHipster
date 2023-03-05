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

describe('DemoOrm e2e test', () => {
  const demoOrmPageUrl = '/demo-orm';
  const demoOrmPageUrlPattern = new RegExp('/demo-orm(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const demoOrmSample = { codeId: 'state Cedi', codeName: 'engage transmitting', itemKey: 'withdrawal Rican sticky' };

  let demoOrm;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/demo-orms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/demo-orms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/demo-orms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (demoOrm) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/demo-orms/${demoOrm.id}`,
      }).then(() => {
        demoOrm = undefined;
      });
    }
  });

  it('DemoOrms menu should load DemoOrms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('demo-orm');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DemoOrm').should('exist');
    cy.url().should('match', demoOrmPageUrlPattern);
  });

  describe('DemoOrm page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(demoOrmPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DemoOrm page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/demo-orm/new$'));
        cy.getEntityCreateUpdateHeading('DemoOrm');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', demoOrmPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/demo-orms',
          body: demoOrmSample,
        }).then(({ body }) => {
          demoOrm = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/demo-orms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/demo-orms?page=0&size=20>; rel="last",<http://localhost/api/demo-orms?page=0&size=20>; rel="first"',
              },
              body: [demoOrm],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(demoOrmPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DemoOrm page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('demoOrm');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', demoOrmPageUrlPattern);
      });

      it('edit button click should load edit DemoOrm page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DemoOrm');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', demoOrmPageUrlPattern);
      });

      it('edit button click should load edit DemoOrm page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DemoOrm');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', demoOrmPageUrlPattern);
      });

      it('last delete button click should delete instance of DemoOrm', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('demoOrm').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', demoOrmPageUrlPattern);

        demoOrm = undefined;
      });
    });
  });

  describe('new DemoOrm page', () => {
    beforeEach(() => {
      cy.visit(`${demoOrmPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DemoOrm');
    });

    it('should create an instance of DemoOrm', () => {
      cy.get(`[data-cy="codeId"]`).type('Surinam Ba').should('have.value', 'Surinam Ba');

      cy.get(`[data-cy="codeName"]`).type('Walks Phased Rupee').should('have.value', 'Walks Phased Rupee');

      cy.get(`[data-cy="itemKey"]`).type('orchestration').should('have.value', 'orchestration');

      cy.get(`[data-cy="itemValueJa"]`).type('system Health').should('have.value', 'system Health');

      cy.get(`[data-cy="itemValueEn"]`).type('neural Business-focused Angola').should('have.value', 'neural Business-focused Angola');

      cy.get(`[data-cy="itemValuePair"]`).type('Dakota Investment').should('have.value', 'Dakota Investment');

      cy.get(`[data-cy="parentCodeId"]`).type('Soft').should('have.value', 'Soft');

      cy.get(`[data-cy="parentItemKey"]`).type('index').should('have.value', 'index');

      cy.get(`[data-cy="parentItemKeyBackup"]`).type('Engineer Brand primary').should('have.value', 'Engineer Brand primary');

      cy.get(`[data-cy="parentItemKeyNew"]`).type('flexibility Market Agent').should('have.value', 'flexibility Market Agent');

      cy.get(`[data-cy="createdTimestamp"]`).type('2023-03-04T23:16').blur().should('have.value', '2023-03-04T23:16');

      cy.get(`[data-cy="updatedTimestamp"]`).type('2023-03-05T01:26').blur().should('have.value', '2023-03-05T01:26');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        demoOrm = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', demoOrmPageUrlPattern);
    });
  });
});
