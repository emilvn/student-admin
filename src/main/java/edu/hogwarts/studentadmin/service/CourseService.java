package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, TeacherService teacherService, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course get(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course create(Course course) {
        if (course.getTeacher() != null) {
            var teacher = teacherService.get(course.getTeacher().getId());
            course.setTeacher(teacher);
        }
        if (course.getStudents() != null) {
            var students = course.getStudents()
                    .stream()
                    .map(student -> studentService.get(student.getId()))
                    .toList();
            course.setStudents(students);
        }
        return courseRepository.save(course);
    }

    public Course update(Long id, Course course) {
        var courseToUpdate = get(id);
        if (courseToUpdate == null) {
            return null;
        }
        courseToUpdate.setSubject(course.getSubject());
        if (course.getTeacher() != null) {
            var teacher = teacherService.get(course.getTeacher().getId());
            courseToUpdate.setTeacher(teacher);
        }
        else{
            courseToUpdate.setTeacher(null);
        }
        if (course.getStudents() != null) {
            var students = course.getStudents()
                    .stream()
                    .map(student -> studentService.get(student.getId()))
                    .toList();
            courseToUpdate.setStudents(new ArrayList<>(students));
        }
        else{
            courseToUpdate.setStudents(new ArrayList<>());
        }
        courseToUpdate.setSchoolYear(course.getSchoolYear());
        courseToUpdate.setCurrent(course.isCurrent());
        return courseRepository.save(courseToUpdate);
    }

    public Course patch(Long id, Course course) {
        var courseToUpdate = get(id);
        if (courseToUpdate == null) {
            return null;
        }
        if (course.getSubject() != null) {
            courseToUpdate.setSubject(course.getSubject());
        }
        if (course.getTeacher() != null) {
            var teacher = teacherService.get(course.getTeacher().getId());
            courseToUpdate.setTeacher(teacher);
        }
        if (course.getStudents() != null) {
            var students = course.getStudents();
            if(!students.isEmpty()) {
                students = students.stream()
                        .map(student -> studentService.get(student.getId()))
                        .toList();
                courseToUpdate.setStudents(students);
            }
        }
        if (course.getSchoolYear() != 0) {
            courseToUpdate.setSchoolYear(course.getSchoolYear());
        }
        courseToUpdate.setCurrent(course.isCurrent());
        return courseRepository.save(courseToUpdate);
    }

    public Course updateTeacher(Long id, Teacher teacher) {
        var course = get(id);
        if (course == null) {
            return null;
        }
        if(course.getStudents() == null){
            course.setStudents(new ArrayList<>());
        }
        if (teacher == null) {
            course.setTeacher(null);
        }
        else if(teacherService.get(teacher.getId()) == null){
            return null;
        }
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    public Course addStudent(Long id, Long studentId) {
        var course = get(id);
        if (course == null) {
            return null;
        }
        var student = studentService.get(studentId);
        if (student == null) {
            return null;
        }
        if(course.getStudents().stream().anyMatch(s -> s.getId().equals(studentId))){
            return course;
        }
        course.getStudents().add(student);
        return courseRepository.save(course);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    public void removeTeacher(Long id) {
        var course = get(id);
        if (course == null) {
            return;
        }
        course.setTeacher(null);
        courseRepository.save(course);
    }

    public void removeStudent(Long id, Long studentId) {
        var course = get(id);
        if (course == null) {
            return;
        }
        var students = course.getStudents();
        var student = studentService.get(studentId);
        if (!students.contains(student)) {
            return;
        }
        course.getStudents().remove(student);
        courseRepository.save(course);
    }

}
