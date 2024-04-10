package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON;

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
    public void testFindById_Integration() throws Exception {
        Long id = 1L;

        // Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session/{id}"
        mockMvc.perform(get("/api/session/{id}", id))
               .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindAll_Integration() throws Exception {
        // Utilise MockMvc pour effectuer une requête GET sur l'URL "/api/session"
        mockMvc.perform(get("/api/session"))
            .andExpect(status().isOk()); // Vérifie que le statut de la réponse est OK
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
