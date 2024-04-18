import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Utilisation de HttpClientTestingModule pour simuler HttpClient
      providers: [UserService]
    });

    service = TestBed.inject(UserService); // Injection du service à tester
    httpMock = TestBed.inject(HttpTestingController); // Injection de HttpTestingController pour contrôler les requêtes HTTP
  });

  afterEach(() => {
    httpMock.verify(); // Vérification qu'il n'y a pas de requêtes HTTP en attente après chaque test
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Vérification que le service a été correctement créé
  });

  it('should delete the user', () => {
    // Given : L'ID de l'utilisateur à supprimer
    const id = '1';

    // When : Appel de la méthode à tester avec l'ID de l'utilisateur
    service.delete(id).subscribe();

    // Then : Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne(`api/user/${id}`);
    // Vérification que la méthode de la requête est DELETE
    expect(req.request.method).toBe('DELETE');

    // Envoi de la réponse mockée à la requête
    req.flush(null);
  });
});