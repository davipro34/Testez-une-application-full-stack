import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Utilisation de HttpClientTestingModule pour simuler HttpClient
      providers: [SessionApiService]
    });

    service = TestBed.inject(SessionApiService); // Injection du service à tester
    httpMock = TestBed.inject(HttpTestingController); // Injection de HttpTestingController pour contrôler les requêtes HTTP
  });

  afterEach(() => {
    httpMock.verify(); // Vérification qu'il n'y a pas de requêtes HTTP en attente après chaque test
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Vérification que le service a été correctement créé
  });

  it('should delete the session', () => {
    // Given : L'ID de la session à supprimer
    const id = '1';

    // When : Appel de la méthode à tester avec l'ID de la session
    service.delete(id).subscribe();

    // Then : Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne(`api/session/${id}`);

    expect(req.request.method).toBe('DELETE'); // Vérification que la méthode de la requête est DELETE

    req.flush(null); // Envoi de la réponse mockée à la requête
  });

  it('should create a session', () => {
    // Given : La session à créer
    const session: Session = {
      name: 'Test Session',
      description: 'This is a test session',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2, 3]
    };

    // When : Appel de la méthode à tester avec la session
    service.create(session).subscribe();

    // Then : Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne('api/session');

    expect(req.request.method).toBe('POST'); // Vérification que la méthode de la requête est POST

    expect(req.request.body).toEqual(session); // Vérification que le corps de la requête est la session

    req.flush(session); // Envoi de la réponse mockée à la requête
  });

  it('should update a session', () => {
    // Given : L'ID de la session à mettre à jour et la session mise à jour
    const id = '1';
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Test Session',
      description: 'This is an updated test session',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2, 3]
    };
  
    // When : Appel de la méthode à tester avec l'ID de la session et la session mise à jour
    service.update(id, updatedSession).subscribe();
  
    // Then : Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne(`api/session/${id}`);
  
    expect(req.request.method).toBe('PUT'); // Vérification que la méthode de la requête est PUT
  
    expect(req.request.body).toEqual(updatedSession); // Vérification que le corps de la requête est la session mise à jour
  
    req.flush(updatedSession); // Envoi de la réponse mockée à la requête
  });

  it('should participate in a session', () => {
    // Given : L'ID de la session et l'ID de l'utilisateur
    const sessionId = '1';
    const userId = '1';
  
    // When : Appel de la méthode à tester avec l'ID de la session et l'ID de l'utilisateur
    service.participate(sessionId, userId).subscribe();
  
    // Then : Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
  
    expect(req.request.method).toBe('POST'); // Vérification que la méthode de la requête est POST
  
    req.flush(null); // Envoi de la réponse mockée à la requête
  });

  it('should unparticipate from a session', () => {
    // Given : L'ID de la session et l'ID de l'utilisateur
    const sessionId = '1';
    const userId = '1';
  
    // When : Appel de la méthode à tester avec l'ID de la session et l'ID de l'utilisateur
    service.unParticipate(sessionId, userId).subscribe();
  
    // Then : Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
  
    expect(req.request.method).toBe('DELETE'); // Vérification que la méthode de la requête est DELETE
  
    req.flush(null); // Envoi de la réponse mockée à la requête
  });
});