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

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseController(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository){
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Course>> getAll(){
        var courses = this.courseRepository.findAll();
        if(!courses.isEmpty()){
            return ResponseEntity.ok(courses);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = GET, value = "/{id}")
    public ResponseEntity<Course> get(@PathVariable("id") Long id){
        var course = this.courseRepository.findById(id);
        if(course.isPresent()){
            return ResponseEntity.ok(course.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = GET, value = "/{id}/teacher")
    public ResponseEntity<Teacher> getTeacher(@PathVariable("id") Long id){
        var course = this.courseRepository.findById(id);
        if(course.isPresent()){
            var teacher = course.get().getTeacher();
            if(teacher != null){
                return ResponseEntity.ok(teacher);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = GET, value = "/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable("id") Long id){
        var course = this.courseRepository.findById(id);
        if(course.isPresent()){
            var students = course.get().getStudents();
            if(!students.isEmpty()){
                return ResponseEntity.ok(students);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Course> create(@RequestBody Course course){
        var newCourse = this.courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @RequestMapping(method = PUT, value = "/{id}")
    public ResponseEntity<Course> update(@RequestBody Course course, @PathVariable("id") Long id){
        var courseToUpdate = this.courseRepository.findById(id);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.setSubject(course.getSubject());
            updatedCourse.setSchoolYear(course.getSchoolYear());
            updatedCourse.setCurrent(course.isCurrent());
            updatedCourse.setTeacher(course.getTeacher());
            updatedCourse.setStudents(course.getStudents());
            this.courseRepository.save(updatedCourse);
            return ResponseEntity.ok(updatedCourse);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = PUT, value = "/{id}/teacher")
    public ResponseEntity<Teacher> updateTeacher(@RequestBody Teacher teacher, @PathVariable("id") Long id){
        var courseToUpdate = this.courseRepository.findById(id);
        var teacherExists = this.teacherRepository.findById(teacher.getId()).isPresent();
        Teacher teacherToAdd = teacher;
        if(!teacherExists){
            teacherToAdd = this.teacherRepository.save(teacher);
        }
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.setTeacher(teacherToAdd);
            this.courseRepository.save(updatedCourse);
            return ResponseEntity.ok(updatedCourse.getTeacher());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = PUT, value = "/{id}/students")
    public ResponseEntity<List<Student>> addStudent(@RequestBody Student student, @PathVariable("id") Long id){
        var courseToUpdate = this.courseRepository.findById(id);
        var studentExists = this.studentRepository.findById(student.getId()).isPresent();
        Student studentToAdd = student;
        if(!studentExists){
            studentToAdd = this.studentRepository.save(student);
        }
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.getStudents().add(studentToAdd);
            this.courseRepository.save(updatedCourse);
            return ResponseEntity.ok(updatedCourse.getStudents());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Course> delete(@PathVariable Long id){
        var courseToDelete = this.courseRepository.findById(id);
        if(courseToDelete.isPresent()){
            this.courseRepository.delete(courseToDelete.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{id}/teacher")
    public ResponseEntity<Teacher> removeTeacher(@PathVariable("id") Long id){
        var courseToUpdate = this.courseRepository.findById(id);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.setTeacher(null);
            this.courseRepository.save(updatedCourse);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{courseId}/students/{studentId}")
    public ResponseEntity<Student> removeStudent(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId){
        var courseToUpdate = this.courseRepository.findById(courseId);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            var studentToRemove = updatedCourse.getStudents().stream().filter(student -> student.getId().equals(studentId)).findFirst();
            if(studentToRemove.isPresent()){
                updatedCourse.getStudents().remove(studentToRemove.get());
                this.courseRepository.save(updatedCourse);
                return ResponseEntity.ok(studentToRemove.get());
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}
