package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;

    public StudentController(StudentRepository studentRepository, HouseRepository houseRepository){
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Student>> getAll(){
        var students = this.studentRepository.findAll();
        if(!students.isEmpty()){
            return ResponseEntity.ok(students);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = GET, value="/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id){
        var student = this.studentRepository.findById(id);
        if(student.isPresent()){
            return ResponseEntity.ok(student.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Student> create(@RequestBody Student student){
        var houseId = student.getHouse().getId();
        var house = houseRepository.findById(houseId);
        if(house.isPresent()){
            student.setHouse(house.get());
        }
        else{
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<Student> update(@RequestBody Student student, @PathVariable("id") Long id){
        var studentToUpdate = studentRepository.findById(id);
        if(studentToUpdate.isPresent()){
            studentToUpdate.get().setFirstName(student.getFirstName());
            studentToUpdate.get().setMiddleName(student.getMiddleName());
            studentToUpdate.get().setLastName(student.getLastName());
            studentToUpdate.get().setDateOfBirth(student.getDateOfBirth());
            studentToUpdate.get().setPrefect(student.isPrefect());
            studentToUpdate.get().setEnrollmentYear(student.getEnrollmentYear());
            studentToUpdate.get().setGraduationYear(student.getGraduationYear());
            studentToUpdate.get().setGraduated(student.isGraduated());
            studentToUpdate.get().setHouse(student.getHouse());

            var houseId = student.getHouse().getId();
            var house = houseRepository.findById(houseId);
            if(house.isPresent()){
                studentToUpdate.get().setHouse(house.get());
            }
            else{
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok(studentRepository.save(studentToUpdate.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Student> delete(@PathVariable("id") Long id){
        var studentToDelete = this.studentRepository.findById(id);
        if(studentToDelete.isPresent()){
            this.studentRepository.delete(studentToDelete.get());
            return ResponseEntity.ok(studentToDelete.get());
        }
        return ResponseEntity.notFound().build();
    }
}
