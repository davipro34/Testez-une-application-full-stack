import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { of } from 'rxjs';
import { ObserverSpy } from '@hirez_io/observer-spy';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';

describe('AppComponent', () => {
  let app: AppComponent;
  let authService: AuthService;
  let service: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: '', component: AppComponent}
        ]),
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    // Initialisation des services
    service = TestBed.inject(SessionService);
    authService = TestBed.inject(AuthService);
    const fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
  });

  // Test de la fonction $isLogged() et de son interaction avec SessionService.
  it('should return the correct logged in status', () => {
    // Given
    // Création d'un spy sur la fonction $isLogged() de SessionService
    // et simulation de son comportement pour retourner un Observable de true
    jest.spyOn(service, '$isLogged').mockReturnValue(of(true));

    // When
    // Appel de la fonction $isLogged() de AppComponent
    const isLogged$ = app.$isLogged();

    // Then
    // Vérification que la fonction $isLogged() de SessionService a été appelée
    expect(service.$isLogged).toHaveBeenCalled();

    // Création d'un ObserverSpy pour espionner l'Observable
    const observerSpy = new ObserverSpy();
    isLogged$.subscribe(observerSpy);

    // Vérification que la fonction $isLogged() de AppComponent retourne le bon résultat
    expect(observerSpy.getLastValue()).toBe(true);
  });

  // Test de la fonction logout() et de son interaction avec SessionService et Router.
  it('should log out the user and navigate to the root', () => {
    // Given
    // Création d'un spy sur la fonction logOut() de SessionService
    // et simulation de son comportement pour qu'elle ne fasse rien (puisqu'elle ne retourne rien)
    jest.spyOn(service, 'logOut').mockImplementation(() => {});

    // Récupération de l'instance de Router injectée dans le TestBed
    const router = TestBed.inject(Router);

    // Création d'un spy sur la fonction navigate() de Router
    // et simulation de son comportement pour qu'elle ne fasse rien (puisqu'elle retourne un Promise)
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

    // When
    // Appel de la fonction logout() de AppComponent
    app.logout();

    // Then
    // Vérification que la fonction logOut() de SessionService a été appelée
    expect(service.logOut).toHaveBeenCalled();

    // Vérification que la fonction navigate() de Router a été appelée avec le bon argument
    expect(router.navigate).toHaveBeenCalledWith(['']);
  });

});