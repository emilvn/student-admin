package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.model.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @Test
    void createTeacherTest() {
        var house = new House();
        house.setId(1L);
        var teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setHouse(house);
        var addedStudent = teacherService.create(teacher);

        assertEquals("Test", addedStudent.getFirstName());
        assertEquals("Gryffindor", addedStudent.getHouse().getName());
    }

    @Test
    void updateTeacherTest() {
        var house = new House();
        house.setId(2L);
        var teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Harold");
        teacher.setHouse(house);
        var updatedTeacher = teacherService.update(teacher, 1L);

        assertEquals("Harold", updatedTeacher.getFirstName());
        assertEquals("Hufflepuff", updatedTeacher.getHouse().getName());

        updatedTeacher = teacherService.update(teacher, -2L);
        assertNull(updatedTeacher);
    }
}
