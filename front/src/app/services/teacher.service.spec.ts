import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Teacher } from '../interfaces/teacher.interface';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Utilisation de HttpClientTestingModule pour simuler HttpClient
      providers: [TeacherService]
    });

    service = TestBed.inject(TeacherService); // Injection du service à tester
    httpMock = TestBed.inject(HttpTestingController); // Injection de HttpTestingController pour contrôler les requêtes HTTP
  });

  afterEach(() => {
    httpMock.verify(); // Vérification qu'il n'y a pas de requêtes HTTP en attente après chaque test
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); // Vérification que le service a été correctement créé
  });

  it('should fetch the teacher detail', () => {
    // Given : Création d'un enseignant mocké
    const mockTeacher: Teacher = { 
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date('2022-01-01T00:00:00'),
      updatedAt: new Date('2022-01-01T00:00:00')
    };
    const id = '1'; // L'ID de l'enseignant à récupérer

    // When : Appel de la méthode à tester avec l'ID de l'enseignant
    service.detail(id).subscribe((teacher: Teacher) => {
    // Then : Vérification que l'enseignant récupéré est égal à l'enseignant mocké
      expect(teacher).toEqual(mockTeacher);
    });

    // Vérification qu'une seule requête a été envoyée à l'URL correcte
    const req = httpMock.expectOne(`api/teacher/${id}`);
    // Vérification que la méthode de la requête est GET
    expect(req.request.method).toBe('GET');

    // Envoi de la réponse mockée à la requête
    req.flush(mockTeacher);
  });
});