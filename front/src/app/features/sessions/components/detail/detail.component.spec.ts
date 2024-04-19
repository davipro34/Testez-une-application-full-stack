import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { Location } from '@angular/common';

import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';


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
});

