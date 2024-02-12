package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseController(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Course>> getAll() {
        var courses = courseRepository.findAll();
        if (!courses.isEmpty()) {
            return ResponseEntity.ok(courses);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = GET, value = "/{id}")
    public ResponseEntity<Course> get(@PathVariable("id") Long id) {
        var course = courseRepository.findById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = GET, value = "/{id}/teacher")
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

    @RequestMapping(method = GET, value = "/{id}/students")
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

    @RequestMapping(method = POST)
    public ResponseEntity<Course> create(@RequestBody Course course) {
        if (!validateTeacher(course.getTeacher()) || !validateStudents(course.getStudents())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(courseRepository.save(course));
    }

    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<Course> update(@RequestBody Course course, @PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!validateStudents(course.getStudents()) || !validateTeacher(course.getTeacher())) {
            return ResponseEntity.badRequest().build();
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

    @RequestMapping(method = PUT, value = "/{id}/teacher")
    public ResponseEntity<Course> updateTeacher(@RequestBody Teacher teacher, @PathVariable("id") Long id) {
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

    @RequestMapping(method = PUT, value = "/{id}/students")
    public ResponseEntity<Course> addStudent(@RequestBody Student student, @PathVariable("id") Long id) {
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

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Course> delete(@PathVariable("id") Long id) {
        var courseToDelete = courseRepository.findById(id);

        if (courseToDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        courseRepository.deleteById(id);
        return ResponseEntity.ok(courseToDelete.get());
    }

    @RequestMapping(method = DELETE, value = "/{id}/teacher")
    public ResponseEntity<Course> removeTeacher(@PathVariable("id") Long id) {
        var courseToUpdate = courseRepository.findById(id);

        if (courseToUpdate.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var updatedCourse = courseToUpdate.get();
        updatedCourse.setTeacher(null);
        courseRepository.save(updatedCourse);
        return get(id);
    }

    @RequestMapping(method = DELETE, value = "/{courseId}/students/{studentId}")
    public ResponseEntity<Course> removeStudent(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId) {
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
        if (teacher.getId() == null) {
            return true;
        }
        return teacherRepository.findById(teacher.getId()).isPresent();
    }

    private boolean validateStudent(Student student) {
        if (student.getId() == null) {
            return false;
        }
        return studentRepository.findById(student.getId()).isPresent();
    }

    private boolean validateStudents(List<Student> students) {
        if (students.isEmpty()) {
            return true;
        }
        return students.stream().allMatch(student -> studentRepository.findById(student.getId()).isPresent());
    }
}
