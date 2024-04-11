package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest // Indique que c'est un test qui nécessite le contexte Spring
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
@TestPropertySource(locations = "classpath:application-test.properties") // Spécifie le fichier de propriétés à utiliser
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Indique que le contexte Spring doit être rechargé avant chaque méthode de test
public class TeacherControllerIntegrationTest {

    @Autowired // Injecte une instance de MockMvc
    private MockMvc mockMvc;

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_Success() throws Exception {
        // Given : Les données nécessaires sont déjà dans la base de données de test
        // When
        mockMvc.perform(get("/api/teacher/{id}", 1L) // Effectue une requête GET à l'URL /api/teacher/{id} avec l'ID 1
                .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête à JSON
        // Then
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id", is(1))) // Vérifie que l'ID de l'enseignant dans la réponse est 1
                .andExpect(jsonPath("$.firstName", is("Margot"))) // Vérifie que le prénom de l'enseignant dans la réponse est "Margot"
                .andExpect(jsonPath("$.lastName", is("DELAHAYE"))); // Vérifie que le nom de l'enseignant dans la réponse est "DELAHAYE"
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_TeacherNotFound() throws Exception {
        // Given : Il n'y a pas d'enseignant avec l'ID 999 dans la base de données de test
        // When
        mockMvc.perform(get("/api/teacher/{id}", 999L)  // Effectue une requête GET à l'URL /api/teacher/{id} avec l'ID 999
                .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête à JSON
        // Then
                .andExpect(status().isNotFound()); // Vérifie que le statut de la réponse est 404 (Not Found)
    }
}