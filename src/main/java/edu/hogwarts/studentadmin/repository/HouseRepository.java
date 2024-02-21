package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the House entity.
 * Provides all JpaRepository methods for the House entity.
 */
public interface HouseRepository extends JpaRepository<House, String> {
}
