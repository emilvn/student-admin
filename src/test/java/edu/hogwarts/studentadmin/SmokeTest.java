package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.controller.CourseController;
import edu.hogwarts.studentadmin.controller.StudentController;
import edu.hogwarts.studentadmin.controller.TeacherController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SmokeTest {
    private CourseController courseController;
    private StudentController studentController;
    private TeacherController teacherController;

    public SmokeTest(CourseController courseController, StudentController studentController, TeacherController teacherController) {
        this.courseController = courseController;
        this.studentController = studentController;
        this.teacherController = teacherController;
    }

    @Test
    void contextLoads() throws Exception{
        assertThat(courseController).isNotNull();
        assertThat(studentController).isNotNull();
        assertThat(teacherController).isNotNull();
    }

}
