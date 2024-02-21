package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for the House entity.
 */
@Service
public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    /**
     * Get all houses.
     * @return A list of all houses.
     */
    public List<House> getAll() {
        return this.houseRepository.findAll();
    }

    /**
     * Get a house by name.
     * @param name The name of the house.
     * @return The house with the given name, or null if not found.
     */
    public House get(String name) {
        if (name == null || name.isBlank())
            return null;
        return this.houseRepository.findById(name).orElse(null);
    }
}
