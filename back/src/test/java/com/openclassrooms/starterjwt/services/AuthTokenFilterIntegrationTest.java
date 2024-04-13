package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTokenFilterIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate; // Injecte un TestRestTemplate pour envoyer des requêtes HTTP

    @MockBean
    private JwtUtils jwtUtils; // Mocke JwtUtils pour contrôler son comportement

    @MockBean
    private UserDetailsServiceImpl userDetailsService; // Mocke UserDetailsServiceImpl pour contrôler son comportement

    @Test
    public void testDoFilterInternal_ValidJwtTokenAndUserExists() {
        // Given
        // On configure le mock jwtUtils pour retourner true lors de la validation du token
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        // On configure le mock jwtUtils pour retourner "username" lors de l'extraction du nom d'utilisateur du token
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username");
        UserDetails userDetails = mock(UserDetails.class); // On crée un mock de UserDetails
        // On configure le mock userDetails pour retourner une liste vide lors de l'appel à getAuthorities()
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());
        // On configure le mock userDetailsService pour retourner notre mock de UserDetails lors de l'appel à loadUserByUsername()
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);

        HttpHeaders headers = new HttpHeaders(); // On crée un nouvel objet HttpHeaders
        // On ajoute un en-tête d'autorisation avec un token fictif
        headers.set("Authorization", "Bearer token");
        // On crée une nouvelle entité Http avec notre corps et nos en-têtes
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        // When
        // On envoie une requête GET à "/api/session" avec notre entité Http
        ResponseEntity<String> response = restTemplate.exchange("/api/session", HttpMethod.GET, entity, String.class);

        // Then
        // On vérifie que le statut de la réponse est 200 OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // On vérifie que la méthode loadUserByUsername() a été appelée une fois avec "username" comme argument
        verify(userDetailsService, times(1)).loadUserByUsername("username");
    }
}