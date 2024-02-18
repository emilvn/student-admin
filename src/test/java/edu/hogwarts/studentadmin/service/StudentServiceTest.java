package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.model.Student;
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
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private HouseService houseService;

    @BeforeEach
    void setUp() {
        var gryffindor = new House(1L, "Gryffindor", "Godric Gryffindor", new ArrayList<>(List.of("Red", "Gold")));
        var hufflepuff = new House(2L, "Hufflepuff", "Helga Hufflepuff", new ArrayList<>(List.of("Yellow", "Black")));
        var testHouse = new House(3L, "Test house", null, null);

        when(houseService.get(1L)).thenReturn(gryffindor);
        when(houseService.get(2L)).thenReturn(hufflepuff);
        when(houseService.get(3L)).thenReturn(testHouse);
        when(houseService.get(5L)).thenReturn(null);
    }

    @Test
    void createStudentTest() {
        var student = new Student();
        var house = new House();
        house.setId(3L);
        house.setName("Test house");
        student.setHouse(house);
        student.setFirstName("Test create");
        var addedStudent = studentService.create(student);

        assertEquals("Test create", addedStudent.getFirstName(), "First name should be added");
        assertEquals("Test house", addedStudent.getHouse().getName(), "House should be added");
    }

    @Test
    void updateStudentTest() {
        var student = new Student();
        student.setFullName("Test put");
        var updatedStudent = studentService.update(student, 1L);

        // Test that all properties are overwritten
        assertEquals("Test", updatedStudent.getFirstName(), "First name should be overridden");
        assertEquals("put", updatedStudent.getLastName(), "Last name should be overridden");
        assertNull(updatedStudent.getMiddleName(), "Middle name should be overridden");
        assertNull(updatedStudent.getDateOfBirth(), "Date of birth should be overridden");
        assertEquals(0, updatedStudent.getEnrollmentYear(), "Enrollment year should be overridden");
        assertEquals(0, updatedStudent.getGraduationYear(), "Graduation year should be overridden");
        assertFalse(updatedStudent.isGraduated(), "Graduated should be overridden");
        assertFalse(updatedStudent.isPrefect(), "Prefect should be overridden");
        assertNull(updatedStudent.getHouse(), "House should be overridden");

        updatedStudent = studentService.update(student, -2L);
        assertNull(updatedStudent, "Should return null for invalid student id");
    }

    @Test
    void patchStudentTest() {
        var student = new Student();
        student.setFullName("Test patch");
        var updatedStudent = studentService.patch(student, 1L);

        // Test that only first name is updated
        assertEquals("Test", updatedStudent.getFirstName(), "Name should be updated");
        assertNull(updatedStudent.getMiddleName(), "Name should be updated");
        assertEquals("patch", updatedStudent.getLastName(), "Name should be updated");
        assertEquals("Gryffindor", updatedStudent.getHouse().getName(), "House should stay the same");

        updatedStudent = studentService.patch(student, -2L);
        assertNull(updatedStudent);
    }
}
