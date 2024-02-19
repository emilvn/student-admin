package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.service.HouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/houses")
@CrossOrigin
public class HouseController {
    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public ResponseEntity<List<House>> getAll() {
        var houses = houseService.getAll();
        if (houses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(houses);
    }

    @GetMapping("/{name}")
    public ResponseEntity<House> get(@PathVariable String name) {
        var house = houseService.get(name);
        if (house == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(house);
    }
}
