package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides service methods to manage houses in the school.
 */
@Service
public class HouseService {
    private final HouseRepository houseRepository;

    /**
     * Constructor for HouseService. Uses dependency injection to get the HouseRepository.
     * @param houseRepository The repository for houses
     */
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    /**
     * Gets a list of all houses in the database.
     * @return A list of all houses.
     */
    public List<House> getAll() {
        return this.houseRepository.findAll();
    }

    /**
     * Gets a specific house entity by its name.
     * @param name The name of the house.
     * @return The house with the given name, or null if not found.
     */
    public House get(String name) {
        if (name == null || name.isBlank())
            return null;
        return this.houseRepository.findById(name).orElse(null);
    }
}
