package com.openclassrooms.starterjwt.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

// Annotation pour indiquer que c'est une classe de test Spring Boot
@SpringBootTest
public class UserMapperImplUnitTest {

    // Déclaration des variables d'instance
    private UserMapperImpl mapper;
    private UserDto dto1, dto2;
    private User user1, user2;

    // Méthode exécutée avant chaque test
    @BeforeEach
    public void setUp() {
        // Initialisation du mapper
        mapper = new UserMapperImpl();

        // Initialisation des objets UserDto pour les tests
        dto1 = new UserDto(1L, "test1@example.com", "Doe1", "John1", true, "password1", LocalDateTime.now(), LocalDateTime.now());
        dto2 = new UserDto(2L, "test2@example.com", "Doe2", "John2", false, "password2", LocalDateTime.now(), LocalDateTime.now());

        // Initialisation des objets User pour les tests
        user1 = new User(1L, "test1@example.com", "Doe1", "John1", "password1", true, LocalDateTime.now(), LocalDateTime.now());
        user2 = new User(2L, "test2@example.com", "Doe2", "John2", "password2", false, LocalDateTime.now(), LocalDateTime.now());
    }

    // Test de la méthode toEntity avec un UserDto en entrée
    @Test
    public void toEntity_GivenUserDto_ReturnsUser() {
        // Conversion du UserDto en User
        User user = mapper.toEntity(dto1);

        // Vérification que l'objet User n'est pas null
        assertNotNull(user);

        // Vérification que les attributs de l'objet User sont corrects
        assertEquals(dto1.getId(), user.getId());
        assertEquals(dto1.getEmail(), user.getEmail());
        assertEquals(dto1.getLastName(), user.getLastName());
        assertEquals(dto1.getFirstName(), user.getFirstName());
        assertEquals(dto1.getPassword(), user.getPassword());
        assertEquals(dto1.isAdmin(), user.isAdmin());
        assertEquals(dto1.getCreatedAt(), user.getCreatedAt());
        assertEquals(dto1.getUpdatedAt(), user.getUpdatedAt());
    }

    // Test de la méthode toEntity avec une liste de UserDto en entrée
    @Test
    public void toEntity_GivenUserDtoList_ReturnsUserList() {
        // Création d'une liste de UserDto pour le test
        List<UserDto> dtoList = Arrays.asList(dto1, dto2);

        // Conversion de la liste de UserDto en liste de User
        List<User> userList = mapper.toEntity(dtoList);

        // Vérification que la liste de User n'est pas null et contient le bon nombre d'éléments
        assertNotNull(userList);
        assertEquals(2, userList.size());

        // Vérification que les attributs du premier User sont corrects
        assertEquals(dto1.getId(), userList.get(0).getId());
        assertEquals(dto1.getEmail(), userList.get(0).getEmail());
        assertEquals(dto1.getLastName(), userList.get(0).getLastName());
        assertEquals(dto1.getFirstName(), userList.get(0).getFirstName());
        assertEquals(dto1.getPassword(), userList.get(0).getPassword());
        assertEquals(dto1.isAdmin(), userList.get(0).isAdmin());
        assertEquals(dto1.getCreatedAt(), userList.get(0).getCreatedAt());
        assertEquals(dto1.getUpdatedAt(), userList.get(0).getUpdatedAt());

        // Vérification que les attributs du deuxième User sont corrects
        assertEquals(dto2.getId(), userList.get(1).getId());
        assertEquals(dto2.getEmail(), userList.get(1).getEmail());
        assertEquals(dto2.getLastName(), userList.get(1).getLastName());
        assertEquals(dto2.getFirstName(), userList.get(1).getFirstName());
        assertEquals(dto2.getPassword(), userList.get(1).getPassword());
        assertEquals(dto2.isAdmin(), userList.get(1).isAdmin());
        assertEquals(dto2.getCreatedAt(), userList.get(1).getCreatedAt());
        assertEquals(dto2.getUpdatedAt(), userList.get(1).getUpdatedAt());
    }

    // Test de la méthode toDto avec une liste de User en entrée
    @Test
    public void toDto_GivenUserList_ReturnsUserDtoList() {
        // Création d'une liste de User pour le test
        List<User> userList = Arrays.asList(user1, user2);

        // Conversion de la liste de User en liste de UserDto
        List<UserDto> dtoList = mapper.toDto(userList);

        // Vérification que la liste de UserDto n'est pas null et contient le bon nombre d'éléments
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        // Vérification que les attributs du premier UserDto sont corrects
        assertEquals(user1.getId(), dtoList.get(0).getId());
        assertEquals(user1.getEmail(), dtoList.get(0).getEmail());
        assertEquals(user1.getLastName(), dtoList.get(0).getLastName());
        assertEquals(user1.getFirstName(), dtoList.get(0).getFirstName());
        assertEquals(user1.getPassword(), dtoList.get(0).getPassword());
        assertEquals(user1.isAdmin(), dtoList.get(0).isAdmin());
        assertEquals(user1.getCreatedAt(), dtoList.get(0).getCreatedAt());
        assertEquals(user1.getUpdatedAt(), dtoList.get(0).getUpdatedAt());

        // Vérification que les attributs du deuxième UserDto sont corrects
        assertEquals(user2.getId(), dtoList.get(1).getId());
        assertEquals(user2.getEmail(), dtoList.get(1).getEmail());
        assertEquals(user2.getLastName(), dtoList.get(1).getLastName());
        assertEquals(user2.getFirstName(), dtoList.get(1).getFirstName());
        assertEquals(user2.getPassword(), dtoList.get(1).getPassword());
        assertEquals(user2.isAdmin(), dtoList.get(1).isAdmin());
        assertEquals(user2.getCreatedAt(), dtoList.get(1).getCreatedAt());
        assertEquals(user2.getUpdatedAt(), dtoList.get(1).getUpdatedAt());
    }
}