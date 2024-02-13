package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Test
    void createStudentTest() {
        House house = createHouse(1L, "Gryffindor");
        Student student = createStudent("Test", house);
        Student addedStudent = studentService.create(student);

        assertEquals("Test", addedStudent.getFirstName());
        assertEquals("Gryffindor", addedStudent.getHouse().getName());
    }

    @Test
    void updateStudentTest() {
        House house = createHouse(2L, "Hufflepuff");
        Student student = createStudent(1L, "Harold", house);
        Student updatedStudent = studentService.update(student, 1L);

        assertEquals("Harold", updatedStudent.getFirstName());
        assertEquals("Hufflepuff", updatedStudent.getHouse().getName());

        updatedStudent = studentService.update(student, -2L);
        assertNull(updatedStudent);
    }

    private House createHouse(Long id, String name) {
        House house = new House();
        house.setId(id);
        house.setName(name);
        return house;
    }

    private Student createStudent(String firstName, House house) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setHouse(house);
        return student;
    }

    private Student createStudent(Long id, String firstName, House house) {
        Student student = createStudent(firstName, house);
        student.setId(id);
        return student;
    }
}
