package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("studentService")
public class StudentService {
    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;

    public StudentService(StudentRepository studentRepository, HouseRepository houseRepository) {
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
    }

    public List<Student> getAll() {
        return this.studentRepository.findAll();
    }

    public Student get(Long id) {
        return this.studentRepository.findById(id).orElse(null);
    }

    public Student create(Student student) {
        var house = this.houseRepository.findById(student.getHouse().getId());
        house.ifPresent(student::setHouse);
        return this.studentRepository.save(student);
    }

    public Student update(Student student, Long id) {
        var studentToUpdate = this.studentRepository.findById(id);
        if (studentToUpdate.isPresent()) {
            var updatedStudent = studentToUpdate.get();
            var house = this.houseRepository.findById(student.getHouse().getId());
            house.ifPresent(updatedStudent::setHouse);

            updatedStudent.setFirstName(student.getFirstName());
            updatedStudent.setMiddleName(student.getMiddleName());
            updatedStudent.setLastName(student.getLastName());
            updatedStudent.setDateOfBirth(student.getDateOfBirth());
            updatedStudent.setPrefect(student.isPrefect());
            updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            updatedStudent.setGraduationYear(student.getGraduationYear());
            updatedStudent.setGraduated(student.isGraduated());

            return this.studentRepository.save(updatedStudent);
        }
        return null;
    }

    public void delete(Long id) {
        if(id != null){
            this.studentRepository.deleteById(id);
        }
    }
}
