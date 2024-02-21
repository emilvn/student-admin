package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.dto.HogwartsPersonDTO;
import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.repository.HogwartsPersonRepository;

import java.util.List;

/**
 * This class provides service methods to manage HogwartsPerson entities in the school.
 * @param <M> The type of HogwartsPerson (Student | Teacher).
 * @param <D> The DTO for the HogwartsPerson (StudentDTO | TeacherDTO).
 */
public abstract class HogwartsPersonService<M extends HogwartsPerson, D extends HogwartsPersonDTO> {
    protected final HouseService houseService;
    protected final HogwartsPersonRepository<M> repository;

    /**
     * Constructor for HogwartsPersonService. Uses dependency injection to get the repository and HouseService.
     * @param repository The repository for HogwartsPerson entities.
     * @param houseService The service for House entities.
     */
    public HogwartsPersonService(HogwartsPersonRepository<M> repository, HouseService houseService) {
        this.repository = repository;
        this.houseService = houseService;
    }

    /**
     * Must be implemented by the subclasses to get a list of all HogwartsPerson entities of the given type.
     * @return A list of all HogwartsPerson entities of the given type.
     */
    public abstract List<D> getAll();

    /**
     * Must be implemented by the subclasses to get a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to find.
     * @return The HogwartsPerson entity, or null if it does not exist.
     */
    public abstract D get(Long id);

    /**
     * Gets a HogwartsPerson entity by its ID.
     * Needed for the CourseService to get the teacher/student entities to add to a course.
     * @param id The ID of the HogwartsPerson entity to find.
     * @return The HogwartsPerson entity, or null if it does not exist.
     */
    public M getEntity(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Must be implemented by the subclasses to add a new HogwartsPerson entity to the database.
     * @param personDTO The DTO for the new HogwartsPerson entity.
     * @return The new HogwartsPerson entity.
     */
    public abstract D create(D personDTO);

    /**
     * Must be implemented by the subclasses to update a HogwartsPerson entity in the database.
     * Must overwrite the entity with all the new values from the DTO.
     * @param person The updated HogwartsPerson entity.
     * @param id The ID of the HogwartsPerson entity to update.
     * @return The updated HogwartsPerson entity.
     */
    public abstract D update(D person, Long id);

    /**
     * Must be implemented by the subclasses to update a HogwartsPerson entity in the database.
     * Must overwrite only the fields that are not null in the DTO.
     * @param person The updated HogwartsPerson entity.
     * @param id The ID of the HogwartsPerson entity to update.
     * @return The updated HogwartsPerson entity.
     */
    public abstract D patch(D person, Long id);

    /**
     * Deletes a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to delete.
     */
    public void delete(Long id) {
        if (id != null) repository.deleteById(id);
    }
}
