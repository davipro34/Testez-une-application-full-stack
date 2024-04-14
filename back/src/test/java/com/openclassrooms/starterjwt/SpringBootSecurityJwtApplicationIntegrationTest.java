package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class SpringBootSecurityJwtApplicationIntegrationTest {

    @Test
    public void contextLoads() {
        // Ce test vérifie que le contexte de l'application Spring Boot se charge correctement
    }

    @Test
    public void main() {
        // Arrange
        String[] args = {};

        // Act
        SpringBootSecurityJwtApplication.main(args);

        // Assert
        // Aucune assertion nécessaire car nous testons simplement que la méthode main ne génère pas d'exception
    }
}
