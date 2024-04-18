import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';

// Importation du composant à tester
import { FormComponent } from './form.component';

// Début de la suite de tests pour FormComponent
describe('FormComponent', () => {
  // Déclaration des variables nécessaires pour les tests
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let ngZone: NgZone;
  let spy: jest.SpyInstance;

  // Création d'un mock pour le service SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  // Configuration de l'environnement de test avant chaque test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService }, // Utilisation du mock pour le service SessionService
        SessionApiService
      ],
      declarations: [FormComponent] // Déclaration du composant à tester
    })
      .compileComponents();

    // Configuration du mock pour simuler un utilisateur non-admin
    mockSessionService.sessionInformation = { admin: false };

    // Injection du service Router et création d'un espion pour la méthode navigate
    const router = TestBed.inject(Router);
    spy = jest.spyOn(router, 'navigate');
    
    // Création du composant à tester
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    ngZone = TestBed.inject(NgZone); // Injection de NgZone pour pouvoir l'utiliser dans les tests
  });

  // Test pour vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que le composant navigue vers '/sessions' si l'utilisateur n'est pas un admin
  it('should navigate to /sessions if user is not admin', () => { // Utilisation de ngZone.run pour s'assurer que toutes les opérations asynchrones sont terminées avant de résoudre la promesse
    return ngZone.run(() => new Promise<void>((resolve) => {
      // Appel de la méthode à tester
      component.ngOnInit();
    
      // Vérification que la méthode navigate a été appelée avec le bon argument
      expect(spy).toHaveBeenCalledWith(['/sessions']);
    
      resolve();
    }));
  });
});