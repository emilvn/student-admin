package edu.hogwarts.studentadmin.controller;

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
        return ResponseEntity.ok(teacherService.create(teacher));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
        var updatedTeacher = teacherService.update(teacher, id);
        if (updatedTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTeacher);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Teacher> patch(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
        var updatedTeacher = teacherService.patch(teacher, id);
        if (updatedTeacher == null) {
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

}
