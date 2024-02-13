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
        House house = createHouse(1L, "Gryffindor");
        Teacher teacher = createTeacher("Test", house);
        Teacher addedTeacher = teacherService.create(teacher);

        assertEquals("Test", addedTeacher.getFirstName());
        assertEquals("Gryffindor", addedTeacher.getHouse().getName());
    }

    @Test
    void updateTeacherTest() {
        House house = createHouse(2L, "Hufflepuff");
        Teacher teacher = createTeacher(1L, "Harold", house);
        Teacher updatedTeacher = teacherService.update(teacher, 1L);

        assertEquals("Harold", updatedTeacher.getFirstName());
        assertEquals("Hufflepuff", updatedTeacher.getHouse().getName());

        updatedTeacher = teacherService.update(teacher, -2L);
        assertNull(updatedTeacher);
    }

    private House createHouse(Long id, String name) {
        House house = new House();
        house.setId(id);
        house.setName(name);
        return house;
    }

    private Teacher createTeacher(String firstName, House house) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setHouse(house);
        return teacher;
    }

    private Teacher createTeacher(Long id, String firstName, House house) {
        Teacher teacher = createTeacher(firstName, house);
        teacher.setId(id);
        return teacher;
    }
}
