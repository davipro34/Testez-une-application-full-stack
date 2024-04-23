/// <reference types="Cypress" />

describe('Login spec', () => {
  // Given
  // Définition de l'utilisateur pour les tests
  const user = {
    id: 1,
    firstName: 'Admin',
    lastName: 'Admin',
    email: 'yoga@studio.com',
    password: 'test!1234',
    admin: true,
  };

  // Avant chaque test, visite de la page de connexion
  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login successfully', () => {
    // When
    // Interception de la requête de connexion et simulation d'une réponse réussie
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 201,
      body: user,
    });

    // Interception de la requête de session et simulation d'une réponse réussie
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],
    }).as('session');

    // Remplissage du formulaire de connexion et soumission
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)

    // Then
    // Vérification que l'URL a changé pour la page de sessions
    cy.url().should('eq', `${Cypress.config().baseUrl}sessions`);

    // Vérification qu'aucun message d'erreur n'est affiché
    cy.get('.error').should('not.exist')

    // Interception de la requête de l'utilisateur et simulation d'une réponse réussie
    cy.intercept('GET', `/api/user/${user.id}`, {
      statusCode: 200,
      body: user,
    }).as('getUser')

    // Clic sur le lien vers la page de l'utilisateur
    cy.get('span[routerLink=me]').click();

    // Then
    // Vérification que l'URL a changé pour la page de l'utilisateur
    cy.url().should('include', '/me');
    
    // Verifier que le bouton affiche "You are admin" et ne soit pas cliquable
    cy.get('p.my2').should('contain', 'You are admin');
  })

  it('should logout successfully', () => {
    // When
    // Interception de la requête de connexion et simulation d'une réponse réussie
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 201,
      body: user,
    });

    // Interception de la requête de session et simulation d'une réponse réussie
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],
    }).as('session');

    // Remplissage du formulaire de connexion et soumission
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)

    // Click sur 'Logout' pour se deconnecter
    cy.get('.link').contains('Logout').click()

    // Then
    // Vérification que l'URL a changé pour le retour à la page d'accueil
    cy.url().should('eq', Cypress.config().baseUrl)

    // Vérification qu'aucun message d'erreur n'est affiché
    cy.get('.error').should('not.exist')
  })

  it('should not login with incorrect email', () => {
    // When
    // Remplissage du formulaire de connexion avec un email incorrect et soumission
    cy.get('input[formControlName=email]').type("noexistinguserg@studio.com")
    cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)

    // Then
    // Vérification qu'un message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred')
  })

  it('should not login with incorrect password', () => {
    // When
    // Remplissage du formulaire de connexion avec un mot de passe incorrect et soumission
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(`badpassword{enter}{enter}`)

    // Then
    // Vérification qu'un message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred')
  })

  it('should not login with missing password', () => {
    // When
    // Remplissage du formulaire de connexion sans mot de passe et soumission
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(`{enter}{enter}`)

    // Then
    // Vérification qu'un message d'erreur est affiché
    cy.get('.error').should('contain', 'An error occurred')
  })
});