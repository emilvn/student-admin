package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseController(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        var courses = courseRepository.findAll();
        if (!courses.isEmpty()) {
            return ResponseEntity.ok(courses);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        var course = courseRepository.findById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/teacher")
    public ResponseEntity<Teacher> getTeacher(@PathVariable("id") Long id) {
        var course = courseRepository.findById(id);
        if (course.isPresent()) {
            var teacher = course.get().getTeacher();
            if (teacher != null) {
                return ResponseEntity.ok(teacher);
            }
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable("id") Long id) {
        var course = courseRepository.findById(id);
        if (course.isPresent()) {
            var students = course.get().getStudents();
            if (!students.isEmpty()) {
                return ResponseEntity.ok(students);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Course course) {
        if (!validateTeacher(course.getTeacher())) {
            return ResponseEntity.badRequest().body("Invalid teacher.");
        }
        if (!validateStudents(course.getStudents())) {
            return ResponseEntity.badRequest().body("Invalid students.");
        }

        var newCourse = courseRepository.save(course);
        return ResponseEntity.ok(newCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody Course course, @PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!validateTeacher(course.getTeacher())) {
            return ResponseEntity.badRequest().body("Invalid teacher.");
        }
        if (!validateStudents(course.getStudents())) {
            return ResponseEntity.badRequest().body("Invalid students.");
        }

        var updatedCourse = courseToUpdate.get();
        updatedCourse.setSubject(course.getSubject());
        updatedCourse.setSchoolYear(course.getSchoolYear());
        updatedCourse.setCurrent(course.isCurrent());
        updatedCourse.setTeacher(course.getTeacher());
        updatedCourse.setStudents(course.getStudents());
        courseRepository.save(updatedCourse);
        return get(id);
    }

    @PutMapping("/{id}/teacher")
    public ResponseEntity<Object> updateTeacher(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!validateTeacher(teacher)) {
            return ResponseEntity.badRequest().build();
        }

        var updatedCourse = courseToUpdate.get();
        updatedCourse.setTeacher(teacher);
        courseRepository.save(updatedCourse);
        return get(id);
    }

    @PutMapping("/{id}/students")
    public ResponseEntity<Object> addStudent(@RequestBody Student student, @PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!validateStudent(student)) {
            return ResponseEntity.badRequest().build();
        }

        var updatedCourse = courseToUpdate.get();
        updatedCourse.getStudents().add(student);
        courseRepository.save(updatedCourse);
        return get(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Course> delete(@PathVariable("id") Long id) {
        var courseToDelete = courseRepository.findById(id);

        if (courseToDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        courseRepository.deleteById(id);
        return ResponseEntity.ok(courseToDelete.get());
    }

    @DeleteMapping("/{id}/teacher")
    public ResponseEntity<Object> removeTeacher(@PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedCourse = courseToUpdate.get();
        updatedCourse.setTeacher(null);
        courseRepository.save(updatedCourse);
        return get(id);
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<Object> removeStudent(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId) {
        var courseToUpdate = courseRepository.findById(courseId);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedCourse = courseToUpdate.get();
        var studentToRemove = updatedCourse.getStudents().stream().filter(student -> student.getId().equals(studentId)).findFirst();

        if (studentToRemove.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        updatedCourse.getStudents().remove(studentToRemove.get());
        courseRepository.save(updatedCourse);
        return get(courseId);
    }

    private boolean validateTeacher(Teacher teacher) {
        if(teacher == null) {
            return false;
        }
        if (teacher.getId() == null) {
            return false;
        }
        return teacherRepository.findById(teacher.getId()).isPresent();
    }

    private boolean validateStudent(Student student) {
        if(student == null) {
            return false;
        }
        if (student.getId() == null) {
            return false;
        }
        return studentRepository.findById(student.getId()).isPresent();
    }

    private boolean validateStudents(List<Student> students) {
        if(students == null) {
            return false;
        }
        if (students.isEmpty()) {
            return true;
        }
        return students.stream().allMatch(student -> studentRepository.findById(student.getId()).isPresent());
    }
}
