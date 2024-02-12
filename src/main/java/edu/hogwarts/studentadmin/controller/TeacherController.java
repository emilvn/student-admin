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
    private final HouseRepository houseRepository;

    public TeacherController(TeacherRepository teacherRepository, HouseRepository houseRepository){
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
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
        var houseId = teacher.getHouse().getId();
        var house = houseRepository.findById(houseId);
        if(house.isPresent()){
            teacher.setHouse(house.get());
        }
        else{
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(teacherRepository.save(teacher));
    }

    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, @PathVariable("id") Long id){
        var teacherToUpdate = teacherRepository.findById(id);
        if(teacherToUpdate.isPresent()){
            teacherToUpdate.get().setFirstName(teacher.getFirstName());
            teacherToUpdate.get().setMiddleName(teacher.getMiddleName());
            teacherToUpdate.get().setLastName(teacher.getLastName());
            teacherToUpdate.get().setDateOfBirth(teacher.getDateOfBirth());
            teacherToUpdate.get().setHeadOfHouse(teacher.isHeadOfHouse());
            teacherToUpdate.get().setEmployment(teacher.getEmployment());
            teacherToUpdate.get().setEmploymentStart(teacher.getEmploymentStart());
            teacherToUpdate.get().setEmploymentEnd(teacher.getEmploymentEnd());

            var houseId = teacher.getHouse().getId();
            var house = houseRepository.findById(houseId);
            if(house.isPresent()){
                teacherToUpdate.get().setHouse(house.get());
            }
            else{
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(teacherRepository.save(teacherToUpdate.get()));
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
