package com.openclassrooms.starterjwt.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

// Annotation pour utiliser l'extension Mockito avec JUnit 5
@ExtendWith(MockitoExtension.class)
public class SessionServiceUnitTest {

    // Mock du repository de Session
    @Mock
    private SessionRepository sessionRepository;

    // Mock du repository de User
    @Mock
    private UserRepository userRepository;

    // Injection des mocks dans le service à tester
    @InjectMocks
    private SessionService sessionService;

    // Test de la méthode participate quand la session ou l'utilisateur n'existe pas
    @Test
    public void participate_WhenSessionOrUserNotFound_ThrowsNotFoundException() {
        // Arrange : configuration des mocks pour retourner une valeur vide
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert : vérification que la méthode lève une exception NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    // Test de la méthode participate quand l'utilisateur participe déjà
    @Test
    public void participate_WhenUserAlreadyParticipates_ThrowsBadRequestException() {
        // Arrange : création d'une session avec un utilisateur participant
        User user = new User();
        user.setId(1L);
        Session session = mock(Session.class);
        List<User> users = new ArrayList<>();
        users.add(user);
        when(session.getUsers()).thenReturn(users);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act & Assert : vérification que la méthode lève une exception BadRequestException
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    // Test de la méthode noLongerParticipate quand la session n'existe pas
    @Test
    public void noLongerParticipate_WhenSessionNotFound_ThrowsNotFoundException() {
        // Arrange : configuration du mock pour retourner une valeur vide
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert : vérification que la méthode lève une exception NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }

    // Test de la méthode noLongerParticipate quand l'utilisateur ne participe pas déjà
    @Test
    public void noLongerParticipate_WhenUserNotAlreadyParticipates_ThrowsBadRequestException() {
        // Arrange : création d'une session sans utilisateur participant
        User user = new User();
        user.setId(1L);
        Session session = mock(Session.class);
        List<User> users = new ArrayList<>();
        when(session.getUsers()).thenReturn(users);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // Act & Assert : vérification que la méthode lève une exception BadRequestException
        // quand l'utilisateur ne participe pas déjà à la session
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }
}