package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        var students = this.studentRepository.findAll();
        if (!students.isEmpty()) {
            return ResponseEntity.ok(students);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        var student = this.studentRepository.findById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Student student) {
        if(student.getFirstName() == null) {
            return ResponseEntity.badRequest().body("First name is required.");
        }
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@RequestBody Student student, @PathVariable("id") Long id) {
        var studentToUpdate = studentRepository.findById(id);
        if (studentToUpdate.isPresent()) {
            var updatedStudent = studentToUpdate.get();
            updatedStudent.setFirstName(student.getFirstName());
            updatedStudent.setMiddleName(student.getMiddleName());
            updatedStudent.setLastName(student.getLastName());
            updatedStudent.setDateOfBirth(student.getDateOfBirth());
            updatedStudent.setPrefect(student.isPrefect());
            updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            updatedStudent.setGraduationYear(student.getGraduationYear());
            updatedStudent.setGraduated(student.isGraduated());
            updatedStudent.setHouse(student.getHouse());
            studentRepository.save(updatedStudent);
            return get(id);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> delete(@PathVariable("id") Long id) {
        var studentToDelete = this.studentRepository.findById(id);
        if (studentToDelete.isPresent()) {
            this.studentRepository.delete(studentToDelete.get());
            return ResponseEntity.ok(studentToDelete.get());
        }
        return ResponseEntity.notFound().build();
    }
}
