package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService extends HogwartsPersonService<Teacher> {

    public TeacherService(TeacherRepository repository, HouseService houseService) {
        super(repository, houseService);
    }

    public Teacher update(Teacher teacher, Long id) {
        var teacherToUpdate = repository.findById(id);
        if (teacherToUpdate.isPresent()) {
            var updatedTeacher = teacherToUpdate.get();
            var house = houseService.get(teacher.getHouseName());
            updatedTeacher.setHouse(house);
            updatedTeacher.setFullName(teacher.getFullName());
            updatedTeacher.setDateOfBirth(teacher.getDateOfBirth());
            updatedTeacher.setEmployment(teacher.getEmployment());
            updatedTeacher.setEmploymentStart(teacher.getEmploymentStart());
            updatedTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            updatedTeacher.setHeadOfHouse(teacher.isHeadOfHouse());
            return repository.save(updatedTeacher);
        }
        return null;
    }

    public Teacher patch(Teacher teacher, Long id) {
        var teacherToUpdate = repository.findById(id);
        if (teacherToUpdate.isPresent()) {
            var updatedTeacher = teacherToUpdate.get();
            if(teacher.getHouseName() != null){
                var house = houseService.get(teacher.getHouseName());
                updatedTeacher.setHouse(house);
            }
            if (teacher.getFullName() != null) {
                updatedTeacher.setFullName(teacher.getFirstName());
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
            updatedTeacher.setHeadOfHouse(teacher.isHeadOfHouse());

            return repository.save(updatedTeacher);
        }
        return null;
    }
}
