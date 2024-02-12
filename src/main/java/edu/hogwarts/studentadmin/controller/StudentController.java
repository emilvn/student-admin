package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Student>> getAll() {
        var students = this.studentRepository.findAll();
        if (!students.isEmpty()) {
            return ResponseEntity.ok(students);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = GET, value = "/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        var student = this.studentRepository.findById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Student> create(@RequestBody Student student) {
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @RequestMapping(method = PUT, value = "/{id}")
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

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Student> delete(@PathVariable("id") Long id) {
        var studentToDelete = this.studentRepository.findById(id);
        if (studentToDelete.isPresent()) {
            this.studentRepository.delete(studentToDelete.get());
            return ResponseEntity.ok(studentToDelete.get());
        }
        return ResponseEntity.notFound().build();
    }
}
