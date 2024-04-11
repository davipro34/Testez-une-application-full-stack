package com.openclassrooms.starterjwt.controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;

@SpringBootTest // Indique que c'est un test qui nécessite le contexte Spring
@AutoConfigureMockMvc // Configure automatiquement un MockMvc
@TestPropertySource(locations = "classpath:application-test.properties") // Spécifie le fichier de propriétés à utiliser pour ce test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Indique que le contexte Spring doit être rechargé avant chaque méthode de test
public class AuthControllerIntegrationTest {

    @Autowired // Injecte une instance de MockMvc
    private MockMvc mockMvc;

    @Test // Indique que c'est une méthode de test
    public void testLogin_Success() throws Exception {
        // Given : Un utilisateur avec l'email "john@email.com" et le mot de passe "password" existe dans la base de données de test
        // Crée une requête de connexion avec l'email et le mot de passe de l'utilisateur
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@email.com");
        loginRequest.setPassword("password");

        // Convertit la requête de connexion en JSON
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);

        // When
        mockMvc.perform(post("/api/auth/login") // Effectue une requête POST à l'URL /api/auth/login
                .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête à JSON
                .content(jsonLoginRequest)) // Définit le contenu de la requête à la requête de connexion en JSON
        // Then
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.token", is(notNullValue()))); // Vérifie que le token dans la réponse n'est pas null
    }

    @Test // Indique que c'est une méthode de test
    public void testLogin_Unauthorized() throws Exception {
        // Given : Aucun utilisateur avec l'email "wrong@email.com" ou le mot de passe "wrongpassword" n'existe dans la base de données de test

        // Crée une requête de connexion avec un email et un mot de passe incorrects
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@email.com");
        loginRequest.setPassword("wrongpassword");
        // Convertit la requête de connexion en JSON
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);
        // When
        mockMvc.perform(post("/api/auth/login") // Effectue une requête POST à l'URL /api/auth/login
                .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête à JSON
                .content(jsonLoginRequest)) // Définit le contenu de la requête à la requête de connexion en JSON
        // Then
                .andExpect(status().isUnauthorized()); // Vérifie que le statut de la réponse est 401 (Unauthorized)
    }
}