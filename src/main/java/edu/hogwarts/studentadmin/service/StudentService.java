package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides service methods to manage students in the school.
 */
@Service
public class StudentService extends HogwartsPersonService<Student, StudentDTO> {
    /**
     * Constructor for StudentService. Uses dependency injection to get the StudentRepository and HouseService.
     * @param studentRepository The repository for students
     * @param houseService The service for houses
     */
    public StudentService(StudentRepository studentRepository, HouseService houseService) {
        super(studentRepository, houseService);
    }

    public StudentDTO convertToDTO(Student student) {
        var studentDTO = new StudentDTO();
        if(student.getId() != null) {
            studentDTO.setId(student.getId());
        }
        if(student.getHouse() != null) {
            studentDTO.setHouse(student.getHouse());
        }
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setMiddleName(student.getMiddleName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setDateOfBirth(student.getDateOfBirth());
        studentDTO.setEnrollmentYear(student.getEnrollmentYear());
        studentDTO.setGraduationYear(student.getGraduationYear());
        studentDTO.setGraduated(student.isGraduated());
        studentDTO.setPrefect(student.isPrefect());
        studentDTO.setSchoolYear(student.getSchoolYear());
        return studentDTO;
    }

    public Student convertToEntity(StudentDTO studentDTO) {
        var studentEntity = new Student();
        if(studentDTO.getId() != null) {
            studentEntity.setId(studentDTO.getId());
        }
        studentEntity.setHouse(houseService.get(studentDTO.getHouseName()));
        studentEntity.setFirstName(studentDTO.getFirstName());
        studentEntity.setMiddleName(studentDTO.getMiddleName());
        studentEntity.setLastName(studentDTO.getLastName());
        studentEntity.setDateOfBirth(studentDTO.getDateOfBirth());
        studentEntity.setEnrollmentYear(studentDTO.getEnrollmentYear());
        studentEntity.setGraduationYear(studentDTO.getGraduationYear());
        studentEntity.setGraduated(studentDTO.isGraduated());
        studentEntity.setPrefect(studentDTO.isPrefect());
        studentEntity.setSchoolYear(studentDTO.getSchoolYear());
        return studentEntity;
    }

    /**
     * Gets a list of all the students.
     * @return A list of all students.
     */
    public List<StudentDTO> getAll() {
        var students = repository.findAll();
        return students.stream().map(this::convertToDTO).toList();
    }

    /**
     * Gets a student by their ID.
     * @param id The ID of the student to find.
     * @return The student, or null if it does not exist.
     */
    public StudentDTO get(Long id) {
        var student = repository.findById(id).orElse(null);
        if (student == null) {
            return null;
        }
        return convertToDTO(student);
    }

    /**
     * Gets a specific student by their full name.
     * @param name the full name or first name of the student
     * @return the first student found with the given name, or null if none is found
     */
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

    /**
     * Creates a new student from the given student DTO.
     * @param studentDTO the student DTO to create the student from
     * @return the created student
     */
    public StudentDTO create(StudentDTO studentDTO) {
        var studentEntity = convertToEntity(studentDTO);
        return convertToDTO(repository.save(studentEntity));
    }

    /**
     * Updates a student by their ID. Overwrites the entire student with the given student DTO.
     * @param studentDTO the new student to replace the old student
     * @param id the ID of the student to update
     * @return the updated student, or null if the student with the given ID is not found
     */
    public StudentDTO update(StudentDTO studentDTO, Long id) {
        var studentEntity = repository.findById(id).orElse(null);
        if(studentEntity == null){
            return null;
        }
        studentEntity = convertToEntity(studentDTO);
        return convertToDTO(repository.save(studentEntity));
    }

    /**
     * Updates a student by their ID. Updates only the non-null fields of the student with the given new student.
     * @param studentDTO the new student to update the old student
     * @param id the ID of the student to update
     * @return the updated student, or null if the student with the given ID is not found
     */
    public StudentDTO patch(StudentDTO studentDTO, Long id) {
        var studentEntity = repository.findById(id).orElse(null);
        if(studentEntity == null){
            return null;
        }
        if (studentDTO.getHouseName() != null) {
            var house = houseService.get(studentDTO.getHouseName());
            studentEntity.setHouse(house);
        }
        if (studentDTO.getName() != null) {
            studentEntity.setFirstName(studentDTO.getFirstName());
            studentEntity.setMiddleName(studentDTO.getMiddleName());
            studentEntity.setLastName(studentDTO.getLastName());
        }
        if (studentDTO.getDateOfBirth() != null) {
            studentEntity.setDateOfBirth(studentDTO.getDateOfBirth());
        }
        if (studentDTO.getEnrollmentYear() != null) {
            studentEntity.setEnrollmentYear(studentDTO.getEnrollmentYear());
        }
        if (studentDTO.getGraduationYear() != null) {
            studentEntity.setGraduationYear(studentDTO.getGraduationYear());
        }
        if (studentDTO.isGraduated() != null) {
            studentEntity.setGraduated(studentDTO.isGraduated());
        }
        if (studentDTO.getSchoolYear() != null) {
            studentEntity.setSchoolYear(studentDTO.getSchoolYear());
        }
        if (studentDTO.isPrefect() != null) {
            studentEntity.setPrefect(studentDTO.isPrefect());
        }

        return convertToDTO(repository.save(studentEntity));
    }
}
