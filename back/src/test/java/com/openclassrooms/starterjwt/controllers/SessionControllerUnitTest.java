package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest // Indique que c'est une classe de test pour Spring Boot
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
public class SessionControllerUnitTest {

    @Autowired // Injecte un MockMvc
    private MockMvc mockMvc;

    @Autowired // Injecte le contrôleur à tester
    private SessionController sessionController;

    @MockBean // Crée un mock pour le service SessionService
    private SessionService sessionService;

    @MockBean // Crée un mock pour le mapper SessionMapper
    private SessionMapper sessionMapper;

    private Session session; // Instance de Session pour les tests
    private SessionDto sessionDto; // Instance de SessionDto pour les tests
    private ObjectMapper mapper; // ObjectMapper pour convertir les objets en JSON et vice versa

    @BeforeEach // Exécuté avant chaque test
    public void setup() {
        // Arrange
        session = new Session(); // Initialise une nouvelle session
        session.setId(1L); // Définit l'ID de la session

        sessionDto = new SessionDto(); // Initialise un nouveau SessionDto
        sessionDto.setId(1L); // Définit l'ID du SessionDto

        mapper = new ObjectMapper(); // Initialise un nouvel ObjectMapper
        mapper.registerModule(new JavaTimeModule()); // Enregistre le module JavaTime pour la sérialisation/désérialisation des dates
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Désactive la conversion des dates en timestamps
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testFindById_Unit() throws Exception {
        // Arrange
        when(sessionService.getById(1L)).thenReturn(session); // Mocke le comportement de sessionService.getById
        when(sessionMapper.toDto(session)).thenReturn(sessionDto); // Mocke le comportement de sessionMapper.toDto
        // Act & Assert
        mockMvc.perform(get("/api/session/{id}", 1L)) // Effectue une requête GET sur /api/session/{id}
               .andExpect(status().isOk()) // Attend un statut 200 OK
               .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Attend un contenu de type application/json
               .andExpect(jsonPath("$.id", is(1))); // Attend que l'ID de la réponse soit 1
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testFindById_Unit_SessionNotFound() throws Exception {
        // Arrange
        Long id = 1L; // Définit l'ID de la session à chercher
        when(sessionService.getById(id)).thenThrow(new NotFoundException()); // Mocke le comportement de sessionService.getById pour qu'il lance une exception NotFoundException
        // Act & Assert
        mockMvc.perform(get("/api/session/{id}", id)) // Effectue une requête GET sur /api/session/{id}
            .andExpect(status().isNotFound()); // Attend un statut 404 Not Found
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testFindAll_Unit() throws Exception {
        // Arrange
        List<Session> sessions = new ArrayList<>(); // Crée une liste de sessions
        sessions.add(session); // Ajoute la session à la liste

        Session session2 = new Session(); // Crée une deuxième session
        session2.setId(2L); // Définit l'ID de la deuxième session
        sessions.add(session2); // Ajoute la deuxième session à la liste

        List<SessionDto> sessionDtos = sessions.stream() // Convertit la liste de sessions en une liste de SessionDto
            .map(session -> {
                SessionDto dto = new SessionDto(); // Crée un nouveau SessionDto
                dto.setId(session.getId()); // Définit l'ID du SessionDto
                return dto;
            }).collect(Collectors.toList());

        when(sessionService.findAll()).thenReturn(sessions); // Mocke le comportement de sessionService.findAll
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos); // Mocke le comportement de sessionMapper.toDto
        // Act & Assert
        mockMvc.perform(get("/api/session")) // Effectue une requête GET sur /api/session
            .andExpect(status().isOk()) // Attend un statut 200 OK
            .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Attend un contenu de type application/json
            .andExpect(jsonPath("$", hasSize(2))) // Attend que la réponse contienne 2 éléments
            .andExpect(jsonPath("$[0].id", is(1))) // Attend que l'ID du premier élément soit 1
            .andExpect(jsonPath("$[1].id", is(2))); // Attend que l'ID du deuxième élément soit 2

        verify(sessionService, times(1)).findAll(); // Vérifie que sessionService.findAll a été appelé une fois
        verify(sessionMapper, times(1)).toDto(sessions); // Vérifie que sessionMapper.toDto a été appelé une fois
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testCreate_Unit() throws Exception {
        // Arrange
        sessionDto.setName("Test Session"); // Définit le nom de la session
        sessionDto.setDate(new Date()); // Définit la date de la session
        sessionDto.setTeacher_id(1L); // Définit l'ID de l'enseignant de la session
        sessionDto.setDescription("Test Description"); // Définit la description de la session
        sessionDto.setUsers(Arrays.asList(1L, 2L)); // Définit les utilisateurs de la session
        sessionDto.setCreatedAt(LocalDateTime.now()); // Définit la date de création de la session
        sessionDto.setUpdatedAt(LocalDateTime.now()); // Définit la date de mise à jour de la session

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session); // Mocke le comportement de sessionMapper.toEntity
        when(sessionService.create(session)).thenReturn(session); // Mocke le comportement de sessionService.create
        when(sessionMapper.toDto(session)).thenReturn(sessionDto); // Mocke le comportement de sessionMapper.toDto
        // Act & Assert
        mockMvc.perform(post("/api/session") // Effectue une requête POST sur /api/session
                .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête
                .content(mapper.writeValueAsString(sessionDto))) // Convertit le SessionDto en JSON et l'ajoute au corps de la requête
                .andExpect(status().isOk()) // Attend un statut 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Attend un contenu de type application/json
                .andExpect(jsonPath("$.id", is(1))); // Attend que l'ID de la réponse soit 1

        verify(sessionService, times(1)).create(any(Session.class)); // Vérifie que sessionService.create a été appelé une fois
        verify(sessionMapper, times(1)).toDto(any(Session.class)); // Vérifie que sessionMapper.toDto a été appelé une fois
    }

    @Test // Indique que c'est une méthode de test
    public void testUpdate_Unit() {
        // Arrange
        sessionDto.setName("Test Session"); // Définit le nom de la session
        sessionDto.setDate(new Date()); // Définit la date de la session
        sessionDto.setTeacher_id(1L); // Définit l'ID de l'enseignant de la session
        sessionDto.setDescription("This is a test session."); // Définit la description de la session
        sessionDto.setUsers(Arrays.asList(2L, 3L)); // Définit les utilisateurs de la session

        session.setName("Test Session"); // Définit le nom de la session
        session.setDate(new Date()); // Définit la date de la session
        session.setTeacher(new Teacher(1L, "DELAHAYE", "Margot", null, null)); // Définit l'enseignant de la session
        session.setDescription("This is a test session."); // Définit la description de la session
        session.setUsers(Arrays.asList(
            new User(2L, "john@email", "DOE", "John", "password", false, null, null), 
            new User(3L, "jane@email", "DOE", "Jane", "password", false, null, null)
        )); // Définit les utilisateurs de la session

        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session); // Mocke le comportement de sessionService.update
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session); // Mocke le comportement de sessionMapper.toEntity
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto); // Mocke le comportement de sessionMapper.toDto
        // Act
        ResponseEntity<?> response = sessionController.update("1", sessionDto); // Appelle la méthode update du contrôleur
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Vérifie que le statut de la réponse est 200 OK
        assertTrue(response.getBody() instanceof SessionDto); // Vérifie que le corps de la réponse est une instance de SessionDto
        assertEquals(sessionDto, response.getBody()); // Vérifie que le corps de la réponse est égal au SessionDto

        verify(sessionService, times(1)).update(anyLong(), any(Session.class)); // Vérifie que sessionService.update a été appelé une fois
        verify(sessionMapper, times(1)).toEntity(any(SessionDto.class)); // Vérifie que sessionMapper.toEntity a été appelé une fois
        verify(sessionMapper, times(1)).toDto(any(Session.class)); // Vérifie que sessionMapper.toDto a été appelé une fois
    }
}