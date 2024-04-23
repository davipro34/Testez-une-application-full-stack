/// <reference types="Cypress" />

describe('Register spec', () => {
  // Given
  // Définition de l'utilisateur pour les tests
  const user = {
    id: 1,
    firstName: 'David',
    lastName: 'Pro',
    email: 'david@email.com',
    password: 'password',
  };

  // Avant chaque test, visite de la page d'inscription
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display an error if the required email field is not filled', () => {
    // When
    // Remplissage du formulaire d'inscription sans l'email et soumission
    cy.get('input[formControlName=firstName]').type(user.firstName)
    cy.get('input[formControlName=lastName]').type(user.lastName)
    cy.get('input[formControlName="email"]').type(`{enter}`);
    cy.get('input[formControlName=password]').type(user.password)

    // Then
    // Vérification que le champ email est invalide
    cy.get('input[formControlName="email"]').should('have.class', 'ng-invalid');

    // Vérification que le bouton de soumission est désactivé
    cy.get('button[type=submit]').should('be.disabled')
  });

  it('should display an error if the register service returns an error', () => {
    // Given
    // Interception de la requête d'inscription et simulation d'une réponse d'erreur
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 500,
      body: { error: 'An error occurred' },
    }).as('register')
  
    // When
    // Remplissage du formulaire d'inscription et soumission
    cy.get('input[formControlName=firstName]').type(user.firstName)
    cy.get('input[formControlName=lastName]').type(user.lastName)
    cy.get('input[formControlName="email"]').type(user.email)
    cy.get('input[formControlName=password]').type(user.password)
    cy.get('button[type=submit]').click()
  
    // Then
    // Vérification que le message "An error occurred" s'affiche
    cy.get('span.error.ml2.ng-star-inserted').should('contain.text', 'An error occurred');
  });

  it('should display an error if the required password field is to short', () => {
    // When
    // Remplissage du formulaire d'inscription sans le mot de passe et soumission
    cy.get('input[formControlName=firstName]').type(user.firstName)
    cy.get('input[formControlName=lastName]').type(user.lastName)
    cy.get('input[formControlName="email"]').type(user.email)
    cy.get('input[formControlName=password]').type("12")
    cy.get('button[type=submit]').click()

    // Then
    // Vérification que le message "An error occurred" s'affiche
    cy.get('span.error.ml2.ng-star-inserted').should('contain.text', 'An error occurred');
  });

  it('should register, log in, verify user account details and delete it', () => {
    // Given
    // Interception de la requête d'inscription et simulation d'une réponse réussie
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 201,
      body: user,
    }).as('register')

    // When
    // Remplissage du formulaire d'inscription et soumission
    cy.get('input[formControlName=firstName]').type(user.firstName)
    cy.get('input[formControlName=lastName]').type(user.lastName)
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(user.password)
    cy.get('button[type=submit]').click()

    // Then
    // Vérification que l'URL a changé pour la page de connexion
    cy.url().should('include', '/login')

    // Given
    // Interception de la requête de connexion et simulation d'une réponse réussie
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 201,
      body: user,
    }).as('login')

    // Interception de la requête de session et simulation d'une réponse réussie
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: user,
    }).as('getSession')

    // When
    // Remplissage du formulaire de connexion et soumission
    cy.get('input[formControlName=email]').type(user.email)
    cy.get('input[formControlName=password]').type(`${user.password}{enter}{enter}`)

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

    // Vérification que les détails de l'utilisateur sont corrects
    cy.get('.m3 mat-card-content p').contains(`Name: ${user.firstName} ${user.lastName.toUpperCase()}`).should('exist');
    cy.get('.m3 mat-card-content p').contains(`Email: ${user.email}`).should('exist');
    cy.get('.m3 mat-card-content div.my2').should('exist');

    // Given
    // Interception de la requête DELETE et simulation d'une réponse réussie
    cy.intercept('DELETE', `/api/user/${user.id}`, {
      statusCode: 200,
    }).as('deleteUser')

    // When
    // Clique sur bouton de suppression
    cy.get('.my2 > .mat-focus-indicator').click();

    // Then
    // Vérification que la requête DELETE a été appelée
    cy.wait('@deleteUser').its('response.statusCode').should('eq', 200);

    // Vérification que l'utilisateur est redirigé vers la page de connexion après la suppression
    cy.url().should('eq', 'http://localhost:4200/');
  });
})