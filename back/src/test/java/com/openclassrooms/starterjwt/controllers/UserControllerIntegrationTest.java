package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest // Indique que c'est un test qui nécessite le contexte Spring
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
@TestPropertySource(locations = "classpath:application-test.properties") // Spécifie le fichier de propriétés à utiliser pour ce test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Indique que le contexte Spring doit être rechargé avant chaque méthode de test
public class UserControllerIntegrationTest {

    @Autowired // Injecte une instance de MockMvc
    private MockMvc mockMvc;

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_Success() throws Exception {
        // Given : Les données nécessaires sont déjà dans la base de données de test
        // When
        mockMvc.perform(get("/api/user/{id}", 2L) // Effectue une requête GET à l'URL /api/user/{id} avec l'ID 2
                .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête à JSON
        // Then
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id", is(2))) // Vérifie que l'ID de l'utilisateur dans la réponse est 2
                .andExpect(jsonPath("$.email", is("john@email.com"))) // Vérifie que l'email de l'utilisateur dans la réponse est "john@email.com"
                .andExpect(jsonPath("$.lastName", is("DOE"))) // Vérifie que le nom de famille de l'utilisateur dans la réponse est "DOE"
                .andExpect(jsonPath("$.firstName", is("John"))); // Vérifie que le prénom de l'utilisateur dans la réponse est "John"
    }

    @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_UserNotFound() throws Exception {
        // Given : Aucun utilisateur avec l'ID 999 n'existe dans la base de données de test
        // When
        mockMvc.perform(get("/api/user/{id}", 999L) // Effectue une requête GET à l'URL /api/user/{id} avec l'ID 999
                .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête à JSON
        // Then
                .andExpect(status().isNotFound()); // Vérifie que le statut de la réponse est 404 (Not Found)
    }
}
