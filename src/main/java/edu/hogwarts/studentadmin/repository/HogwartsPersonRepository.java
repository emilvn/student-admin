package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.HogwartsPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HogwartsPersonRepository<M extends HogwartsPerson> extends JpaRepository<M, Long> {
}
