package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;


@SpringBootTest // Indique que c'est un test qui nécessite le contexte Spring
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
@TestPropertySource(locations = "classpath:application-test.properties") // Spécifie le fichier de propriétés à utiliser
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SessionControllerIntegrationTest {

    @Autowired // Injecte un MockMvc depuis le contexte Spring
    private MockMvc mockMvc;

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_Success() throws Exception {
        // Given : L'ID de la session à rechercher
        Long id = 1L;
        // When : Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session/{id}"
        mockMvc.perform(get("/api/session/{id}", id))
        // Then : Vérifie que le statut de la réponse est OK
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(id.intValue()))) // Vérifie que l'ID de la session dans la réponse est correct
        .andExpect(jsonPath("$.name", is(notNullValue()))) // Vérifie que le nom de la session dans la réponse n'est pas null
        .andExpect(jsonPath("$.date", is(notNullValue()))) // Vérifie que la date de la session dans la réponse n'est pas null
        .andExpect(jsonPath("$.teacher_id", is(notNullValue()))) // Vérifie que l'ID de l'enseignant de la session dans la réponse n'est pas null
        .andExpect(jsonPath("$.description", is(notNullValue()))); // Vérifie que la description de la session dans la réponse n'est pas null
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_SessionNotFound() throws Exception {
        // Given : Un ID qui n'existe pas dans la base de données
        Long id = 9999L;
        // When : Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session/{id}"
        mockMvc.perform(get("/api/session/{id}", id))
        // Then : Vérifie que le statut de la réponse est 404 (Not Found)
        .andExpect(status().isNotFound());
    }
    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindAll_Integration() throws Exception {
        // When : Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session"
        mockMvc.perform(get("/api/session"))
        // Then : Vérifie que le statut de la réponse est OK
        .andExpect(status().isOk())
        // Vérifie que la liste de sessions n'est pas vide
        .andExpect(jsonPath("$", not(empty())));
    }
    
    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testCreate_Integration() throws Exception {
        // Crée un objet SessionDto pour le test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session"); // Respecte la contrainte @Size(max = 50)
        sessionDto.setDate(new Date()); // @NotNull
        sessionDto.setTeacher_id(1L); // @NotNull
        sessionDto.setDescription("This is a test session."); // Respecte la contrainte @Size(max = 2500)
    
        // Convertit l'objet SessionDto en JSON
        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);
    
        // Utilise MockMvc pour effectuer une requête POST sur l'URL "/api/session"
        // Envoie l'objet SessionDto en JSON dans le corps de la requête
        mockMvc.perform(post("/api/session")
                .contentType(APPLICATION_JSON)
                .content(sessionDtoJson))
            .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testUpdate_Integration() throws Exception {
        // Crée un objet SessionDto pour le test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session"); // Respecte la contrainte @Size(max = 50)
        sessionDto.setDate(new Date()); // @NotNull
        sessionDto.setTeacher_id(1L); // @NotNull
        sessionDto.setDescription("This is an updated test session."); // Respecte la contrainte @Size(max = 2500)

        // Convertit l'objet SessionDto en JSON
        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);

        Long id = 1L; // L'ID de la session à mettre à jour

        // Utilise MockMvc pour effectuer une requête PUT sur l'URL "/api/session/{id}"
        // Envoie l'objet SessionDto en JSON dans le corps de la requête
        mockMvc.perform(put("/api/session/{id}", id)
                .contentType(APPLICATION_JSON)
                .content(sessionDtoJson))
            .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testDelete_Integration() throws Exception {
        Long id = 1L; // L'ID de la session à supprimer

        // Utilise MockMvc pour effectuer une requête DELETE sur l'URL "/api/session/{id}"
        mockMvc.perform(delete("/api/session/{id}", id))
            .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testParticipate_Integration() throws Exception {
        Long sessionId = 1L; // L'ID de la session à laquelle participer
        Long userId = 2L; // L'ID de l'utilisateur qui participe

        // Utilise MockMvc pour effectuer une requête POST sur l'URL "/api/session/{id}/participate/{userId}"
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId))
            .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testNoLongerParticipate_Integration() throws Exception {
        Long sessionId = 2L; // L'ID de la session à laquelle l'utilisateur ne participe plus
        Long userId = 3L; // L'ID de l'utilisateur qui ne participe plus

        // Utilise MockMvc pour effectuer une requête DELETE sur l'URL "/api/session/{id}/participate/{userId}"
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId))
            .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
    }
}
