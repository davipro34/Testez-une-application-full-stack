import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
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

  it('should create the app', () => {
    expect(app).toBeTruthy();
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

});