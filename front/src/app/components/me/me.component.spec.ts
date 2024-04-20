import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs/internal/observable/of';
import { User } from 'src/app/interfaces/user.interface';

// Début de la suite de tests pour le composant MeComponent
describe('MeComponent', () => {
  // Déclaration des variables nécessaires pour les tests
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;

  // Mock du service SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Configuration du module de test
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    // Création du composant et du fixture pour les tests
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    // Injection des services nécessaires
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);

    // Détection des changements initiaux
    fixture.detectChanges();
  });

  // Test de création du composant
  it('should create', () => {
    // Then
    expect(component).toBeTruthy();
  });

  // Test de la méthode back
  it('should navigate back when back method is called', () => {
    // Given
    // Crée un espion sur la méthode window.history.back
    const spy = jest.spyOn(window.history, 'back');

    // When
    // Appelle la méthode back
    component.back();

    // Then
    // Vérifie que la méthode window.history.back a été appelée
    expect(spy).toHaveBeenCalled();

    // Nettoie l'espion
    spy.mockRestore();
  });

  // Test de la méthode ngOnInit
  it('should update user when ngOnInit is called', () => {
    // Given
    // Crée un utilisateur mock
    const user: User = { 
      id: 1, 
      email: 'test@example.com', 
      lastName: 'User', 
      firstName: 'Test', 
      admin: false,
      password: 'test-password',
      createdAt: new Date()
    };
    // Définit les informations de session
    sessionService.sessionInformation = { 
      id: 1, 
      token: 'test-token', 
      type: 'test-type', 
      username: 'test-user', 
      firstName: 'Test', 
      lastName: 'User', 
      admin: false 
    };
    // Crée un espion sur la méthode getById du service UserService
    jest.spyOn(userService, 'getById').mockReturnValue(of(user));
    
    // When
    // Appelle la méthode ngOnInit
    component.ngOnInit();
    
    // Then
    // Vérifie que l'utilisateur du composant est égal à l'utilisateur mock
    expect(component.user).toEqual(user);
  });
});