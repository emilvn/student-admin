package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.service.TeacherService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for Teacher entities.
 * Handles HTTP requests for the /teachers endpoint.
 */
@RestController
@RequestMapping("/teachers")
@CrossOrigin
public class TeacherController extends HogwartsPersonController<Teacher, TeacherService> {
    /**
     * Create a new TeacherController. Uses dependency injection to set the TeacherService.
     * @param teacherService The service for Teacher entities.
     */
    public TeacherController(TeacherService teacherService) {
        super(teacherService);
    }

}
