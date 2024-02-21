package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for the House entity.
 * Only GET methods are implemented.
 */
@RestController
@RequestMapping("/houses")
@CrossOrigin
public class HouseController {
    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    /**
     * Get all houses.
     * @return A list of all houses.
     */
    @GetMapping
    public ResponseEntity<List<House>> getAll() {
        var houses = houseService.getAll();
        if (houses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(houses);
    }

    /**
     * Get a house by name. Ignores case.
     * @param name The name of the house.
     * @return The house with the given name, or 404 if not found.
     */
    @GetMapping("/{name}")
    public ResponseEntity<House> get(@PathVariable String name) {
        var house = houseService.get(name);
        if (house == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(house);
    }
}
