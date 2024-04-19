// Importation des modules nécessaires pour les tests
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { NgZone } from '@angular/core';

import { FormComponent } from './form.component';
import { Session } from 'src/app/features/sessions/interfaces/session.interface';

// Début de la suite de tests pour le composant FormComponent
describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  let route : ActivatedRoute;
  let ngZone: NgZone;
  let httpTestingController: HttpTestingController;

  // Mocks pour les services et les données utilisés dans les tests
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 
  const mockSession: Session = {
    id: 1,
    name: 'my session',
    description: '....',
    date: new Date(),
    teacher_id: 25,
    users: [1, 5, 9],
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: FormComponent}
        ]),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    // Création du composant et du fixture pour les tests
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    ngZone = TestBed.inject(NgZone);
    httpTestingController = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  // Test pour vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Suite de tests pour la méthode ngOnInit
  describe('ngOnInit', () => {
    // Test pour vérifier que la méthode initForm est appelée quand l'admin n'est pas en session et que l'URL ne contient pas 'update'
    it('call without session admin and update in url', () => {
      // Given
      const myPrivateFuncInitForm = jest.spyOn(component as any, 'initForm');
      
      // When
      component.ngOnInit();
      
      // Then
      expect(myPrivateFuncInitForm).toHaveBeenCalled();
    })

    // Test pour vérifier que la méthode initForm est appelée quand l'admin n'est pas en session et que l'URL contient 'update'
    it('call without session admin with update in url', () => {
      // Given
      component.onUpdate = false;
      jest.spyOn(router, 'url', 'get').mockReturnValue('update');
      jest.spyOn(route.snapshot.paramMap, 'get').mockReturnValue('1');
      const myPrivateFuncExitPage = jest.spyOn(component as any, 'initForm');
      const id = '1'
      
      // When
      component.ngOnInit();
      
      // Then
      expect(component.onUpdate).toBe(true);
      const req = httpTestingController.expectOne(`api/session/${id}`);
      expect(req.request.method).toEqual('GET');
      req.flush(mockSession);
      expect(myPrivateFuncExitPage).toBeCalledWith(mockSession);
    })
  
    // Test pour vérifier que l'utilisateur est redirigé vers '/sessions' quand l'admin est en session
    it('call with session admin and redirect to session', () => {
      // Given
      mockSessionService.sessionInformation.admin = false;
      const navigateSpy = jest.spyOn(router,'navigate');
      
      // When
      ngZone.run(() => {
        component.ngOnInit();
        
        // Then
        expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
      })
    })
  });

  // Suite de tests pour la méthode submit
  describe('submit', () => {
    // Test pour vérifier que la méthode submit appelle la bonne API quand onUpdate est true
    it('should call submit with onUpdate true', async () => {
      // Given
      const myPrivateFuncExitPage = jest.spyOn(component as any, 'exitPage');
      myPrivateFuncExitPage.mockImplementation(() => {});
      
      // When
      expect(component.submit()).toBe(void 0)
      
      // Then
      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toEqual('POST');
      req.flush(mockSession);
      expect(myPrivateFuncExitPage).toBeCalledWith("Session created !");
    });
    
    // Test pour vérifier que la méthode submit appelle la bonne API quand onUpdate est false
    it('should call submit with onUpdate false', () => {
      // Given
      const myPrivateFuncExitPage = jest.spyOn(component as any, 'exitPage');
      myPrivateFuncExitPage.mockImplementation(() => {});
      component.onUpdate = true;
      const id = '1'
      Object.defineProperty(component, 'id', { value: '1'})

      // When
      expect(component.submit()).toBe(void 0)
      
      // Then
      const req = httpTestingController.expectOne(`api/session/${id}`);
      expect(req.request.method).toEqual('PUT');
      req.flush(true);
      expect(myPrivateFuncExitPage).toBeCalledWith("Session updated !");
    });
  });
});