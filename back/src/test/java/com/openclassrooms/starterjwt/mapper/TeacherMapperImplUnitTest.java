package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherMapperImplUnitTest {

    @Autowired
    private TeacherMapper teacherMapper;

    private TeacherDto teacherDto;

    @BeforeEach
    public void setup() {
        // Arrange: Créer un TeacherDto pour les tests
        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());
    }
    
    @Test
    public void toEntity_ReturnsTeacher_WhenDtoIsNotNull() {
        // Act: Convertir le TeacherDto en Teacher
        Teacher teacher = ((EntityMapper<TeacherDto, Teacher>) teacherMapper).toEntity(teacherDto);
    
        // Assert: Vérifier que le Teacher n'est pas null et que ses propriétés correspondent à celles du TeacherDto
        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    public void toEntity_ReturnsNull_WhenDtoIsNull() {
        // Act: Convertir un TeacherDto null en Teacher
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        // Assert: Vérifier que le Teacher est null
        assertNull(teacher);
    }

    @Test
    public void toDto_ReturnsNull_WhenEntityIsNull() {
        // Act: Convertir un Teacher null en TeacherDto
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);
    
        // Assert: Vérifier que le TeacherDto est null
        assertNull(teacherDto);
    }

    @Test
    public void toDtoList_ReturnsNull_WhenEntityListIsNull() {
        // Act: Convertir une liste de Teacher null en liste de TeacherDto
        List<TeacherDto> teacherDtoList = teacherMapper.toDto((List<Teacher>) null);

        // Assert: Vérifier que la liste de TeacherDto est null
        assertNull(teacherDtoList);
    }

    @Test
    public void toEntityList_ReturnsNull_WhenDtoListIsNull() {
        // Act: Convertir une liste de TeacherDto null en liste de Teacher
        List<Teacher> teacherList = teacherMapper.toEntity((List<TeacherDto>) null);

        // Assert: Vérifier que la liste de Teacher est null
        assertNull(teacherList);
    }

    @Test
    public void toEntityList_ReturnsTeacherList_WhenDtoListIsNotNull() {
        // Arrange: Créer une liste de TeacherDto pour le test
        List<TeacherDto> teacherDtoList = Arrays.asList(teacherDto);

        // Act: Convertir la liste de TeacherDto en liste de Teacher
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        // Assert: Vérifier que la liste de Teacher n'est pas null, que sa taille est la même que celle de la liste de TeacherDto
        // et que les propriétés du premier Teacher correspondent à celles du premier TeacherDto
        assertNotNull(teacherList);
        assertEquals(teacherDtoList.size(), teacherList.size());
        Teacher teacher = teacherList.get(0);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }
}