import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { ObserverSpy } from '@hirez_io/observer-spy';

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

});
