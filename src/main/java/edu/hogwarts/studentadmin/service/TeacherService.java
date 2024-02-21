package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.dto.TeacherDTO;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides service methods to manage teachers in the school.
 */
@Service
public class TeacherService extends HogwartsPersonService<Teacher, TeacherDTO> {
    /**
     * Constructor for TeacherService. Uses dependency injection to get the TeacherRepository and HouseService.
     * @param repository The repository for teachers
     * @param houseService The service for houses
     */
    public TeacherService(TeacherRepository repository, HouseService houseService) {
        super(repository, houseService);
    }

    /**
     * Converts a teacher entity to a teacher DTO.
     * @param teacher The teacher entity to convert.
     * @return The teacher DTO.
     */
    public TeacherDTO convertToDTO(Teacher teacher) {
        var teacherDTO = new TeacherDTO();
        if(teacher.getId() != null) {
            teacherDTO.setId(teacher.getId());
        }
        if(teacher.getHouse() != null) {
            teacherDTO.setHouse(teacher.getHouse());
        }
        teacherDTO.setFirstName(teacher.getFirstName());
        teacherDTO.setMiddleName(teacher.getMiddleName());
        teacherDTO.setLastName(teacher.getLastName());
        teacherDTO.setDateOfBirth(teacher.getDateOfBirth());
        teacherDTO.setEmployment(teacher.getEmployment());
        teacherDTO.setEmploymentStart(teacher.getEmploymentStart());
        teacherDTO.setEmploymentEnd(teacher.getEmploymentEnd());
        teacherDTO.setHeadOfHouse(teacher.isHeadOfHouse());
        return teacherDTO;
    }

    /**
     * Converts a teacher DTO to a teacher entity.
     * @param teacherDTO The teacher DTO to convert.
     * @return The teacher entity.
     */
    public Teacher convertToEntity(TeacherDTO teacherDTO) {
        var teacherEntity = new Teacher();
        if(teacherDTO.getId() != null) {
            teacherEntity.setId(teacherDTO.getId());
        }

        teacherEntity.setHouse(houseService.get(teacherDTO.getHouseName()));
        teacherEntity.setFirstName(teacherDTO.getFirstName());
        teacherEntity.setMiddleName(teacherDTO.getMiddleName());
        teacherEntity.setLastName(teacherDTO.getLastName());
        teacherEntity.setDateOfBirth(teacherDTO.getDateOfBirth());
        teacherEntity.setEmployment(teacherDTO.getEmployment());
        teacherEntity.setEmploymentStart(teacherDTO.getEmploymentStart());
        teacherEntity.setEmploymentEnd(teacherDTO.getEmploymentEnd());
        teacherEntity.setHeadOfHouse(teacherDTO.isHeadOfHouse());
        return teacherEntity;
    }

    /**
     * Gets a list of all the teachers.
     * @return A list of all teachers.
     */
    public List<TeacherDTO> getAll() {
        var teachers = repository.findAll();
        return teachers.stream().map(this::convertToDTO).toList();
    }

    /**
     * Gets a teacher by their ID.
     * @param id The ID of the HogwartsPerson entity to find.
     * @return The teacher entity, or null if it does not exist.
     */
    public TeacherDTO get(Long id) {
        var teacher = repository.findById(id).orElse(null);
        if (teacher == null) {
            return null;
        }
        return convertToDTO(teacher);
    }

    /**
     * Creates a new teacher from the given teacher DTO.
     * @param teacherDTO the teacher DTO to create the teacher from
     * @return the created teacher
     */
    public TeacherDTO create(TeacherDTO teacherDTO) {
        var teacherEntity = convertToEntity(teacherDTO);
        return convertToDTO(repository.save(teacherEntity));
    }

    /**
     * Updates a teacher by their ID. Overwrites the entire teacher with the given teacher DTO.
     * @param teacherDTO the DTO of teacher to update
     * @param id the id of the teacher to update
     * @return the updated teacher or null if the teacher was not found
     */
    public TeacherDTO update(TeacherDTO teacherDTO, Long id) {
        var teacherEntity = repository.findById(id).orElse(null);
        if(teacherEntity == null){
            return null;
        }
        teacherEntity = convertToEntity(teacherDTO);
        return convertToDTO(repository.save(teacherEntity));
    }

    /**
     * Updates a teacher by their ID, with a given partial teacher DTO.
     * Overwrites only the fields of the teacher that are not null in the new teacher DTO.
     * @param teacherDTO the DTO of the teacher to update
     * @param id the id of the teacher to update
     * @return the updated teacher or null if the teacher was not found
     */
    public TeacherDTO patch(TeacherDTO teacherDTO, Long id) {
        var teacherEntity = repository.findById(id).orElse(null);
        if (teacherEntity == null) {
            return null;
        }
        if (teacherDTO.getHouseName() != null) {
            var house = houseService.get(teacherDTO.getHouseName());
            teacherEntity.setHouse(house);
        }
        if (teacherDTO.getName() != null) {
            teacherEntity.setFirstName(teacherDTO.getFirstName());
            teacherEntity.setMiddleName(teacherDTO.getMiddleName());
            teacherEntity.setLastName(teacherDTO.getLastName());
        }
        if (teacherDTO.getDateOfBirth() != null) {
            teacherEntity.setDateOfBirth(teacherDTO.getDateOfBirth());
        }
        if (teacherDTO.getEmployment() != null) {
            teacherEntity.setEmployment(teacherDTO.getEmployment());
        }
        if (teacherDTO.getEmploymentStart() != null) {
            teacherEntity.setEmploymentStart(teacherDTO.getEmploymentStart());
        }
        if (teacherDTO.getEmploymentEnd() != null) {
            teacherEntity.setEmploymentEnd(teacherDTO.getEmploymentEnd());
        }
        if (teacherDTO.isHeadOfHouse() != null) {
            teacherEntity.setHeadOfHouse(teacherDTO.isHeadOfHouse());
        }

        return convertToDTO(repository.save(teacherEntity));
    }
}
