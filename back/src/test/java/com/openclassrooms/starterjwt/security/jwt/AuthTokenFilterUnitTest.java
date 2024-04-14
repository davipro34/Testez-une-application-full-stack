package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class AuthTokenFilterUnitTest {

    @InjectMocks
    AuthTokenFilter authTokenFilter; // Instance de la classe à tester

    @Mock
    JwtUtils jwtUtils; // Mock de JwtUtils

    @Mock
    UserDetailsService userDetailsService; // Mock de UserDetailsService

    @Mock
    HttpServletRequest request; // Mock de HttpServletRequest

    @Mock
    HttpServletResponse response; // Mock de HttpServletResponse

    @Mock
    FilterChain filterChain; // Mock de FilterChain

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialise les mocks
        SecurityContext securityContext = Mockito.mock(SecurityContext.class); // Crée un mock de SecurityContext
        SecurityContextHolder.setContext(securityContext); // Définit le SecurityContext pour SecurityContextHolder
    }

    @Test
    public void testDoFilterInternal() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer token"); // Configure le mock pour retourner "Bearer token" lors de l'appel à getHeader("Authorization")
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true); // Configure le mock pour retourner true lors de la validation du token
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username"); // Configure le mock pour retourner "username" lors de l'extraction du nom d'utilisateur du token
        UserDetails userDetails = mock(UserDetails.class); // Crée un mock de UserDetails
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails); // Configure le mock pour retourner notre mock de UserDetails lors de l'appel à loadUserByUsername()

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain); // Appelle la méthode à tester

        // Assert
        verify(filterChain, times(1)).doFilter(request, response); // Vérifie que la méthode doFilter() du mock de FilterChain a été appelée une fois avec les arguments request et response
    }

    @Test
    public void testDoFilterInternal_NoAuthorizationHeader() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null); // Configure le mock pour retourner null lors de l'appel à getHeader("Authorization")

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain); // Appelle la méthode à tester

        // Assert
        verify(filterChain, times(1)).doFilter(request, response); // Vérifie que la méthode doFilter() du mock de FilterChain a été appelée une fois avec les arguments request et response
    }

    @Test
    public void testDoFilterInternal_AuthorizationHeaderNotStartingWithBearer() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Invalid token"); // Configure le mock pour retourner "Invalid token" lors de l'appel à getHeader("Authorization")

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain); // Appelle la méthode à tester

        // Assert
        verify(filterChain, times(1)).doFilter(request, response); // Vérifie que la méthode doFilter() du mock de FilterChain a été appelée une fois avec les arguments request et response
    }

    @Test
    public void testDoFilterInternal_InvalidJwtToken() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer token"); // Configure le mock pour retourner "Bearer token" lors de l'appel à getHeader("Authorization")
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(false); // Configure le mock pour retourner false lors de la validation du token

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain); // Appelle la méthode à tester

        // Assert
        verify(filterChain, times(1)).doFilter(request, response); // Vérifie que la méthode doFilter() du mock de FilterChain a été appelée une fois avec les arguments request et response
    }

    @Test
    public void testDoFilterInternal_ExceptionWhenLoadingUserByUsername() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer token"); // Configure le mock pour retourner "Bearer token" lors de l'appel à getHeader("Authorization")
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true); // Configure le mock pour retourner true lors de la validation du token
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username"); // Configure le mock pour retourner "username" lors de l'extraction du nom d'utilisateur du token
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found")); // Configure le mock pour lancer une exception lors de l'appel à loadUserByUsername()

        // Act
        authTokenFilter.doFilterInternal(request, response, filterChain); // Appelle la méthode à tester

        // Assert
        verify(filterChain, times(1)).doFilter(request, response); // Vérifie que la méthode doFilter() du mock de FilterChain a été appelée une fois avec les arguments request et response
    }
}