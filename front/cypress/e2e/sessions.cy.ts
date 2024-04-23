/// <reference types="Cypress" />

// Définition de la suite de tests pour la page "Sessions"
describe('Sessions page', () => {
  // Given : Les données de sessions pour les tests
  const sessions = [
    {
      id: 1,
      name: 'Séance de Yoga début d\'après-midi',
      description:  'Une séance de yoga sur le thème de l\'eau',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      name: 'Séance de Yoga milieu d\'après-midi',
      description:  'Une séance de yoga sur le thème de l\'air',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ];

  // Given : Les données pour une nouvelle session
  const newSession = {
    id: 3,
    name: 'Séance de Yoga fin d\'après-midi',
    description:  'Une séance de yoga sur le thème de la terre',
    date: new Date(),
    teacher_id: 1,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  // Given : Les données de sessions avec la nouvelle session
  const sessionsWithNewSession = [
    ...sessions,
    newSession,
  ];

  // Given : Les données pour une session mise à jour
  const updatedSession = {
    ...newSession,
    name: 'Mise à jour de la session de yoga',
    description:  'Description de la mise à jour de la session de yoga',
  }

  // Given : Les données de sessions avec la session mise à jour
  const sessionsWithUpdatedSession = [
      ...sessions,
    updatedSession,
  ];

  // Given : Les données des enseignants pour les tests
  const teachers = [
    {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ]

  // Given : Les informations de session de l'administrateur
  const admin = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'yoga@studio.com',
    firstName: 'Admin',
    lastName: 'Admin',
    admin: true,
  }

  // Given : Les informations de connexion
  const loginCredentials = {
    email: 'yoga@studio.com',
    password: 'test!1234',
  }

  // Définition de la suite de tests pour un utilisateur administrateur
  describe('given the user is an admin', () => {

    // Before : Avant chaque test, l'utilisateur se connecte
    before(() => {
      // When : L'utilisateur visite la page de connexion
      cy.visit('/login');
      // Then : Une requête POST est interceptée pour simuler la connexion
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 201,
        body: admin,
      }).as('login');

      // Then : Une requête GET est interceptée pour simuler le chargement des sessions
      cy.intercept('GET', '/api/session', sessions).as('sessions');

      // When : L'utilisateur remplit le formulaire de connexion
      cy.get('input[formControlName=email]').type(loginCredentials.email);
      cy.get('input[formControlName=password]').type(`${loginCredentials.password}{enter}{enter}`);
      // Then : L'utilisateur est redirigé vers la page des sessions
      cy.url().should('eq', `${Cypress.config().baseUrl}sessions`);
    });

    // Test : Vérification de l'affichage de la liste des sessions
    it('should display the list of sessions', () => {
      // Then : Le nombre de sessions affichées doit correspondre au nombre de sessions données
      cy.get('mat-card.item').should('have.length', sessions.length);
    });

    // Test : Vérification de l'affichage du bouton de création de session
    it('should display a button to create a session', () => {
      // Then : Le bouton de création de session doit exister
      cy.get('[routerLink=create]').should('exist');
    });

    // Test : Vérification de l'affichage du bouton pour voir les détails d'une session
    it('should display a button to view details of a session', () => {
      // Then : Le bouton pour voir les détails d'une session doit exister
      cy.get('mat-card-actions').find('button[mat-raised-button]').contains('Detail').should('exist');
    });

    // Définition de la suite de tests pour le formulaire de création de session
    describe('create session form', () => {

      // Before : Avant chaque test, l'utilisateur accède au formulaire de création de session
      before(() => {
        // Then : Une requête GET est interceptée pour simuler le chargement des enseignants
        cy.intercept('GET', '/api/teacher', {
          body: teachers
        }).as('teachers');

        // When : L'utilisateur vérifie le nombre de sessions affichées
        cy.get('mat-card.item').should('have.length', sessions.length);

        // When : L'utilisateur clique sur le bouton de création de session
        cy.get('[routerLink=create]').click();
        // Then : L'utilisateur est redirigé vers la page de création de session
        cy.url().should('eq', `${Cypress.config().baseUrl}sessions/create`);
      })

      // Test : Vérification de l'affichage d'une erreur si un champ obligatoire est manquant
      it('should display an error if a required field is missing', () => {
        // When : L'utilisateur tente de soumettre le formulaire sans remplir le champ "name"
        cy.get('input[formControlName=name]').type(`{enter}`);
        // Then : Le champ "name" doit être marqué comme invalide
        cy.get('input[formControlName="name"]').should('have.class', 'ng-invalid');
        // Then : Le bouton de soumission doit être désactivé
        cy.get('button[type="submit"]').should('be.disabled');
      });

      // Test : Vérification de la création d'une nouvelle session lorsque les champs obligatoires sont remplis
      it('should create a new session when required fields are entered', () => {

        // Given : Une requête POST est interceptée pour simuler la création de la session
        cy.intercept('POST', '/api/session', {
          statusCode: 201,
          body: newSession,
        }).as('session');

        // Given : Une requête GET est interceptée pour simuler le chargement des sessions avec la nouvelle session
        cy.intercept('GET', '/api/session', {
          body: sessionsWithNewSession,
        }).as('sessions');

        // When : On remplit les champs du formulaire avec les informations de la nouvelle session
        cy.get('input[formControlName=name]').type(newSession.name);
        cy.get('input[formControlName=date]').type(newSession.date.toISOString().split('T')[0]);
        cy.get('textarea[formControlName=description]').type(newSession.description);
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('mat-option').contains(`${teachers[0].firstName} ${teachers[0].lastName}`).click();

        // When : On soumet le formulaire
        cy.get('button[type="submit"]').contains('Save').click();

        // Then : Un message de confirmation de création de session devrait apparaître
        cy.get('snack-bar-container').contains('Session created !').should('exist');

        // Then : L'URL devrait être celle de la liste des sessions
        cy.url().should('eq', `${Cypress.config().baseUrl}sessions`);

        // Then : Le nombre de sessions devrait avoir augmenté de 1
        cy.get('mat-card.item').should('have.length', sessions.length + 1);

        // Then : Le nom de la nouvelle session devrait apparaître dans la liste
        cy.get('mat-card-title').contains(newSession.name, {matchCase: false}).should('exist');
      });

    });

    describe('update session form', () => {
      before(() => {
        // Given : On intercepte une requête GET pour simuler le chargement des enseignants
        cy.intercept('GET', '/api/teacher', {
          body: teachers
        }).as('teachers');

        // Given : On sélectionne la session à modifier
        cy.get('mat-card.item').eq(newSession.id - 1).within(() => {
          cy.get('mat-card-title').contains(newSession.name, {matchCase: false}).should('exist');

          // Given : On intercepte une requête GET pour simuler le chargement de la session à modifier
          cy.intercept('GET', `/api/session/${newSession.id}`, {
            body: newSession,
          }).as('session to update');

          // When : On clique sur le bouton "Edit"
          cy.get('mat-card-actions').find('button[mat-raised-button]').contains('Edit').click();

          // Then : L'URL devrait être celle de la page de mise à jour de la session
          cy.url().should('eq', `${Cypress.config().baseUrl}sessions/update/${newSession.id}`);
        });
      });

      // Test : Vérification de l'affichage d'une erreur si un champ obligatoire est manquant
      it('should display an error if a required field is missing', () => {
        // When : On vide le champ "name" et on soumet le formulaire
        cy.get('input[formControlName=name]').clear().type(`{enter}`);

        // Then : Le champ "name" devrait avoir la classe "ng-invalid"
        cy.get('input[formControlName="name"]').should('have.class', 'ng-invalid');

        // Then : Le bouton de soumission du formulaire devrait être désactivé
        cy.get('button[type="submit"]').should('be.disabled');
      });

      // Test : Vérification de la mise à jour d'une session lorsque les champs obligatoires sont remplis
      it('should update a session when required fields are entered', () => {
        // Given : On intercepte une requête PUT pour simuler la mise à jour de la session
        cy.intercept('PUT', `/api/session/${newSession.id}`, {
          body: updatedSession,
        }).as('update session');

        // When : On remplit les champs du formulaire avec les informations de la session mise à jour
        cy.get('input[formControlName=name]').clear().type(updatedSession.name);
        cy.get('input[formControlName=date]').clear().type(updatedSession.date.toISOString().split('T')[0]);
        cy.get('textarea[formControlName=description]').clear().type(updatedSession.description);
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('mat-option').contains(`${teachers[0].firstName} ${teachers[0].lastName}`).click();

        // Given : On intercepte une requête GET pour simuler le chargement des sessions avec la session mise à jour
        cy.intercept('GET', '/api/session', {
          body: sessionsWithUpdatedSession,
        }).as('sessions');

        // When : On soumet le formulaire
        cy.get('button[type="submit"]').contains('Save').click();

        // Then : Un message de confirmation de mise à jour de session devrait apparaître
        cy.get('snack-bar-container').contains('Session updated !').should('exist');

        // Then : On attend que le message de confirmation disparaisse
        cy.wait(3000); // wait for the snackbar to close

        // Then : L'URL devrait être celle de la liste des sessions
        cy.url().should('eq', `${Cypress.config().baseUrl}sessions`);

        // Then : Le nom de la session mise à jour devrait apparaître dans la liste
        cy.get('mat-card-title').contains(updatedSession.name, {matchCase: false}).should('exist');
      });
    });

    describe('session details', () => {
    before(() => {
      // Given : On intercepte une requête GET pour simuler le chargement de la session mise à jour
      cy.intercept('GET', `/api/session/${updatedSession.id}`, {
        body: updatedSession,
      }).as('session');

      // Given : On sélectionne la session dont on veut afficher les détails
      cy.get('mat-card.item').eq(updatedSession.id - 1).within(() => {
        cy.get('mat-card-title').contains(updatedSession.name, {matchCase: false}).should('exist');

        // Given : On intercepte une requête GET pour simuler le chargement de l'enseignant de la session
        cy.intercept('GET', `/api/teacher/${teachers[0].id}`, {
          body: teachers[0]
        }).as('teacher 1');

        // When : On clique sur le bouton "Detail"
        cy.get('mat-card-actions').find('button[mat-raised-button]').contains('Detail').click();
      });
      }
    )

      // Test : Vérification de l'affichage des détails d'une session
      it('should display the details of a session', () => {
        // Then : L'URL devrait être celle de la page de détail de la session
        cy.url().should('eq', `${Cypress.config().baseUrl}sessions/detail/${updatedSession.id}`);

        // Then : Le nom de la session devrait être affiché
        cy.get('h1').contains(updatedSession.name, {matchCase: false});

        // Then : La description de la session devrait être affichée
        cy.get('div.description').contains(updatedSession.description);

        // Then : Le prénom de l'enseignant de la session devrait être affiché
        cy.get('mat-card-subtitle').contains(teachers[0].firstName, {matchCase: false});
      });

      // Test : Vérification de l'affichage d'un bouton pour supprimer une session
      it('should display a button to delete a session', () => {
        // Then : Un bouton "Delete" devrait être affiché
        cy.get('mat-card-title').find('button[mat-raised-button]').contains('Delete').should('exist');
      });

      // Test : Vérification de la suppression d'une session
      it('should delete a session', () => {
        // Given : On intercepte une requête DELETE pour simuler la suppression de la session
        cy.intercept('DELETE', `/api/session/${updatedSession.id}`, {
          statusCode: 204,
        }).as('delete session');

        // Given : On intercepte une requête GET pour simuler le chargement des sessions sans la session supprimée
        cy.intercept('GET', '/api/session', {
          body: sessions,
        }).as('sessions');

        // When : On clique sur le bouton "Delete"
        cy.get('mat-card-title').find('button[mat-raised-button]').contains('Delete').click();

        // Then : Un message de confirmation de suppression de session devrait apparaître
        cy.get('snack-bar-container').contains('Session deleted !').should('exist');

        // Then : On attend que le message de confirmation disparaisse
        cy.wait(3000); // wait for the snackbar to close

        // Then : L'URL devrait être celle de la liste des sessions
        cy.url().should('eq', `${Cypress.config().baseUrl}sessions`);

        // Then : Le nombre de sessions devrait avoir diminué de 1
        cy.get('mat-card.item').should('have.length', 2);

        // Then : Le nom de la session supprimée ne devrait plus apparaître dans la liste
        cy.get('mat-card-title').contains(updatedSession.name, {matchCase: false}).should('not.exist');
      });
    });
  });
});