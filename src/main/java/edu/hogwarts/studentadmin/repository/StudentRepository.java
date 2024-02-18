package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends HogwartsPersonRepository<Student> {
}
