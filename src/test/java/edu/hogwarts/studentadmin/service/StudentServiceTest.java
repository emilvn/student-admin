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
        House house = createHouse();
        Student student = createStudent(house);
        Student addedStudent = studentService.create(student);

        assertEquals("Test", addedStudent.getFirstName());
        assertEquals("Gryffindor", addedStudent.getHouse().getName());
    }

    @Test
    void patchStudentTest() {
        Student student = createStudent();
        Student updatedStudent = studentService.patch(student, 1L);

        // Test that only first name is updated
        assertEquals("Harold", updatedStudent.getFirstName());
        assertEquals("Potter", updatedStudent.getLastName());
        assertEquals("Gryffindor", updatedStudent.getHouse().getName());

        updatedStudent = studentService.patch(student, -2L);
        assertNull(updatedStudent);
    }

    @Test
    void updateStudentTest() {
        Student student = createStudent();
        Student updatedStudent = studentService.update(student, 1L);

        // Test that all properties are overwritten
        assertEquals("Harold", updatedStudent.getFirstName());
        assertNull(updatedStudent.getMiddleName());
        assertNull(updatedStudent.getLastName());
        assertNull(updatedStudent.getDateOfBirth());
        assertEquals(0, updatedStudent.getEnrollmentYear());
        assertEquals(0, updatedStudent.getGraduationYear());
        assertFalse(updatedStudent.isGraduated());
        assertFalse(updatedStudent.isPrefect());
        assertEquals("Gryffindor", updatedStudent.getHouse().getName());

        updatedStudent = studentService.update(student, -2L);
        assertNull(updatedStudent);
    }

    private House createHouse() {
        House house = new House();
        house.setId(1L);
        house.setName("Gryffindor");
        return house;
    }

    private Student createStudent(House house) {
        Student student = new Student();
        student.setFirstName("Test");
        student.setHouse(house);
        return student;
    }

    private Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Harold");
        return student;
    }
}
