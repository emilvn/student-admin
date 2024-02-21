package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Course entities.
 * Provides all JpaRepository methods for the Course entity.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
}
