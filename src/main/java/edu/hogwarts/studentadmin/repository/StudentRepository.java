package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends HogwartsPersonRepository<Student> {
    @Query("SELECT s FROM student s WHERE CONCAT(LOWER(s.firstName), ' ', COALESCE(LOWER(s.middleName), ''), ' ', COALESCE(LOWER(s.lastName), '')) = LOWER(:fullName)")
    Optional<Student> findByFullName(@Param("fullName") String fullName);
}
