package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.service.TeacherService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teachers")
@CrossOrigin
public class TeacherController extends HogwartsPersonController<Teacher, TeacherService> {
    public TeacherController(TeacherService teacherService) {
        super(teacherService);
    }

}
