package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides service methods to manage courses in the school.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    /**
     * Constructor for CourseService. Uses dependency injection to get the CourseRepository, TeacherService, and StudentService.
     * @param courseRepository The repository for courses
     * @param teacherService The service for teachers
     * @param studentService The service for students
     */
    public CourseService(CourseRepository courseRepository, TeacherService teacherService, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    /**
     * Gets list of all courses from the database
     * @return List of all courses
     */
    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    /**
     * Gets a specific course by its id
     * @param id The id of the course
     * @return The course with the given id, or null if it doesn't exist
     */
    public Course get(Long id) {
        return courseRepository.findById(id).orElse(null);
    }


    /**
     * Creates a new course.
     * If the teacher or students are not null, they are fetched from the database to ensure they exist.
     * @param course The course to create
     * @return The created course
     */
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

    /**
     * Updates a course overwriting all its fields with the new course data.
     * @param id The id of the course to update
     * @param course The new course data
     * @return The updated course, or null if the course doesn't exist
     */
    public Course update(Long id, Course course) {
        var courseToUpdate = get(id);
        if (courseToUpdate == null) {
            return null;
        }
        courseToUpdate.setSubject(course.getSubject());
        if (course.getTeacher() != null) {
            var teacher = teacherService.get(course.getTeacher().getId());
            courseToUpdate.setTeacher(teacher);
        } else {
            courseToUpdate.setTeacher(null);
        }
        if (course.getStudents() != null) {
            var students = course.getStudents()
                    .stream()
                    .map(student -> studentService.get(student.getId()))
                    .toList();
            courseToUpdate.setStudents(new ArrayList<>(students));
        } else {
            courseToUpdate.setStudents(new ArrayList<>());
        }
        courseToUpdate.setSchoolYear(course.getSchoolYear());
        courseToUpdate.setCurrent(course.isCurrent());
        return courseRepository.save(courseToUpdate);
    }

    /**
     * Updates a course overwriting only the fields that are not null in the new course data.
     * @param id The id of the course to update
     * @param course The new course data
     * @return The updated course, or null if the course doesn't exist
     */
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
            if (!students.isEmpty()) {
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

    /**
     * Updates the teacher of a course.
     * @param id The id of the course to update
     * @param teacher The new teacher
     * @return The updated course, or null if the course doesn't exist or the teacher doesn't exist
     */
    public Course updateTeacher(Long id, Teacher teacher) {
        var course = get(id);
        if (course == null) {
            return null;
        }
        if (course.getStudents() == null) {
            course.setStudents(new ArrayList<>());
        }
        if (teacher == null) {
            course.setTeacher(null);
        } else if (teacherService.get(teacher.getId()) == null) {
            return null;
        }
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }

    /**
     * Adds students to a course, finding them by their names.
     * @param id The id of the course to update
     * @param students The new students as a list of Student objects with at least their names set
     * @return The updated course, or null if the course doesn't exist
     */
    public Course addStudentsByName(Long id, List<Student> students) {
        var course = get(id);
        if (course == null) {
            return null;
        }
        if (course.getStudents() == null) {
            course.setStudents(new ArrayList<>());
        }
        var studentsToAdd = students.stream()
                .map(student -> studentService.get(student.getName()))
                .toList();
        for (var student : studentsToAdd) {
            if (!course.getStudents().contains(student)) {
                course.getStudents().add(student);
            }
        }
        return courseRepository.save(course);
    }

    /**
     * Adds students to a course, finding them by their ids.
     * @param id The id of the course to update
     * @param students The new students as a list of Student objects with at least their ids set
     * @return The updated course, or null if the course doesn't exist
     */
    public Course addStudentsById(Long id, List<Student> students) {
        var course = get(id);
        if (course == null) {
            return null;
        }
        if (course.getStudents() == null) {
            course.setStudents(new ArrayList<>());
        }
        var studentsToAdd = students.stream()
                .map(student -> studentService.get(student.getId()))
                .toList();
        for (var student : studentsToAdd) {
            if (!course.getStudents().contains(student)) {
                course.getStudents().add(student);
            }
        }
        return courseRepository.save(course);
    }

    /**
     * Deletes a course by its id
     * @param id The id of the course to delete
     */
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

    /**
     * Removes the teacher from a course, setting it to null.
     * @param id The id of the course to update
     */
    public void removeTeacher(Long id) {
        var course = get(id);
        if (course == null) {
            return;
        }
        course.setTeacher(null);
        courseRepository.save(course);
    }

    /**
     * Removes a specific student from a course.
     * @param id The id of the course to update
     * @param studentId The id of the student to remove
     */
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
