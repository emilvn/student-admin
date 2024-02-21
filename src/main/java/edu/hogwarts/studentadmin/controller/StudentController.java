package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.service.StudentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Student entities.
 * Handles HTTP requests for the /students endpoint.
 */
@RestController
@RequestMapping("/students")
@CrossOrigin
public class StudentController extends HogwartsPersonController<Student, StudentDTO, StudentService> {
    /**
     * Create a new StudentController. Uses dependency injection to set the StudentService.
     * @param studentService The service for Student entities.
     */
    public StudentController(StudentService studentService) {
        super(studentService);
    }
}
