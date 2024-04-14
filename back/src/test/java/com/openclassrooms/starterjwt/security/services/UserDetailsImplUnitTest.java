package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UserDetailsImplUnitTest {

    @Test
    public void testAdminField() {
        // Arrange
        UserDetailsImpl adminUser = UserDetailsImpl.builder()
            .id(1L)
            .username("admin")
            .firstName("Admin")
            .lastName("User")
            .admin(true)
            .password("password")
            .build();

        UserDetailsImpl nonAdminUser = UserDetailsImpl.builder()
            .id(2L)
            .username("user")
            .firstName("Non-admin")
            .lastName("User")
            .admin(false)
            .password("password")
            .build();

        // Act & Assert
        // Vérification que le champ admin est correctement défini pour un utilisateur admin
        assertTrue(adminUser.getAdmin());

        // Vérification que le champ admin est correctement défini pour un utilisateur non-admin
        assertFalse(nonAdminUser.getAdmin());
    }

    @Test
    public void testEquals() {
        // Arrange
        UserDetailsImpl user1 = UserDetailsImpl.builder()
            .id(1L)
            .username("user1")
            .firstName("First")
            .lastName("User")
            .admin(true)
            .password("password")
            .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
            .id(1L)
            .username("user2")
            .firstName("Second")
            .lastName("User")
            .admin(false)
            .password("password")
            .build();

        UserDetailsImpl user3 = UserDetailsImpl.builder()
            .id(2L)
            .username("user3")
            .firstName("Third")
            .lastName("User")
            .admin(true)
            .password("password")
            .build();

        // Act & Assert
        // Vérification que deux utilisateurs avec le même id sont égaux
        assertEquals(user1, user2);

        // Vérification que deux utilisateurs avec des id différents ne sont pas égaux
        assertNotEquals(user1, user3);
    }

    @Test
    public void testEqualsWithSameInstance() {
        // Arrange
        UserDetailsImpl user = UserDetailsImpl.builder()
            .id(1L)
            .username("user")
            .firstName("First")
            .lastName("User")
            .admin(true)
            .password("password")
            .build();

        // Act & Assert
        // Vérification que l'utilisateur est égal à lui-même
        assertEquals(user, user);
    }

    @Test
    public void testEqualsWithNullAndDifferentClass() {
        // Arrange
        UserDetailsImpl user = UserDetailsImpl.builder()
            .id(1L)
            .username("user")
            .firstName("First")
            .lastName("User")
            .admin(true)
            .password("password")
            .build();

        // Act & Assert
        // Vérification que l'utilisateur n'est pas égal à null
        assertNotEquals(user, null);

        // Vérification que l'utilisateur n'est pas égal à une instance d'une autre classe
        assertNotEquals(user, new Object());
    }
}