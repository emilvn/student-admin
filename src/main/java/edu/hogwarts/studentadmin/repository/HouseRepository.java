package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, String> {
}
