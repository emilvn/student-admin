package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.HouseRepository;
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
    private final TeacherRepository teacherRepository;

    public TeacherController(TeacherRepository teacherRepository){
        this.teacherRepository = teacherRepository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Teacher>> getAll(){
        var teachers = this.teacherRepository.findAll();
        if(!teachers.isEmpty()){
            return ResponseEntity.ok(teachers);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = GET, value="/{id}")
    public ResponseEntity<Teacher> get(@PathVariable("id") Long id){
        var teacher = this.teacherRepository.findById(id);
        if(teacher.isPresent()){
            return ResponseEntity.ok(teacher.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Teacher> create(@RequestBody Teacher teacher){
        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, @PathVariable("id") Long id){
        var teacherToUpdate = teacherRepository.findById(id);
        if(teacherToUpdate.isPresent()){
            var updatedTeacher = teacherToUpdate.get();
            updatedTeacher.setFirstName(teacher.getFirstName());
            updatedTeacher.setMiddleName(teacher.getMiddleName());
            updatedTeacher.setLastName(teacher.getLastName());
            updatedTeacher.setDateOfBirth(teacher.getDateOfBirth());
            updatedTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            updatedTeacher.setEmployment(teacher.getEmployment());
            updatedTeacher.setEmploymentStart(teacher.getEmploymentStart());
            updatedTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            updatedTeacher.setHouse(teacher.getHouse());
            teacherRepository.save(updatedTeacher);
            return get(id);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Teacher> delete(@PathVariable("id") Long id){
        var teacher = this.teacherRepository.findById(id);
        if(teacher.isPresent()){
            this.teacherRepository.deleteById(id);
            return ResponseEntity.ok(teacher.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
    }

}
