package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionMapperImplUnitTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Test
    public void testToEntity() {
        // Arrange : Création de deux objets SessionDto
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<SessionDto> dtoList = Arrays.asList(sessionDto1, sessionDto2);

        // Act : Conversion de la liste de SessionDto en liste de Session
        List<Session> result = sessionMapper.toEntity(dtoList);

        // Assert : Vérification que la conversion a réussi
        assertNotNull(result);
        assertEquals(2, result.size());

        Session session1 = result.get(0);
        assertEquals(sessionDto1.getId(), session1.getId());
        assertEquals(sessionDto1.getName(), session1.getName());

        Session session2 = result.get(1);
        assertEquals(sessionDto2.getId(), session2.getId());
        assertEquals(sessionDto2.getName(), session2.getName());
    }

    @Test
    public void testToEntityWithNullSessionDto() {
        // Arrange : Création d'un SessionDto null
        SessionDto sessionDto = null;

        // Act : Conversion du SessionDto null en Session
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert : Vérification que le résultat est null
        assertNull(result);
    }

    @Test
    public void testToEntityWithUsers() {
        // Arrange : Création d'un SessionDto avec une liste d'ID d'utilisateurs
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Act : Conversion du SessionDto en Session
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert : Vérification que la conversion a réussi et que les utilisateurs sont corrects
        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
        assertEquals(user1, result.getUsers().get(0));
        assertEquals(user2, result.getUsers().get(1));
    }

    @Test
    public void testToEntityWithNull() {
        // Arrange : Création d'une liste de SessionDto null
        List<SessionDto> dtoList = null;

        // Act : Conversion de la liste de SessionDto null en liste de Session
        List<Session> result = sessionMapper.toEntity(dtoList);

        // Assert : Vérification que le résultat est null
        assertNull(result);
    }

    @Test
    public void testToDtoWithNull() {
        // Arrange : Création d'une liste de Session null
        List<Session> entityList = null;

        // Act : Conversion de la liste de Session null en liste de SessionDto
        List<SessionDto> result = sessionMapper.toDto(entityList);

        // Assert : Vérification que le résultat est null
        assertNull(result);
    }

    @Test
    public void testToDtoWithNullSession() {
        // Arrange : Création d'une Session null
        Session session = null;

        // Act : Conversion de la Session null en SessionDto
        SessionDto result = sessionMapper.toDto(session);

        // Assert : Vérification que le résultat est null
        assertNull(result);
    }

    @Test
    public void testToDtoWithTeacher() {
        // Arrange : Création d'une Session avec un enseignant
        Teacher teacher = new Teacher();
        teacher.setId(1L);
    
        Session session = new Session();
        session.setTeacher(teacher);
    
        // Act : Conversion de la Session en SessionDto
        SessionDto result = sessionMapper.toDto(session);
    
        // Assert : Vérification que la conversion a réussi et que l'ID de l'enseignant est correct
        assertNotNull(result);
        assertEquals(teacher.getId(), result.getTeacher_id());
    }

    @Test
    public void testToDtoWithNullTeacher() {
        // Arrange : Création d'une Session avec un enseignant null
        Session session = new Session();
        session.setTeacher(null);

        // Act : Conversion de la Session en SessionDto
        SessionDto result = sessionMapper.toDto(session);

        // Assert : Vérification que la conversion a réussi et que l'ID de l'enseignant est null
        assertNotNull(result);
        assertNull(result.getTeacher_id());
    }

    @Test
    public void testToDtoWithTeacherNullId() {
        // Arrange : Création d'une Session avec un enseignant dont l'ID est null
        Teacher teacher = new Teacher();
        teacher.setId(null);

        Session session = new Session();
        session.setTeacher(teacher);

        // Act : Conversion de la Session en SessionDto
        SessionDto result = sessionMapper.toDto(session);

        // Assert : Vérification que la conversion a réussi et que l'ID de l'enseignant est null
        assertNotNull(result);
        assertNull(result.getTeacher_id());
    }
}