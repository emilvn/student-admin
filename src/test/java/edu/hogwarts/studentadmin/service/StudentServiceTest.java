package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Test
    void createStudentTest() {
        var house = new House();
        house.setId(1L);
        var student = new Student();
        student.setFirstName("Test");
        student.setHouse(house);
        var addedStudent = studentService.create(student);

        assertEquals("Test", addedStudent.getFirstName());
        assertEquals("Gryffindor", addedStudent.getHouse().getName());
    }

    @Test
    void updateStudentTest() {
        var house = new House();
        house.setId(2L);
        var student = new Student(1L, "Harold", "Frank", "Longbottom", LocalDate.of(1980, 7, 30), house, false, 1991, 1998, true);
        student.setFirstName("Harold");
        var updatedStudent = studentService.update(student, 1L);

        assertEquals("Harold", updatedStudent.getFirstName());
        assertEquals("Hufflepuff", updatedStudent.getHouse().getName());

        updatedStudent = studentService.update(student, -2L);
        assertNull(updatedStudent);
    }
}
