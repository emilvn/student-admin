package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends HogwartsPersonRepository<Teacher> {
}
