package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.dto.HogwartsPersonDTO;
import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.model.Teacher;
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
     * Gets a list of all the HogwartsPerson entities of the given type.
     * @return A list of all HogwartsPerson entities of the given type.
     */
    public abstract List<D> getAll();

    /**
     * Gets a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to find.
     * @return The HogwartsPerson entity, or null if it does not exist.
     */
    public abstract D get(Long id);

    public M getEntity(Long id) {
        return repository.findById(id).orElse(null);
    }

    public abstract D create(D personDTO);

    public abstract D update(D person, Long id);

    public abstract D patch(D person, Long id);

    /**
     * Deletes a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to delete.
     */
    public void delete(Long id) {
        if (id != null) repository.deleteById(id);
    }
}
