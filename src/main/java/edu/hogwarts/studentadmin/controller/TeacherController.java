package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.service.HouseService;
import edu.hogwarts.studentadmin.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final HouseService houseService;

    public TeacherController(TeacherService teacherService, HouseService houseService) {
        this.teacherService = teacherService;
        this.houseService = houseService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAll() {
        var teachers = teacherService.getAll();
        if (teachers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> get(@PathVariable("id") Long id) {
        var teacher = teacherService.get(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Teacher teacher) {
        if(validateTeacher(teacher) != null) {
            return validateTeacher(teacher);
        }
        return ResponseEntity.ok(teacherService.create(teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
        var updatedTeacher = teacherService.update(teacher, id);
        if(updatedTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTeacher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Teacher> delete(@PathVariable("id") Long id) {
        var teacher = teacherService.get(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }

        teacherService.delete(id);
        return ResponseEntity.ok(teacher);
    }

    private ResponseEntity<Object> validateTeacher(Teacher teacher) {
        if(teacher == null) {
            return ResponseEntity.notFound().build();
        }
        if(teacher.getFirstName() == null) {
            return ResponseEntity.badRequest().body("First name is required.");
        }
        if(teacher.getHouse() == null) {
            return ResponseEntity.badRequest().body("House is required.");
        }
        if(teacher.getHouse().getId() == null) {
            return ResponseEntity.badRequest().body("House ID is required.");
        }
        if(houseService.get(teacher.getHouse().getId()) == null) {
            return ResponseEntity.badRequest().body("House with given ID doesnt exist.");
        }
        return null;
    }

}
