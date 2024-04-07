package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest // Indique que c'est un test qui nécessite le contexte Spring
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
public class SessionControllerUnitTest {

    @Autowired // Injecte un MockMvc depuis le contexte Spring
    private MockMvc mockMvc;

    @MockBean // Crée un mock pour SessionService
    private SessionService sessionService;

    @MockBean // Crée un mock pour SessionMapper
    private SessionMapper sessionMapper;

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_Unit() throws Exception {
        // Arrange
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        // Configure les mocks pour retourner des objets spécifiques
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act & Assert
        // Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session/{id}"
        mockMvc.perform(get("/api/session/{id}", 1L))
               .andExpect(status().isOk()) // Vérifie que le statut de la réponse est OK
               .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que le type de contenu est JSON
               .andExpect(jsonPath("$.id", is(1))); // Vérifie que l'ID de la session dans le corps de la réponse est 1
    }

    @Test
    @WithMockUser
    public void testFindById_Unit_SessionNotFound() throws Exception {
        Long id = 1L;

        // Configure le mock pour lancer une exception lorsque getById est appelé
        when(sessionService.getById(id)).thenThrow(new NotFoundException());

        // Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session/{id}"
        mockMvc.perform(get("/api/session/{id}", id))
            .andExpect(status().isNotFound()); // Vérifie que le statut de la réponse est 404 NOT FOUND
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindAll_Unit() throws Exception {
        // Arrange
        List<Session> sessions = new ArrayList<>();
        Session session1 = new Session();
        session1.setId(1L);
        sessions.add(session1);

        Session session2 = new Session();
        session2.setId(2L);
        sessions.add(session2);

        List<SessionDto> sessionDtos = sessions.stream()
            .map(session -> {
                SessionDto dto = new SessionDto();
                dto.setId(session.getId());
                return dto;
            }).collect(Collectors.toList());

        // Configure les mocks pour retourner des objets spécifiques
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        // Act
        mockMvc.perform(get("/api/session"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)));

        // Assert
        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(sessions);
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testCreate_Unit() throws Exception {
        // Arrange
        Session session = new Session(); // Crée une nouvelle session
        session.setId(1L); // Définit l'ID de la session

        // Crée un nouveau DTO de session avec des valeurs de test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L); // Définit l'ID du DTO de session
        sessionDto.setName("Test Session"); // Définit le nom du DTO de session
        sessionDto.setDate(new Date()); // Définit la date du DTO de session
        sessionDto.setTeacher_id(1L); // Définit l'ID de l'enseignant du DTO de session
        sessionDto.setDescription("Test Description"); // Définit la description du DTO de session
        sessionDto.setUsers(Arrays.asList(1L, 2L)); // Définit les utilisateurs du DTO de session
        sessionDto.setCreatedAt(LocalDateTime.now()); // Définit la date de création du DTO de session
        sessionDto.setUpdatedAt(LocalDateTime.now()); // Définit la date de mise à jour du DTO de session

        // Configure les mocks pour retourner des objets spécifiques
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session); // Quand toEntity est appelé avec sessionDto, retourne session
        when(sessionService.create(session)).thenReturn(session); // Quand create est appelé avec session, retourne session
        when(sessionMapper.toDto(session)).thenReturn(sessionDto); // Quand toDto est appelé avec session, retourne sessionDto

        // Crée un ObjectMapper configuré pour sérialiser LocalDateTime sous forme de chaîne de caractères ISO-8601
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Act & Assert
        // Utilise MockMvc pour effectuer une requête POST sur l'URL "/api/session"
        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête à JSON
                .content(mapper.writeValueAsString(sessionDto))) // Convertit sessionDto en JSON et l'ajoute au corps de la requête
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que le type de contenu de la réponse est JSON
                .andExpect(jsonPath("$.id", is(1))); // Vérifie que l'ID de la session dans le corps de la réponse est 1

        // Vérifie que les méthodes create et toDto ont été appelées une fois
        verify(sessionService, times(1)).create(any(Session.class));
        verify(sessionMapper, times(1)).toDto(any(Session.class));
    }
}
