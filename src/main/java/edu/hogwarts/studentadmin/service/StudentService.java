package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final HouseService houseService;

    public StudentService(StudentRepository studentRepository, HouseService houseService) {
        this.studentRepository = studentRepository;
        this.houseService = houseService;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student get(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student create(Student student) {
        var house = houseService.get(student.getHouse().getId());
        student.setHouse(house);
        return studentRepository.save(student);
    }

    public Student update(Student student, Long id) {
        var studentToUpdate = studentRepository.findById(id);
        if (studentToUpdate.isPresent()) {
            var updatedStudent = studentToUpdate.get();
            if(student.getHouse() != null) {
                var house = houseService.get(student.getHouse().getId());
                updatedStudent.setHouse(house);
            }
            if(student.getFirstName() != null) {
                updatedStudent.setFirstName(student.getFirstName());
            }
            if(student.getMiddleName() != null) {
                updatedStudent.setMiddleName(student.getMiddleName());
            }
            if(student.getLastName() != null) {
                updatedStudent.setLastName(student.getLastName());
            }
            if(student.getDateOfBirth() != null) {
                updatedStudent.setDateOfBirth(student.getDateOfBirth());
            }
            if(student.getEnrollmentYear() != 0) {
                updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            }
            if(student.getGraduationYear() != 0) {
                updatedStudent.setGraduationYear(student.getGraduationYear());
            }
            updatedStudent.setGraduated(student.isGraduated());
            updatedStudent.setPrefect(student.isPrefect());

            return studentRepository.save(updatedStudent);
        }
        return null;
    }

    public void delete(Long id) {
        if(id != null){
            studentRepository.deleteById(id);
        }
    }
}
