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
        // Arrange
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");
        // Add more fields as necessary

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");
        // Add more fields as necessary

        List<SessionDto> dtoList = Arrays.asList(sessionDto1, sessionDto2);

        // Act
        List<Session> result = sessionMapper.toEntity(dtoList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        Session session1 = result.get(0);
        assertEquals(sessionDto1.getId(), session1.getId());
        assertEquals(sessionDto1.getName(), session1.getName());
        // Add more assertions as necessary

        Session session2 = result.get(1);
        assertEquals(sessionDto2.getId(), session2.getId());
        assertEquals(sessionDto2.getName(), session2.getName());
        // Add more assertions as necessary
    }

    @Test
    public void testToEntityWithNullSessionDto() {
        // Arrange
        SessionDto sessionDto = null;

        // Act
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToEntityWithUsers() {
        // Arrange
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        // Act
        Session result = sessionMapper.toEntity(sessionDto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
        assertEquals(user1, result.getUsers().get(0));
        assertEquals(user2, result.getUsers().get(1));
    }

    @Test
    public void testToEntityWithNull() {
        // Arrange
        List<SessionDto> dtoList = null;

        // Act
        List<Session> result = sessionMapper.toEntity(dtoList);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToDtoWithNull() {
        // Arrange
        List<Session> entityList = null;

        // Act
        List<SessionDto> result = sessionMapper.toDto(entityList);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToDtoWithNullSession() {
        // Arrange
        Session session = null;

        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNull(result);
    }

    @Test
    public void testToDtoWithTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
    
        Session session = new Session();
        session.setTeacher(teacher);
    
        // Act
        SessionDto result = sessionMapper.toDto(session);
    
        // Assert
        assertNotNull(result);
        assertEquals(teacher.getId(), result.getTeacher_id());
    }

    @Test
    public void testToDtoWithNullTeacher() {
        // Arrange
        Session session = new Session();
        session.setTeacher(null);

        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertNull(result.getTeacher_id());
    }

    @Test
    public void testToDtoWithTeacherNullId() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(null);

        Session session = new Session();
        session.setTeacher(teacher);

        // Act
        SessionDto result = sessionMapper.toDto(session);

        // Assert
        assertNotNull(result);
        assertNull(result.getTeacher_id());
    }
}