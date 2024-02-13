package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Course;
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
        if (course.getSubject() != null) {
            courseToUpdate.setSubject(course.getSubject());
        }
        if (course.getTeacher() != null) {
            var teacher = teacherService.get(course.getTeacher().getId());
            courseToUpdate.setTeacher(teacher);
        }
        if (course.getStudents() != null) {
            var students = course.getStudents()
                    .stream()
                    .map(student -> studentService.get(student.getId()))
                    .toList();
            courseToUpdate.setStudents(new ArrayList<>(students));
        }
        if (course.getSchoolYear() != 0) {
            courseToUpdate.setSchoolYear(course.getSchoolYear());
        }
        courseToUpdate.setCurrent(course.isCurrent());
        return courseRepository.save(courseToUpdate);
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
