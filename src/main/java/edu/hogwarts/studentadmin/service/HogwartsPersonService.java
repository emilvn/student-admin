package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.repository.HogwartsPersonRepository;

import java.util.List;

/**
 * This class provides service methods to manage HogwartsPerson entities in the school.
 * @param <M> The type of HogwartsPerson (Student | Teacher).
 */
public abstract class HogwartsPersonService<M extends HogwartsPerson> {
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
    public List<M> getAll() {
        return repository.findAll();
    }

    /**
     * Gets a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to find.
     * @return The HogwartsPerson entity, or null if it does not exist.
     */
    public M get(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Creates a new HogwartsPerson entity.
     * Uses the HouseService to get the House entity by its name and set it on the HogwartsPerson entity.
     * @param person The HogwartsPerson entity to create.
     * @return The created HogwartsPerson entity.
     */
    public M create(M person) {
        var house = houseService.get(person.getHouseName());
        System.out.println(house);
        person.setHouse(house);
        return repository.save(person);
    }

    public abstract M update(M person, Long id);

    public abstract M patch(M person, Long id);

    /**
     * Deletes a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to delete.
     */
    public void delete(Long id) {
        if (id != null) repository.deleteById(id);
    }
}
