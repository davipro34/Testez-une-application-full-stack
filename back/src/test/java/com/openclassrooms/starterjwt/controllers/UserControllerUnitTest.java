package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        // Initialise un DTO d'utilisateur pour les tests
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("Admin");
        userDto.setLastName("Admin");

        // Initialise un utilisateur pour les tests
        User user = new User();
        user.setId(1L);
        user.setFirstName("Admin");
        user.setLastName("Admin");

        // Initialise une liste de DTO d'utilisateurs pour les tests
        List<UserDto> userDtos = Arrays.asList(userDto);

        // Initialise une liste d'utilisateurs pour les tests
        List<User> users = Arrays.asList(user);

        // Configure les mocks pour retourner ces objets lorsque les méthodes correspondantes sont appelées
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        when(userMapper.toDto(users)).thenReturn(userDtos);
    }

    @Test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testFindById() throws Exception {
        // Arrange
        // Act : Effectue une requête GET sur l'URL "/api/user/1"
        mockMvc.perform(get("/api/user/1"))
        // Assert : Vérifie que le statut de la réponse est OK et que le corps de la réponse contient le DTO d'utilisateur correct
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.firstName", is("Admin")))
        .andExpect(jsonPath("$.lastName", is("Admin")));
        // Vérifie que la méthode du service a été appelée avec les bons arguments
        verify(userService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testFindById_NumberFormatException() throws Exception {
        // Arrange : Aucun arrangement nécessaire pour ce test

        // Act : Effectue une requête GET sur l'URL "/api/user/notANumber"
        mockMvc.perform(get("/api/user/notANumber"))

        // Assert : Vérifie que le statut de la réponse est 400 (Bad Request)
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@test.com") // Exécute le test avec un utilisateur mocké
    public void testDeleteById() throws Exception {
        // Arrange
        // Initialise un utilisateur pour les tests
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        // Configure le mock pour retourner cet utilisateur lorsque la méthode findById est appelée
        when(userService.findById(1L)).thenReturn(user);
        // Act : Effectue une requête DELETE sur l'URL "/api/user/1"
        mockMvc.perform(delete("/api/user/1"))
        // Assert : Vérifie que le statut de la réponse est OK
        .andExpect(status().isOk());
        // Vérifie que la méthode du service a été appelée avec le bon argument
        verify(userService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "wrong@test.com") // Exécute le test avec un utilisateur mocké dont le nom d'utilisateur est "wrong@test.com"
    public void testDeleteById_Unauthorized() throws Exception {
        // Arrange
        // Initialise un utilisateur pour les tests
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        // Configure le mock pour retourner cet utilisateur lorsque la méthode findById est appelée
        when(userService.findById(1L)).thenReturn(user);

        // Act : Effectue une requête DELETE sur l'URL "/api/user/1"
        mockMvc.perform(delete("/api/user/1"))

        // Assert : Vérifie que le statut de la réponse est 401 (Unauthorized)
        .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser // Exécute le test avec un utilisateur mocké
    public void testDeleteById_NumberFormatException() throws Exception {
        // Arrange : Aucun arrangement nécessaire pour ce test

        // Act : Effectue une requête DELETE sur l'URL "/api/user/notANumber"
        mockMvc.perform(delete("/api/user/notANumber"))

        // Assert : Vérifie que le statut de la réponse est 400 (Bad Request)
        .andExpect(status().isBadRequest());
    }
}