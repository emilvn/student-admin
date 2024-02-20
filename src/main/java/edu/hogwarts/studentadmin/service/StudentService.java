package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends HogwartsPersonService<Student> {
    public StudentService(StudentRepository studentRepository, HouseService houseService) {
        super(studentRepository, houseService);
    }

    public Student get(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        int firstSpace = name.indexOf(" ");
        int lastSpace = name.lastIndexOf(" ");
        if (firstSpace == -1) {
            return ((StudentRepository) repository).findFirstByFirstNameIgnoreCase(name).orElse(null);
        } else if (firstSpace == lastSpace) {
            var firstName = name.substring(0, firstSpace);
            var lastName = name.substring(firstSpace + 1);
            return ((StudentRepository) repository).findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName).orElse(null);
        }
        var firstName = name.substring(0, firstSpace);
        var middleName = name.substring(firstSpace + 1, lastSpace);
        var lastName = name.substring(lastSpace + 1);
        return ((StudentRepository) repository).findFirstByFirstNameIgnoreCaseAndMiddleNameIgnoreCaseAndLastNameIgnoreCase(firstName, middleName, lastName).orElse(null);
    }

    public Student update(Student student, Long id) {
        var studentToUpdate = repository.findById(id);
        if (studentToUpdate.isPresent()) {
            var updatedStudent = studentToUpdate.get();
            var house = houseService.get(student.getHouseName());
            updatedStudent.setHouse(house);
            updatedStudent.setName(student.getName());
            updatedStudent.setDateOfBirth(student.getDateOfBirth());
            updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            updatedStudent.setGraduationYear(student.getGraduationYear());
            updatedStudent.setGraduated(student.isGraduated());
            updatedStudent.setPrefect(student.isPrefect());
            return repository.save(updatedStudent);
        }
        return null;
    }

    public Student patch(Student student, Long id) {
        var studentToUpdate = repository.findById(id);
        if (studentToUpdate.isPresent()) {
            var updatedStudent = studentToUpdate.get();
            if (student.getHouseName() != null) {
                var house = houseService.get(student.getHouseName());
                updatedStudent.setHouse(house);
            }
            if (student.getName() != null) {
                updatedStudent.setName(student.getName());
            }
            if (student.getDateOfBirth() != null) {
                updatedStudent.setDateOfBirth(student.getDateOfBirth());
            }
            if (student.getEnrollmentYear() != null) {
                updatedStudent.setEnrollmentYear(student.getEnrollmentYear());
            }
            if (student.getGraduationYear() != null) {
                updatedStudent.setGraduationYear(student.getGraduationYear());
            }
            if (student.isGraduated() != null) {
                updatedStudent.setGraduated(student.isGraduated());
            }
            if (student.getSchoolYear() != null) {
                updatedStudent.setSchoolYear(student.getSchoolYear());
            }
            if (student.isPrefect() != null) {
                updatedStudent.setPrefect(student.isPrefect());
            }

            return repository.save(updatedStudent);
        }
        return null;
    }
}
