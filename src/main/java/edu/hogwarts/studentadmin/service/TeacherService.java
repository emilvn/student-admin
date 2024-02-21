package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.stereotype.Service;

/**
 * This class provides service methods to manage teachers in the school.
 */
@Service
public class TeacherService extends HogwartsPersonService<Teacher> {
    /**
     * Constructor for TeacherService. Uses dependency injection to get the TeacherRepository and HouseService.
     * @param repository The repository for teachers
     * @param houseService The service for houses
     */
    public TeacherService(TeacherRepository repository, HouseService houseService) {
        super(repository, houseService);
    }

    /**
     * Updates a teacher by their ID. Overwrites the entire teacher with the given new teacher.
     * @param teacher the teacher to update
     * @param id the id of the teacher to update
     * @return the updated teacher or null if the teacher was not found
     */
    public Teacher update(Teacher teacher, Long id) {
        var teacherToUpdate = repository.findById(id);
        if (teacherToUpdate.isPresent()) {
            var updatedTeacher = teacherToUpdate.get();
            var house = houseService.get(teacher.getHouseName());
            updatedTeacher.setHouse(house);
            updatedTeacher.setName(teacher.getName());
            updatedTeacher.setDateOfBirth(teacher.getDateOfBirth());
            updatedTeacher.setEmployment(teacher.getEmployment());
            updatedTeacher.setEmploymentStart(teacher.getEmploymentStart());
            updatedTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            updatedTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            return repository.save(updatedTeacher);
        }
        return null;
    }

    /**
     * Updates a teacher by their ID. Overwrites only the fields of the teacher that are not null in the new teacher.
     * @param teacher the teacher to update
     * @param id the id of the teacher to update
     * @return the updated teacher or null if the teacher was not found
     */
    public Teacher patch(Teacher teacher, Long id) {
        var teacherToUpdate = repository.findById(id);
        if (teacherToUpdate.isPresent()) {
            var updatedTeacher = teacherToUpdate.get();
            if (teacher.getHouseName() != null) {
                var house = houseService.get(teacher.getHouseName());
                updatedTeacher.setHouse(house);
            }
            if (teacher.getName() != null) {
                updatedTeacher.setName(teacher.getFirstName());
            }
            if (teacher.getDateOfBirth() != null) {
                updatedTeacher.setDateOfBirth(teacher.getDateOfBirth());
            }
            if (teacher.getEmployment() != null) {
                updatedTeacher.setEmployment(teacher.getEmployment());
            }
            if (teacher.getEmploymentStart() != null) {
                updatedTeacher.setEmploymentStart(teacher.getEmploymentStart());
            }
            if (teacher.getEmploymentEnd() != null) {
                updatedTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            }
            if (teacher.isHeadOfHouse() != null) {
                updatedTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            }

            return repository.save(updatedTeacher);
        }
        return null;
    }
}
