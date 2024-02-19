package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.House;
import edu.hogwarts.studentadmin.repository.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> getAll() {
        return this.houseRepository.findAll();
    }

    public House get(String name) {
        if(name == null || name.isBlank())
            return null;
        return this.houseRepository.findById(name).orElse(null);
    }
}
