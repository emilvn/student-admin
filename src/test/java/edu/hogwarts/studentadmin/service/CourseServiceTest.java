package edu.hogwarts.studentadmin.service;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        Teacher teacher = createTeacher(1L, "Teacher");
        Teacher teacher2 = createTeacher(2L, "Teacher2");
        Student student1 = createStudent(1L, "Student1");
        Student student2 = createStudent(2L, "Student2");
        Student student3 = createStudent(4L, "Student3");

        when(teacherService.get(1L)).thenReturn(teacher);
        when(teacherService.get(2L)).thenReturn(teacher2);
        when(teacherService.get(3L)).thenReturn(null);
        when(studentService.get(1L)).thenReturn(student1);
        when(studentService.get(2L)).thenReturn(student2);
        when(studentService.get(3L)).thenReturn(null);
        when(studentService.get(4L)).thenReturn(student3);
        when(studentService.getAll()).thenReturn(new ArrayList<>(List.of(student1, student2)));

        Course course = createCourse(1L, teacher, List.of(student1, student2));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenReturn(course);
    }

    @Test
    void createCourseTest() {
        Teacher teacher = teacherService.get(1L);
        List<Student> students = studentService.getAll();
        Course course = createCourse(null, teacher, students);
        Course addedCourse = courseService.create(course);

        //Test that the course was added with the correct values
        assertEquals("Test", addedCourse.getSubject());
        assertEquals("Teacher", addedCourse.getTeacher().getFirstName());
        assertEquals(2, addedCourse.getStudents().size());
    }

    @Test
    void patchCourseTest() {
        Course course = createCourse();
        Course updatedCourse = courseService.patch(1L, course);

        //Test that only the subject was updated
        assertEquals("Updated", updatedCourse.getSubject());
        assertEquals("Teacher", updatedCourse.getTeacher().getFirstName());
        assertEquals(2, updatedCourse.getStudents().size());

        updatedCourse = courseService.patch(-1L, course);
        assertNull(updatedCourse);
    }

    @Test
    void updateCourseTest() {
        Course course = createCourse();
        Course updatedCourse = courseService.update(1L, course);

        //Test that all fields were updated
        assertEquals("Updated", updatedCourse.getSubject());
        assertNull(updatedCourse.getTeacher());
        assertEquals(0, updatedCourse.getStudents().size());

        updatedCourse = courseService.update(-1L, course);
        assertNull(updatedCourse);
    }

    @Test
    void removeTeacherTest() {
        courseService.removeTeacher(1L);
        Course updatedCourse = courseService.get(1L);
        // Test that teacher is removed
        assertNull(updatedCourse.getTeacher());
    }

    @Test
    void addStudentToCourseTest() {
        Course course = courseService.addStudent(1L, 1L);
        // Test that student is not added as they are already in the course
        assertEquals(2, course.getStudents().size());

        Course updatedCourse = courseService.addStudent(1L, 4L);
        // Test that student is added to the course
        assertEquals(3, updatedCourse.getStudents().size());

        Course invalidCourse = courseService.addStudent(1L, 3L);
        // Test that method returns null for invalid student id
        assertNull(invalidCourse);
    }

    @Test
    void updateTeacherOfCourseTest() {
        Course course = courseService.updateTeacher(1L, teacherService.get(2L));

        // Test that teacher is updated
        assertEquals("Teacher2", course.getTeacher().getFirstName());


        Course invalidCourse = courseService.updateTeacher(2L, teacherService.get(2L));
        // Test that null is returned, invalid course
        assertNull(invalidCourse);
    }

    @Test
    void removeStudentFromCourseTest() {
        courseService.removeStudent(1L, 1L);
        Course course = courseService.get(1L);

        // Test student is removed from the course
        assertEquals(1, course.getStudents().size());

        courseService.removeStudent(1L, 3L);
        // Verify that nothing happens (no student removed)
        assertEquals(1, course.getStudents().size());
    }


    private Teacher createTeacher(Long id, String firstName) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFirstName(firstName);
        return teacher;
    }

    private Student createStudent(Long id, String firstName) {
        Student student = new Student();
        student.setId(id);
        student.setFirstName(firstName);
        return student;
    }

    private Course createCourse(Long id, Teacher teacher, List<Student> students) {
        Course course = new Course();
        course.setId(id);
        course.setSubject("Test");
        course.setTeacher(teacher);
        course.setStudents(new ArrayList<>(students));
        return course;
    }

    private Course createCourse(){
        Course course = new Course();
        course.setSubject("Updated");
        return course;
    }
}
