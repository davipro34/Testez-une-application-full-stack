import { TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { FormBuilder } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

// Début de la suite de tests pour le composant LoginComponent
describe('LoginComponent', () => {
  let component: LoginComponent; // Instance du composant à tester
  let authService: AuthService; // Service d'authentification à espionner
  let router: Router; // Routeur à espionner
  let sessionService: SessionService; // Service de session à espionner

  // Configuration de l'environnement de test avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Importation du module HttpClientTestingModule pour simuler les requêtes HTTP
      providers: [
        LoginComponent, // Fourniture du composant à tester
        FormBuilder, // Fourniture du FormBuilder pour la création de formulaires réactifs
        { provide: AuthService, useValue: { login: jest.fn() } }, // Fourniture d'une version espionnée du service AuthService
        { provide: Router, useValue: { navigate: jest.fn() } }, // Fourniture d'une version espionnée du routeur
        { provide: SessionService, useValue: { logIn: jest.fn(), $isLogged: jest.fn() } }, // Fourniture d'une version espionnée du service SessionService
      ],
    });

    // Injection des dépendances
    component = TestBed.inject(LoginComponent);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
  });

  // Test de la méthode submit() pour vérifier son interaction avec AuthService, SessionService et Router.
  // Ce test vérifie que lors de la soumission du formulaire de connexion, la méthode login() du service AuthService est appelée avec les valeurs du formulaire,
  // que la méthode logIn() du service SessionService est appelée avec la réponse de la méthode login() du service AuthService,
  // et que la méthode navigate() du routeur est appelée avec le chemin '/sessions'.
  describe('submit', () => {
    // Test pour vérifier que la méthode login du service AuthService est appelée avec les valeurs du formulaire lors de la soumission du formulaire
    it('should call AuthService.login with form values when form is submitted', () => {
      // Given
      const formValues = {
        email: 'test@example.com',
        password: 'password',
      };
      component.form.setValue(formValues); // Définition des valeurs du formulaire
      jest.spyOn(authService, 'login').mockReturnValue(of({} as SessionInformation)); // Espionnage de la méthode login du service AuthService

      // When
      component.submit();

      // Then
      expect(authService.login).toHaveBeenCalledWith(formValues);
    });

    // Test pour vérifier que la méthode logIn du service SessionService est appelée avec la réponse de la méthode login du service AuthService lors de la soumission du formulaire
    it('should call SessionService.logIn with AuthService.login response when form is submitted', () => {
      // Given
      const formValues = {
        email: 'test@example.com',
        password: 'password',
      };
      const sessionInformation = {} as SessionInformation;
      component.form.setValue(formValues); // Définition des valeurs du formulaire
      jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation)); // Espionnage de la méthode login du service AuthService
      const logInSpy = jest.spyOn(sessionService, 'logIn'); // Espionnage de la méthode logIn du service SessionService

      // When
      component.submit();

      // Then
      expect(logInSpy).toHaveBeenCalledWith(sessionInformation);
    });

    // Test pour vérifier que la méthode navigate du routeur est appelée avec le chemin '/sessions' lors de la soumission du formulaire
    it('should call Router.navigate with \'/sessions\' when form is submitted', () => {
      // Given
      const formValues = {
        email: 'test@example.com',
        password: 'password',
      };
      component.form.setValue(formValues); // Définition des valeurs du formulaire
      jest.spyOn(authService, 'login').mockReturnValue(of({} as SessionInformation)); // Espionnage de la méthode login du service AuthService
      const navigateSpy = jest.spyOn(router, 'navigate'); // Espionnage de la méthode navigate du routeur

      // When
      component.submit();

      // Then
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    });

    // Test pour vérifier que onError est défini sur true lorsque la méthode login du service AuthService renvoie une erreur
    it('should set onError to true when AuthService.login returns an error', () => {
      // Given
      const formValues = {
        email: 'test@example.com',
        password: 'password',
      };
      component.form.setValue(formValues); // Définition des valeurs du formulaire
      jest.spyOn(authService, 'login').mockReturnValue(throwError('error')); // Espionnage de la méthode login du service AuthService

      // When
      component.submit();

      // Then
      expect(component.onError).toBe(true);
    });
  });
});