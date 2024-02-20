package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.service.CourseService;
import edu.hogwarts.studentadmin.service.StudentService;
import edu.hogwarts.studentadmin.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is a REST controller for courses.
 * It handles requests related to courses, such as getting, creating, updating and deleting courses.
 */
@RestController
@RequestMapping("/courses")
@CrossOrigin
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public CourseController(CourseService courseService, TeacherService teacherService, StudentService studentService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    /**
     * Get all courses
     *
     * @return An HTTP response containing a list of all courses or a 204 status code if there are no courses
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        var courses = courseService.getAll();
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

    /**
     * Get a course by its id
     *
     * @param id The id of the course given as a path variable
     * @return An HTTP response containing the course with the given id, or a 404 status code if it doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    /**
     * Get the teacher of a specified course
     * @param id The id of the course given as a path variable
     * @return An HTTP response containing the teacher of the course with the given id, a 404 if the course doesn't exist, or a 204 status code if it doesn't have a teacher
     */
    @GetMapping("/{id}/teacher")
    public ResponseEntity<Teacher> getTeacher(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        var teacher = course.getTeacher();
        if (teacher == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(teacher);
    }

    /**
     * Get the students of a specified course
     * @param id The id of the course given as a path variable
     * @return An HTTP response containing the students of the course with the given id, a 404 if the course doesn't exist, or a 204 status code if it doesn't have students
     */
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        var students = course.getStudents();
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    /**
     * Create a new course
     * @param course The course to create, given as a request body
     * @return An HTTP response containing the created course, or a 400 status code if the course data is invalid
     */
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Course course) {
        if (course.getTeacher() != null) {
            if (teacherService.get(course.getTeacher().getId()) == null) {
                return ResponseEntity.badRequest().body("Invalid teacher id.");
            }
        }
        if (course.getStudents() != null) {
            var studentsExist = course.getStudents().stream().allMatch(student -> studentService.get(student.getId()) != null);
            if (!studentsExist) {
                return ResponseEntity.badRequest().body("Invalid student id(s).");
            }
            for (var student : course.getStudents()) {
                if (studentService.get(student.getName()).getSchoolYear() != course.getSchoolYear()) {
                    return ResponseEntity.badRequest().body("Invalid school year for student(s).");
                }
            }
        }
        return ResponseEntity.ok(courseService.create(course));
    }

    /**
     * Update a course
     * @param course The new course data, given as a request body
     * @param id The id of the course to update, given as a path variable
     * @return An HTTP response containing the updated course, or a 404 status code if the course doesn't exist, or a 400 status code if the course data is invalid
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody Course course, @PathVariable("id") Long id) {
        if (course.getTeacher() != null) {
            if (teacherService.get(course.getTeacher().getId()) == null) {
                return ResponseEntity.badRequest().body("Invalid teacher id.");
            }
        }
        if (course.getStudents() != null) {
            var studentsExist = course.getStudents().stream().allMatch(student -> studentService.get(student.getId()) != null);
            if (!studentsExist) {
                return ResponseEntity.badRequest().body("Invalid student id(s).");
            }
            for (var student : course.getStudents()) {
                if (studentService.get(student.getName()).getSchoolYear() != courseService.get(id).getSchoolYear()) {
                    return ResponseEntity.badRequest().body("Invalid school year for student(s).");
                }
            }
        }
        var updatedCourse = courseService.update(id, course);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Update a course partially
     * @param course The new course data, given as a request body
     * @param id The id of the course to update, given as a path variable
     * @return An HTTP response containing the updated course, or a 404 status code if the course doesn't exist, or a 400 status code if the course data is invalid
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@RequestBody Course course, @PathVariable("id") Long id) {
        if (course.getTeacher() != null) {
            if (teacherService.get(course.getTeacher().getId()) == null) {
                return ResponseEntity.badRequest().body("Invalid teacher id.");
            }
        }
        if (course.getStudents() != null) {
            var studentsExist = course.getStudents().stream().allMatch(student -> studentService.get(student.getId()) != null);
            if (!studentsExist) {
                return ResponseEntity.badRequest().body("Invalid student id(s).");
            }
            for (var student : course.getStudents()) {
                if (studentService.get(student.getName()).getSchoolYear() != courseService.get(id).getSchoolYear()) {
                    return ResponseEntity.badRequest().body("Invalid school year for student(s).");
                }
            }
        }
        var updatedCourse = courseService.patch(id, course);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Update the teacher of a specified course
     * @param teacher The new teacher data, given as a request body
     * @param id The id of the course to update, given as a path variable
     * @return An HTTP response containing the updated course, or a 404 status code if the course doesn't exist
     */
    @PutMapping("/{id}/teacher")
    public ResponseEntity<Object> updateTeacher(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
        var updatedCourse = courseService.updateTeacher(id, teacher);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Add students to a specified course
     * @param id The id of the course to update, given as a path variable
     * @param students The students to add, given as a request body, must contain at least either student ids or names
     * @return An HTTP response containing the updated course, or a 404 status code if the course doesn't exist, or a 400 status code if the student data is invalid
     */
    @PostMapping("/{id}/students")
    public ResponseEntity<Object> addStudents(@PathVariable("id") Long id, @RequestBody List<Student> students) {
        Course updatedCourse;
        var useIds = students.stream().allMatch(student -> student.getId() != null);
        var useNames = students.stream().allMatch(student -> student.getName() != null);
        if (!useIds && !useNames) {
            return ResponseEntity.badRequest().body("Invalid request. Must be either student ids or names.");
        }
        if (useIds) {
            var studentsExist = students.stream().allMatch(student -> studentService.get(student.getId()) != null);
            if (!studentsExist) {
                return ResponseEntity.badRequest().body("Invalid student id(s).");
            }
            for (var student : students) {
                if (studentService.get(student.getId()).getSchoolYear() != courseService.get(id).getSchoolYear()) {
                    return ResponseEntity.badRequest().body("Invalid school year.");
                }
            }
            updatedCourse = courseService.addStudentsById(id, students);
        } else {
            var studentsExist = students.stream().allMatch(student -> studentService.get(student.getName()) != null);
            if (!studentsExist) {
                return ResponseEntity.badRequest().body("Invalid student name(s).");
            }
            for (var student : students) {
                if (studentService.get(student.getName()).getSchoolYear() != courseService.get(id).getSchoolYear()) {
                    return ResponseEntity.badRequest().body("Invalid school year.");
                }
            }
            updatedCourse = courseService.addStudentsByName(id, students);
        }
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Delete a course
     * @param id The id of the course to delete, given as a path variable
     * @return An HTTP response containing the deleted course, or a 404 status code if the course doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Course> delete(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        courseService.delete(id);
        return ResponseEntity.ok(course);
    }

    /**
     * Remove the teacher of a specified course
     * @param id The id of the course to update, given as a path variable
     * @return An HTTP response containing the removed teacher, or a 404 status code if the course doesn't exist
     */
    @DeleteMapping("/{id}/teacher")
    public ResponseEntity<Object> removeTeacher(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        var teacher = course.getTeacher();
        courseService.removeTeacher(id);
        return ResponseEntity.ok(teacher);
    }

    /**
     * Remove a student from a specified course
     * @param courseId The id of the course to update, given as a path variable
     * @param studentId The id of the student to remove, given as a path variable
     * @return An HTTP response containing the removed student, a 404 if the course doesn't exist, or a 400 status code if the student doesn't exist
     */
    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Object> removeStudent(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId) {
        var course = courseService.get(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        var student = studentService.get(studentId);
        if (student == null) {
            return ResponseEntity.badRequest().body("Invalid student id.");
        }
        courseService.removeStudent(courseId, studentId);
        return ResponseEntity.ok(student);
    }
}
