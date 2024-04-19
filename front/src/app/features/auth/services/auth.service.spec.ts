import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';

// Début de la suite de tests pour AuthService
describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  // Avant chaque test, nous configurons le module de test et injectons AuthService et HttpTestingController
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Nous utilisons HttpClientTestingModule pour mocker HttpClient
      providers: [AuthService] // Nous testons AuthService
    });

    service = TestBed.inject(AuthService); // Nous obtenons une instance de AuthService
    httpMock = TestBed.inject(HttpTestingController); // Nous obtenons une instance de HttpTestingController
  });

  // Après chaque test, nous vérifions qu'il n'y a pas de requêtes HTTP en suspens
  afterEach(() => {
    httpMock.verify();
  });

  // Test pour la méthode register
  it('should register a user', () => {
    // Given
    // Nous créons une instance de RegisterRequest
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'Test',
      lastName: 'User',
      password: 'testPassword'
    };

    // When
    // Nous appelons la méthode register avec notre instance de RegisterRequest
    service.register(registerRequest).subscribe();

    // Then
    // Nous vérifions que la requête HTTP a été envoyée à la bonne URL avec la bonne méthode et le bon corps de requête
    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerRequest);

    // Nous simulons une réponse du serveur
    req.flush({});
  });
});