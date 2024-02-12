package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value="/teachers")
public class TeacherController {
    private final TeacherRepository repository;

    public TeacherController(TeacherRepository repository){
        this.repository = repository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Teacher>> getAll(){
        var teachers = this.repository.findAll();
        if(!teachers.isEmpty()){
            return ResponseEntity.ok(teachers);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = GET, value="/{id}")
    public ResponseEntity<Teacher> get(@PathVariable("id") Long id){
        var teacher = this.repository.findById(id);
        if(teacher.isPresent()){
            return ResponseEntity.ok(teacher.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Teacher> create(@RequestBody Teacher teacher){
        return ResponseEntity.ok(this.repository.save(teacher));
    }

    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, @PathVariable("id") Long id){
        var teacherToUpdate = this.repository.findById(id);
        if(teacherToUpdate.isPresent()){
            teacherToUpdate.get().setFirstName(teacher.getFirstName());
            teacherToUpdate.get().setMiddleName(teacher.getMiddleName());
            teacherToUpdate.get().setLastName(teacher.getLastName());
            teacherToUpdate.get().setDateOfBirth(teacher.getDateOfBirth());
            teacherToUpdate.get().setHouse(teacher.getHouse());
            teacherToUpdate.get().setHeadOfHouse(teacher.isHeadOfHouse());
            teacherToUpdate.get().setEmployment(teacher.getEmployment());
            teacherToUpdate.get().setEmploymentStart(teacher.getEmploymentStart());
            teacherToUpdate.get().setEmploymentEnd(teacher.getEmploymentEnd());
            return ResponseEntity.ok(this.repository.save(teacherToUpdate.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Teacher> delete(@PathVariable("id") Long id){
        var teacher = this.repository.findById(id);
        if(teacher.isPresent()){
            this.repository.deleteById(id);
            return ResponseEntity.ok(teacher.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
    }

}
