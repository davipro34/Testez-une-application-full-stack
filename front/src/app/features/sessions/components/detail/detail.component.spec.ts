import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { Location } from '@angular/common';

import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';
import { of } from 'rxjs/internal/observable/of';
import { SessionApiService } from '../../services/session-api.service';
import { Router } from '@angular/router';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let location: Location;
  let locationMock = {
    back: jest.fn() // Mock de la méthode back de l'objet Location
  }

  // Mock du service à tester
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  // Avant chaque test, configure le module de test et crée une instance du composant et du service
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,  // Pour tester le routage
        HttpClientModule,  // Pour les requêtes HTTP
        MatSnackBarModule,  // Pour les notifications
        ReactiveFormsModule  // Pour les formulaires réactifs
      ],
      declarations: [DetailComponent], // Déclare le composant à tester
      providers: [
        { provide: SessionService, useValue: mockSessionService },  // Fournit le mock du service à tester
        { provide: Location, useValue: locationMock }  // Fournit le mock de l'objet Location
      ],
    })
      .compileComponents(); // Compile le module de test
      service = TestBed.inject(SessionService); // Injecte le service à tester
      fixture = TestBed.createComponent(DetailComponent); // Crée le composant à tester
      component = fixture.componentInstance; // Obtient l'instance du composant à tester
      location = TestBed.inject(Location); // Injecte l'objet Location
      fixture.detectChanges(); // Déclenche la détection de changements
  });

  // Teste si le composant est bien créé
  it('should create', () => {
    // Given : Le composant est créé
    // When : On vérifie l'existence du composant
    // Then : Le composant existe
    expect(component).toBeTruthy();
  });

  // Teste si la méthode back du composant appelle bien la méthode back de l'objet history
  it('should navigate back when back method is called', () => {
    // Given : On crée un espion sur la méthode back de l'objet history
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
  
    // When : On appelle la méthode back du composant
    component.back();
  
    // Then : On vérifie que la méthode back de l'objet history a été appelée
    expect(spy).toHaveBeenCalled();
  });

  it('should delete session when delete method is called', () => {
    // Given : On crée des espions sur les méthodes delete, open et navigate
    const sessionApiService = TestBed.inject(SessionApiService);
    const matSnackBar = TestBed.inject(MatSnackBar);
    const router = TestBed.inject(Router);
  
    const deleteSpy = jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(null));
    const snackBarSpy = jest.spyOn(matSnackBar, 'open').mockReturnValue(undefined as any);
    const routerSpy = jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true));
  
    // When : On appelle la méthode delete du composant
    component.delete();
  
    // Then : On vérifie que les méthodes delete, open et navigate ont été appelées
    expect(deleteSpy).toHaveBeenCalledWith(component.sessionId);
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(routerSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate when participate method is called', () => {
    // Given : On crée des espions sur les méthodes participate et fetchSession
    const sessionApiService = TestBed.inject(SessionApiService);
    const participateSpy = jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined));
    
    // On déclare fetchSession comme une méthode publique pour pouvoir créer un espion dessus
    (component as any).fetchSession = jest.fn();
  
    // When : On appelle la méthode participate du composant
    component.participate();
  
    // Then : On vérifie que les méthodes participate et fetchSession ont été appelées
    expect(participateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect((component as any).fetchSession).toHaveBeenCalled();
  });

  it('should unparticipate when unParticipate method is called', () => {
    // Given : On crée des espions sur les méthodes unParticipate et fetchSession
    const sessionApiService = TestBed.inject(SessionApiService);
    const unParticipateSpy = jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
    
    // On déclare fetchSession comme une méthode publique pour pouvoir créer un espion dessus
    (component as any).fetchSession = jest.fn();
  
    // When : On appelle la méthode unParticipate du composant
    component.unParticipate();
  
    // Then : On vérifie que les méthodes unParticipate et fetchSession ont été appelées
    expect(unParticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect((component as any).fetchSession).toHaveBeenCalled();
  });
});

