package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JwtUtilsUnitTest {

    @Test
    public void testGetUserNameFromJwtToken() {
        // Arrange : Créer une instance de JwtUtils et injecter le jwtSecret
        JwtUtils jwtUtils = new JwtUtils();
        String jwtSecret = "testSecret";
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);

        String username = "testUser";

        // Générer un vrai token JWT avec le nom d'utilisateur spécifié
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000)) // 1 jour d'expiration
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Act : Appeler getUserNameFromJwtToken avec le vrai token
        String returnedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Assert : Vérifier que le nom d'utilisateur retourné est le même que celui que nous avons défini
        assertEquals(username, returnedUsername);
    }

    @Test
    public void testValidateJwtToken() {
        // Arrange
        // Création d'une instance de JwtUtils
        JwtUtils jwtUtils = new JwtUtils();
        // Définition du secret JWT utilisé pour signer le token
        String jwtSecret = "testSecret";
        // Injection du secret JWT dans l'instance de JwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        // Définition du nom d'utilisateur qui sera utilisé pour générer le token JWT
        String username = "testUser";
        // Génération d'un token JWT valide
        String validToken = Jwts.builder()
                .setSubject(username) // Définition du sujet du token (le nom d'utilisateur)
                .setIssuedAt(new Date()) // Définition de la date d'émission du token
                .setExpiration(new Date((new Date()).getTime() + 86400000)) // Définition de la date d'expiration du token (1 jour après la date d'émission)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Signature du token avec le secret JWT
                .compact(); // Construction du token
        // Définition d'un token JWT invalide
        String invalidToken = "invalidToken";

        // Act
        // Validation du token JWT valide
        boolean isValid = jwtUtils.validateJwtToken(validToken);
        // Tentative de validation du token JWT invalide
        boolean isInvalid = jwtUtils.validateJwtToken(invalidToken);

        // Assert
        // Vérification que la validation du token JWT valide a renvoyé true
        assertTrue(isValid);
        // Vérification que la validation du token JWT invalide a renvoyé false
        assertFalse(isInvalid);
    }
    
    @Test
    public void testValidateJwtTokenWithInvalidSignature() {
        // Arrange
        // Création d'une instance de JwtUtils
        JwtUtils jwtUtils = new JwtUtils();
        // Définition du secret JWT utilisé pour signer le token
        String jwtSecret = "testSecret";
        // Injection du secret JWT dans l'instance de JwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        // Génération d'un token JWT avec une signature invalide
        String invalidSignatureToken = Jwts.builder()
                .setSubject("testUser")
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();
    
        // Act & Assert
        // Tentative de validation du token JWT avec une signature invalide
        // Vérification que la validation a renvoyé false
        assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));
    }
    
    @Test
    public void testValidateJwtTokenWithMalformedToken() {
        // Arrange
        // Création d'une instance de JwtUtils
        JwtUtils jwtUtils = new JwtUtils();
        // Injection du secret JWT dans l'instance de JwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        // Définition d'un token JWT malformé
        String malformedToken = "malformedToken";
    
        // Act & Assert
        // Tentative de validation du token JWT malformé
        // Vérification que la validation a renvoyé false
        assertFalse(jwtUtils.validateJwtToken(malformedToken));
    }
    
    @Test
    public void testValidateJwtTokenWithExpiredToken() {
        // Arrange
        // Création d'une instance de JwtUtils
        JwtUtils jwtUtils = new JwtUtils();
        // Définition du secret JWT utilisé pour signer le token
        String jwtSecret = "testSecret";
        // Injection du secret JWT dans l'instance de JwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        // Génération d'un token JWT expiré
        String expiredToken = Jwts.builder()
                .setSubject("testUser")
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    
        // Act & Assert
        // Tentative de validation du token JWT expiré
        // Vérification que la validation a renvoyé false
        assertFalse(jwtUtils.validateJwtToken(expiredToken));
    }

    @Test
    public void testValidateJwtTokenWithUnsupportedToken() {
        // Arrange
        // Création d'une instance de JwtUtils
        JwtUtils jwtUtils = new JwtUtils();
        // Définition du secret JWT utilisé pour signer le token
        String jwtSecret = "testSecret";
        // Injection du secret JWT dans l'instance de JwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        // Génération d'un token JWT sans signature
        String unsupportedToken = Jwts.builder()
                .setSubject("testUser")
                .compact();
    
        // Act & Assert
        // Tentative de validation du token JWT sans signature
        // Vérification que la validation a renvoyé false
        assertFalse(jwtUtils.validateJwtToken(unsupportedToken));
    }
    
    @Test
    public void testValidateJwtTokenWithEmptyClaims() {
        // Arrange
        // Création d'une instance de JwtUtils
        JwtUtils jwtUtils = new JwtUtils();
        // Définition du secret JWT utilisé pour signer le token
        String jwtSecret = "testSecret";
        // Injection du secret JWT dans l'instance de JwtUtils
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
        // Définition d'un token JWT avec des claims vides
        String emptyClaimsToken = "";
    
        // Act & Assert
        // Tentative de validation du token JWT avec des claims vides
        // Vérification que la validation a renvoyé false
        assertFalse(jwtUtils.validateJwtToken(emptyClaimsToken));
    }
}