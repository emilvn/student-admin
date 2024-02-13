package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final HouseRepository houseRepository;

    public TeacherService(TeacherRepository teacherRepository, HouseRepository houseRepository) {
        this.teacherRepository = teacherRepository;
        this.houseRepository = houseRepository;
    }

    public List<Teacher> getAll() {
        return this.teacherRepository.findAll();
    }

    public Teacher get(Long id) {
        return this.teacherRepository.findById(id).orElse(null);
    }

    public Teacher create(Teacher teacher) {
        var house = this.houseRepository.findById(teacher.getHouse().getId());
        house.ifPresent(teacher::setHouse);
        return this.teacherRepository.save(teacher);
    }

    public Teacher update(Teacher teacher, Long id) {
        var teacherToUpdate = this.teacherRepository.findById(id);
        if (teacherToUpdate.isPresent()) {
            var updatedTeacher = teacherToUpdate.get();
            if(teacher.getHouse() != null) {
                var house = this.houseRepository.findById(teacher.getHouse().getId());
                house.ifPresent(updatedTeacher::setHouse);
            }
            if(teacher.getFirstName() != null) {
                updatedTeacher.setFirstName(teacher.getFirstName());
            }
            if(teacher.getMiddleName() != null) {
                updatedTeacher.setMiddleName(teacher.getMiddleName());
            }
            if(teacher.getLastName() != null) {
                updatedTeacher.setLastName(teacher.getLastName());
            }
            if(teacher.getDateOfBirth() != null) {
                updatedTeacher.setDateOfBirth(teacher.getDateOfBirth());
            }
            if(teacher.getEmployment() != null) {
                updatedTeacher.setEmployment(teacher.getEmployment());
            }
            if(teacher.getEmploymentStart() != null) {
                updatedTeacher.setEmploymentStart(teacher.getEmploymentStart());
            }
            if(teacher.getEmploymentEnd() != null) {
                updatedTeacher.setEmploymentEnd(teacher.getEmploymentEnd());
            }
            updatedTeacher.setHeadOfHouse(teacher.isHeadOfHouse());

            return this.teacherRepository.save(updatedTeacher);
        }
        return null;
    }

    public void delete(Long id) {
        if(id != null){
            this.teacherRepository.deleteById(id);
        }
    }
}
