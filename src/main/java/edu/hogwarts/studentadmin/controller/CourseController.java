package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
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
        return ResponseEntity.noContent().build();
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
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Course course) {
        if(validateCourse(course) != null){
            return validateCourse(course);
        }
        var teacher = teacherRepository.findById(course.getTeacher().getId());
        teacher.ifPresent(course::setTeacher);

        var students = course.getStudents().stream().map(student -> studentRepository.findById(student.getId())).toList();
        students.forEach(student -> student.ifPresent(course.getStudents()::add));
        var newCourse = courseRepository.save(course);

        return ResponseEntity.ok(newCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody Course course, @PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(validateCourse(course) != null){
            return validateCourse(course);
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
        if (invalidTeacher(teacher)) {
            return ResponseEntity.badRequest().build();
        }

        var updatedCourse = courseToUpdate.get();
        updatedCourse.setTeacher(teacher);
        courseRepository.save(updatedCourse);
        return get(id);
    }

    @PutMapping("/{id}/students/{studentId}")
    public ResponseEntity<Object> addStudent(@PathVariable("id") Long id, @PathVariable("studentId") Long studentId){
        var courseToUpdate = courseRepository.findById(id);
        var student = studentRepository.findById(studentId);
        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (student.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid student id.");
        }

        var studentToAdd = student.get();
        var updatedCourse = courseToUpdate.get();
        updatedCourse.getStudents().add(studentToAdd);
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

    private ResponseEntity<Object> validateCourse(Course course) {
        if(course.getSubject() == null) {
            return ResponseEntity.badRequest().body("Subject is required.");
        }
        if (invalidTeacher(course.getTeacher())) {
            return ResponseEntity.badRequest().body("Invalid teacher.");
        }
        if (invalidStudents(course.getStudents())) {
            return ResponseEntity.badRequest().body("Invalid students.");
        }
        return null;
    }

    private boolean invalidTeacher(Teacher teacher) {
        // null teacher is valid
        if(teacher == null) {
            return false;
        }

        // teacher without id is invalid
        if (teacher.getId() == null) {
            return true;
        }
        return teacherRepository.findById(teacher.getId()).isEmpty();
    }

    private boolean invalidStudents(List<Student> students) {
        // null students is invalid
        if(students == null) {
            return true;
        }

        // empty students is valid
        if (students.isEmpty()) {
            return false;
        }
        return !students.stream().allMatch(student -> studentRepository.findById(student.getId()).isPresent());
    }
}
