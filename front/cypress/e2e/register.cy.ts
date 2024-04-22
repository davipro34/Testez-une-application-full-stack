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
  })
})