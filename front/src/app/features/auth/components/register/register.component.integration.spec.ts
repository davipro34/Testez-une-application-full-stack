// Importation des modules nécessaires
import { TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { FormBuilder } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';

// Début de la suite de tests pour le composant RegisterComponent
describe('RegisterComponent', () => {
  let component: RegisterComponent; // Instance du composant à tester
  let authService: AuthService; // Service d'authentification à espionner
  let router: Router; // Routeur à espionner

  // Configuration de l'environnement de test avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Importation du module HttpClientTestingModule pour simuler les requêtes HTTP
      providers: [
        RegisterComponent, // Fourniture du composant à tester
        FormBuilder, // Fourniture du FormBuilder pour la création de formulaires réactifs
        { provide: AuthService, useValue: { register: jest.fn() } }, // Fourniture d'une version espionnée du service AuthService
        { provide: Router, useValue: { navigate: jest.fn() } }, // Fourniture d'une version espionnée du routeur
      ],
    });

    // Injection des dépendances
    component = TestBed.inject(RegisterComponent);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  // Test de la méthode submit() et de son interaction avec AuthService lors de la soumission du formulaire.
  // Nous vérifions que la méthode register() du service AuthService est appelée avec les valeurs du formulaire.
  describe('submit', () => {
    // Test pour vérifier que AuthService.register est appelé avec les valeurs du formulaire lors de la soumission du formulaire
    it('should call AuthService.register with form values when form is submitted', () => {
      // Given : Initialisation des données de test
      const formValues = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123',
      };
      component.form.setValue(formValues); // Définition des valeurs du formulaire
      jest.spyOn(authService, 'register').mockReturnValue(of(undefined)); // Espionnage de la méthode register du service AuthService

      // When : Appel de la méthode à tester
      component.submit();

      // Then : Vérification que la méthode register du service AuthService a été appelée avec les bonnes valeurs
      expect(authService.register).toHaveBeenCalledWith(formValues);
    });

    // Test pour vérifier que onError est défini sur true lorsque AuthService.register renvoie une erreur
    it('should set onError to true when AuthService.register returns an error', () => {
      // Given : Initialisation des données de test
      const formValues = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123',
      };
      component.form.setValue(formValues); // Définition des valeurs du formulaire
      jest.spyOn(authService, 'register').mockImplementation(() => {
        return new Observable<void>((subscriber) => {
          subscriber.error(new Error('error')); // Simulation d'une erreur lors de l'appel à la méthode register
        });
      });

      // When : Appel de la méthode à tester
      component.submit();

      // Then : Vérification que onError est défini sur true
      expect(component.onError).toBe(true);
    });
  });
});