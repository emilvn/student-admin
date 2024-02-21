package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.repository.HogwartsPersonRepository;

import java.util.List;

/**
 * Service for HogwartsPerson entities.
 * @param <M> The type of HogwartsPerson (Student | Teacher).
 */
public abstract class HogwartsPersonService<M extends HogwartsPerson> {
    protected final HouseService houseService;
    protected final HogwartsPersonRepository<M> repository;

    public HogwartsPersonService(HogwartsPersonRepository<M> repository, HouseService houseService) {
        this.repository = repository;
        this.houseService = houseService;
    }

    /**
     * Get all HogwartsPerson entities.
     * @return A list of HogwartsPerson entities.
     */
    public List<M> getAll() {
        return repository.findAll();
    }

    /**
     * Get a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity.
     * @return The HogwartsPerson entity, or null if it does not exist.
     */
    public M get(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Create a new HogwartsPerson entity.
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
     * Delete a HogwartsPerson entity by its ID.
     * @param id The ID of the HogwartsPerson entity to delete.
     */
    public void delete(Long id) {
        if (id != null) repository.deleteById(id);
    }
}
