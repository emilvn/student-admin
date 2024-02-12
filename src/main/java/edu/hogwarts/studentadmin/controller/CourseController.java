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
        if(!validateTeacher(course.getTeacher()) || !validateStudents(course.getStudents())){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(courseRepository.save(course));
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
            courseRepository.save(updatedCourse);
            return get(id);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = PUT, value = "/{id}/teacher")
    public ResponseEntity<Course> updateTeacher(@RequestBody Teacher teacher, @PathVariable("id") Long id){
        if(teacher.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        var teacherExists = teacherRepository.findById(teacher.getId()).isPresent();
        if(!teacherExists){
            return ResponseEntity.notFound().build();
        }
        var courseToUpdate = courseRepository.findById(id);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.setTeacher(teacher);
            courseRepository.save(updatedCourse);
            return get(id);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = PUT, value = "/{id}/students")
    public ResponseEntity<Course> addStudent(@RequestBody Student student, @PathVariable("id") Long id){
        if(student.getId() == null){
            return ResponseEntity.badRequest().build();
        }
        var studentExists = studentRepository.findById(student.getId()).isPresent();
        if(!studentExists){
            return ResponseEntity.notFound().build();
        }
        var courseToUpdate = this.courseRepository.findById(id);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.getStudents().add(student);
            this.courseRepository.save(updatedCourse);
            courseRepository.flush();
            return get(id);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{id}")
    public ResponseEntity<Course> delete(@PathVariable Long id){
        var courseToDelete = this.courseRepository.findById(id);
        if(courseToDelete.isPresent()){
            this.courseRepository.delete(courseToDelete.get());
            return ResponseEntity.ok(courseToDelete.get());
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{id}/teacher")
    public ResponseEntity<Course> removeTeacher(@PathVariable("id") Long id){
        var courseToUpdate = this.courseRepository.findById(id);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            updatedCourse.setTeacher(null);
            this.courseRepository.save(updatedCourse);
            return get(id);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = DELETE, value = "/{courseId}/students/{studentId}")
    public ResponseEntity<Course> removeStudent(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId){
        var courseToUpdate = this.courseRepository.findById(courseId);
        if(courseToUpdate.isPresent()){
            var updatedCourse = courseToUpdate.get();
            var studentToRemove = updatedCourse.getStudents().stream().filter(student -> student.getId().equals(studentId)).findFirst();
            if(studentToRemove.isPresent()){
                updatedCourse.getStudents().remove(studentToRemove.get());
                this.courseRepository.save(updatedCourse);
                return get(courseId);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    private boolean validateTeacher(Teacher teacher) {
        return teacher == null || teacherRepository.findById(teacher.getId()).isPresent();
    }
    private boolean validateStudents(List<Student> students) {
        return students.isEmpty() || students.stream().allMatch(student -> studentRepository.findById(student.getId()).isPresent());
    }
}
