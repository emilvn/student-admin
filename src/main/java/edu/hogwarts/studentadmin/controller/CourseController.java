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

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public CourseController(CourseService courseService, TeacherService teacherService, StudentService studentService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        var courses = courseService.getAll();
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

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

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Course course) {
        if (course.getSubject() == null) {
            return ResponseEntity.badRequest().body("Subject is required.");
        }
        if (course.getTeacher() != null) {
            if (teacherService.get(course.getTeacher().getId()) == null) {
                return ResponseEntity.badRequest().body("Invalid teacher id.");
            }
        }
        return ResponseEntity.ok(courseService.create(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody Course course, @PathVariable("id") Long id) {
        var updatedCourse = courseService.update(id, course);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patch(@RequestBody Course course, @PathVariable("id") Long id) {
        var updatedCourse = courseService.patch(id, course);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    @PatchMapping("/{id}/teacher")
    public ResponseEntity<Object> updateTeacher(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
        var updatedCourse = courseService.updateTeacher(id, teacher);
        if (updatedCourse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCourse);
    }

    @PutMapping("/{id}/students/{studentId}")
    public ResponseEntity<Object> addStudent(@PathVariable("id") Long id, @PathVariable("studentId") Long studentId) {
        var courseToUpdate = courseService.get(id);
        var student = studentService.get(studentId);
        if (courseToUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        if (student == null) {
            return ResponseEntity.badRequest().body("Invalid student id.");
        }
        var updatedCourse = courseService.addStudent(id, studentId);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> delete(@PathVariable("id") Long id) {
        var course = courseService.get(id);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        courseService.delete(id);
        return ResponseEntity.ok(course);
    }

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
