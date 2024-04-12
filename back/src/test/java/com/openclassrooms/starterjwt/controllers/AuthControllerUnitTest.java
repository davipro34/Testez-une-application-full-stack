package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void testRegisterUser_BadRequest() throws Exception {
        // Arrange
        // Création d'une nouvelle demande d'inscription
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("First");
        signupRequest.setLastName("Last");
        // Conversion de l'objet SignupRequest en chaîne JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(signupRequest);
        // Simulation du comportement de userRepository.existsByEmail pour retourner true
        // Cela signifie que l'email est déjà utilisé
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act
        // Exécution de la requête POST sur "/api/auth/register"
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(jsonRequest))

        // Assert
        // Vérification que le statut de la réponse est 400 (Bad Request)
        // et que le message de la réponse est "Error: Email is already taken!"
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"Error: Email is already taken!\"}"));
    }
}
