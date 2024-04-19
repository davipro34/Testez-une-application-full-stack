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
import { ActivatedRoute, Router } from '@angular/router';
import { NgZone } from '@angular/core';
import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs/internal/observable/of';

// Début de la suite de tests pour FormComponent
describe('FormComponent', () => {
  // Déclaration des variables nécessaires pour les tests
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let ngZone: NgZone;
  let sessionApiService: SessionApiService;
  let route: ActivatedRoute;

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

    // Injection du service Router
    router = TestBed.inject(Router);
    
    // Création du composant à tester
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    ngZone = TestBed.inject(NgZone); // Injection de NgZone pour pouvoir l'utiliser dans les tests
    sessionApiService = TestBed.inject(SessionApiService);
    route = TestBed.inject(ActivatedRoute);
  });

  // Test pour vérifier que le composant est bien créé
  it('should create', () => {
    // Then
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que le composant navigue vers '/sessions' si l'utilisateur n'est pas un admin
  it('should navigate to /sessions if user is not admin', () => { 
    // Given
    // Création d'un espion pour la méthode navigate
    const navigateSpy = jest.spyOn(router, 'navigate');

    // When
    // Utilisation de ngZone.run pour s'assurer que toutes les opérations asynchrones sont terminées avant de résoudre la promesse
    return ngZone.run(() => new Promise<void>((resolve) => {
      // Appel de la méthode à tester
      component.ngOnInit();
    
      // Then
      // Vérification que la méthode navigate a été appelée avec le bon argument
      expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    
      resolve();
    }));
  });

  // Test pour vérifier que le formulaire est initialisé avec des valeurs par défaut en mode création
  it('should initialize form with default values when in create mode', () => {
    // Given
    // Configuration du mock pour simuler un utilisateur admin et une URL de création
    mockSessionService.sessionInformation = { admin: true };
    jest.spyOn(router, 'url', 'get').mockReturnValue('/create');
  
    // When
    // Appel de la méthode à tester
    component.ngOnInit();
  
    // Then
    // Vérification que le formulaire est initialisé avec des valeurs par défaut
    expect(component.sessionForm?.value).toEqual({
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
  });
  
  // Test pour vérifier que le formulaire est initialisé avec les valeurs de la session en mode mise à jour
  it('should initialize form with session values when in update mode', () => {
    // Given
    // Configuration du mock pour simuler un utilisateur admin, une URL de mise à jour et une session existante
    const session: Session = { name: 'test', description: 'test', date: new Date(), teacher_id: 1, users: [] };
    mockSessionService.sessionInformation = { admin: true };
    jest.spyOn(router, 'url', 'get').mockReturnValue('/update');
    route.snapshot.paramMap.get = jest.fn().mockReturnValue('1');
    jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(session));
  
    // When
    // Appel de la méthode à tester
    component.ngOnInit();
  
    // Then
    // Vérification que le formulaire est initialisé avec les valeurs de la session
    expect(component.sessionForm?.value).toEqual({
      name: session.name,
      date: session.date.toISOString().split('T')[0],
      teacher_id: session.teacher_id, // Removed .toString()
      description: session.description,
    });
  });
});