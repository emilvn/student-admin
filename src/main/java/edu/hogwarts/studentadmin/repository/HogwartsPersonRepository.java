package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for HogwartsPerson entities.
 * Provides all JpaRepository methods for the HogwartsPerson entity.
 */
public interface HogwartsPersonRepository<M extends HogwartsPerson> extends JpaRepository<M, Long> {
}
