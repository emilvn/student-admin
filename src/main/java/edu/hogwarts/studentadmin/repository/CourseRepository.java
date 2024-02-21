package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Course entities.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
}
