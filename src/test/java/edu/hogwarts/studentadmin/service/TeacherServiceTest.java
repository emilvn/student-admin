package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;
    
    @MockBean
    private HouseService houseService;
    
    @BeforeEach
    void setUp(){
        var gryffindor = new House(1L, "Gryffindor", "Godric Gryffindor", new ArrayList<>(List.of("Red", "Gold")));
        var hufflepuff = new House(2L, "Hufflepuff", "Helga Hufflepuff", new ArrayList<>(List.of("Yellow", "Black")));
        var testHouse = new House(3L, "Test house", null, null);

        when(houseService.get(1L)).thenReturn(gryffindor);
        when(houseService.get(2L)).thenReturn(hufflepuff);
        when(houseService.get(3L)).thenReturn(testHouse);
        when(houseService.get(5L)).thenReturn(null);
    }

    @Test
    void createTeacherTest() {
        var teacher = new Teacher();
        var house = new House();
        house.setId(3L);
        house.setName("Test house");
        teacher.setHouse(house);
        teacher.setFirstName("Test create");
        Teacher addedTeacher = teacherService.create(teacher);

        assertEquals("Test create", addedTeacher.getFirstName());
        assertEquals("Test house", addedTeacher.getHouse().getName());
    }

    @Test
    void updateTeacherTest() {
        var teacher = new Teacher();
        teacher.setFirstName("Test put");
        Teacher updatedTeacher = teacherService.update(teacher, 1L);

        assertEquals("Test put", updatedTeacher.getFirstName(), "First name should be overridden");
        assertNull(updatedTeacher.getMiddleName(), "Middle name should be overridden");
        assertNull(updatedTeacher.getLastName(), "Last name should be overridden");
        assertNull(updatedTeacher.getDateOfBirth(), "Date of birth should be overridden");
        assertNull(updatedTeacher.getEmploymentStart(), "Enrollment year should be overridden");
        assertNull(updatedTeacher.getEmploymentEnd(), "Graduation year should be overridden");
        assertNull(updatedTeacher.getHouse(), "House should be overridden");

        updatedTeacher = teacherService.update(teacher, -2L);
        assertNull(updatedTeacher);
    }

    @Test
    void patchTeacherTest() {
        var teacher = new Teacher();
        teacher.setFirstName("Test patch");
        var updatedTeacher = teacherService.patch(teacher, 1L);

        assertEquals("Test patch", updatedTeacher.getFirstName());
        assertEquals("Gryffindor", updatedTeacher.getHouse().getName());

        updatedTeacher = teacherService.patch(teacher, -2L);
        assertNull(updatedTeacher);
    }
}
