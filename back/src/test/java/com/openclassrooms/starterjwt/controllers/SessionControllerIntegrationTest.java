package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest // Indique que c'est un test qui nécessite le contexte Spring
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
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
}
