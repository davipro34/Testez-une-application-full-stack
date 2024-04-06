package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
}
