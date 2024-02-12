package edu.hogwarts.studentadmin.repository;

import edu.hogwarts.studentadmin.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
