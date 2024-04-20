import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { ObserverSpy } from '@hirez_io/observer-spy';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // Test de la méthode $isLogged() pour vérifier qu'elle retourne un Observable qui émet la valeur actuelle de isLogged.
  describe('$isLogged', () => {
    // Test pour vérifier que la méthode $isLogged retourne un Observable qui émet la valeur actuelle de isLogged
    it('should return an Observable that emits the current value of isLogged', () => {
      // Given
      const observerSpy = new ObserverSpy(); // Création d'un espion d'observateur pour espionner l'Observable retourné par la méthode $isLogged
      service.isLogged = true; // Définition de la valeur de isLogged

      // When
      service.$isLogged().subscribe(observerSpy); // Abonnement à l'Observable retourné par la méthode $isLogged et espionnage de cet Observable

      // Then
      expect(observerSpy.receivedNext()).toBe(true); // Vérification que l'Observable a émis la valeur actuelle de isLogged
    });
  });

  // Test de la méthode logIn
  describe('logIn', () => {
    // Test pour vérifier que la méthode logIn met à jour isLogged et sessionInformation correctement
    it('should update isLogged to true and sessionInformation when logIn is called', () => {
      // Given
      const user: SessionInformation = {
        token: 'testToken',
        type: 'testType',
        id: 1,
        username: 'testUsername',
        firstName: 'testFirstName',
        lastName: 'testLastName',
        admin: false
      };
      const observerSpy = new ObserverSpy<boolean>(); // Création d'un espion d'observateur pour espionner l'Observable retourné par la méthode $isLogged
      service.$isLogged().subscribe(observerSpy); // Abonnement à l'Observable retourné par la méthode $isLogged et espionnage de cet Observable

      // When
      service.logIn(user); // Appel de la méthode logIn avec l'utilisateur de test

      // Then
      expect(service.isLogged).toBe(true); // Vérification que isLogged est passé à true
      expect(service.sessionInformation).toBe(user); // Vérification que sessionInformation a été mis à jour avec l'utilisateur de test
      expect(observerSpy.getLastValue()).toBe(true); // Vérification que l'Observable a émis la valeur true
    });
  });

  // Test de la méthode logOut
  describe('logOut', () => {
    // Test pour vérifier que la méthode logOut met à jour isLogged et sessionInformation correctement
    it('should update isLogged to false and sessionInformation to undefined when logOut is called', () => {
      // Given
      const user: SessionInformation = {
        token: 'testToken',
        type: 'testType',
        id: 1,
        username: 'testUsername',
        firstName: 'testFirstName',
        lastName: 'testLastName',
        admin: false
      };
      service.logIn(user); // Nous devons d'abord connecter l'utilisateur pour pouvoir le déconnecter
      const observerSpy = new ObserverSpy<boolean>(); // Création d'un espion d'observateur pour espionner l'Observable retourné par la méthode $isLogged
      service.$isLogged().subscribe(observerSpy); // Abonnement à l'Observable retourné par la méthode $isLogged et espionnage de cet Observable

      // When
      service.logOut(); // Appel de la méthode logOut

      // Then
      expect(service.isLogged).toBe(false); // Vérification que isLogged est passé à false
      expect(service.sessionInformation).toBeUndefined(); // Vérification que sessionInformation est devenu undefined
      expect(observerSpy.getLastValue()).toBe(false); // Vérification que l'Observable a émis la valeur false
    });
  });
});
