package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.repository.HogwartsPersonRepository;

import java.util.List;

public abstract class HogwartsPersonService<M extends HogwartsPerson> {
    protected final HouseService houseService;
    protected final HogwartsPersonRepository<M> repository;

    public HogwartsPersonService(HogwartsPersonRepository<M> repository, HouseService houseService) {
        this.repository = repository;
        this.houseService = houseService;
    }

    public List<M> getAll() {
        return repository.findAll();
    }

    public M get(Long id) {
        return repository.findById(id).orElse(null);
    }

    public M create(M person) {
        var house = houseService.get(person.getHouseName());
        System.out.println(house);
        person.setHouse(house);
        return repository.save(person);
    }

    public abstract M update(M person, Long id);

    public abstract M patch(M person, Long id);

    public void delete(Long id) {
        if (id != null) repository.deleteById(id);
    }
}
