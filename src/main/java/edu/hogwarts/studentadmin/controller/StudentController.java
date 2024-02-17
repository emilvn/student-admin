package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        var students = studentService.getAll();
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        var student = studentService.get(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.create(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody Student student, @PathVariable("id") Long id) {
        var updatedStudent = studentService.update(student, id);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@RequestBody Student student, @PathVariable("id") Long id) {
        var updatedStudent = studentService.patch(student, id);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> delete(@PathVariable("id") Long id) {
        var student = studentService.get(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentService.delete(id);
        return ResponseEntity.ok(student);
    }
}
