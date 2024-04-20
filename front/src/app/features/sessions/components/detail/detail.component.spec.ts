import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { NgZone } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { DetailComponent } from './detail.component';

// Début de la suite de tests pour DetailComponent
describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let serviceApi: SessionApiService;
  let httpTestingController: HttpTestingController;
  let route : ActivatedRoute;
  let ngZone: NgZone;
  let router: Router;

  // Mock pour SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  // Avant chaque test, nous configurons le module de test et injectons les dépendances
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', component: DetailComponent}
        ]),
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService }, 
        SessionApiService
      ],
    })
    .compileComponents();
    service = TestBed.inject(SessionService);
    serviceApi = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(DetailComponent);
    router = TestBed.inject(Router);
    route = TestBed.inject(ActivatedRoute);
    ngZone = TestBed.inject(NgZone);
    httpTestingController = TestBed.inject(HttpTestingController);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Test pour vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que ngOnInit appelle fetchSession
  it('should call ngOnInit', () => {
    // Given
    const mockPrivateFetchSession = jest.spyOn(component as any, 'fetchSession')
    
    // When
    component.ngOnInit()
    
    // Then
    expect(mockPrivateFetchSession).toBeCalled();
  });

  // Test pour vérifier que la méthode back appelle window.history.back
  it('should call back', () => {
    // Given
    jest.spyOn(window.history, 'back');
    
    // When
    component.back()
    
    // Then
    expect(window.history.back).toBeCalled()
  });

  // Test pour vérifier que la méthode delete envoie une requête DELETE
  it('should call delete', () => {
    // Given
    const navigateSpy = jest.spyOn(router,'navigate');
    component.sessionId = '1';
    
    // When
    expect(component.delete()).toBe(void 0);
    
    // Then
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
  });

  // Test pour vérifier que la méthode participate envoie une requête POST
  it('should call participate', () => {
    // Given
    const myPrivateFuncExitPage = jest.spyOn(component as any, 'fetchSession');
    component.sessionId = '1';
    component.userId = '1';
    
    // When
    expect(component.participate()).toBe(void 0)
    
    // Then
    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('POST');
    req.flush(true);
  });

  // Test pour vérifier que la méthode unParticipate envoie une requête DELETE
  it('should call participate', () => {
    // Given
    const myPrivateFuncExitPage = jest.spyOn(component as any, 'fetchSession');
    component.sessionId = '1';
    component.userId = '1';

    // When
    expect(component.unParticipate()).toBe(void 0)
    
    // Then
    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('DELETE');
    req.flush(true);
    expect(myPrivateFuncExitPage).toBeCalled();
  });

  // Suite de tests pour l'intégration de DetailComponent
  describe('DetailComponent integrartion suite', () => { 
    // Test pour vérifier que le détail de la session est bien rendu
    it('should render the detail session', async() => {
      expect(fixture.nativeElement.querySelector('[mat-card]')).toBeNull();
    });
  })
});