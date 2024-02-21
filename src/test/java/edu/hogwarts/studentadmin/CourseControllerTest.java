package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.dto.CourseDTO;
import edu.hogwarts.studentadmin.dto.StudentDTO;
import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.service.CourseService;
import edu.hogwarts.studentadmin.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private StudentService studentService;


    /**
     * Test that a student can be added to a course if the school year matches
     * And that the response status is 200
     * @throws Exception if the test fails
     */
    @Test
    public void testStudentSchoolYearMatching() throws Exception{
        var studentCanAdd = new StudentDTO();
        studentCanAdd.setSchoolYear(1);
        var studentCannotAdd = new StudentDTO();
        studentCannotAdd.setSchoolYear(2);

        var courseStart = new CourseDTO();
        courseStart.setId(1L);
        courseStart.setSchoolYear(1);
        courseStart.setStudents(new ArrayList<>());

        var courseEnd = new CourseDTO();
        courseEnd.setId(1L);
        courseEnd.setSchoolYear(1);
        courseEnd.setStudents(List.of(studentCanAdd));

        when(courseService.get(1L)).thenReturn(courseStart);
        when(courseService.addStudentsById(anyLong(), anyList())).thenReturn(courseEnd);

        when(studentService.get(1L)).thenReturn(studentCanAdd);

        mockMvc.perform(post("/courses/1/students")
                .content("[{\"id\": 1}]}")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    /**
     * Test that a student cannot be added to a course if the school year does not match
     * And that the response status is 400
     * @throws Exception if the test fails
     */
    @Test
    public void testStudentSchoolYearNotMatching() throws Exception{
        var studentCannotAdd = new StudentDTO();
        studentCannotAdd.setSchoolYear(2);

        var course = new CourseDTO();
        course.setId(1L);
        course.setSchoolYear(1);
        course.setStudents(new ArrayList<>());

        when(courseService.get(1L)).thenReturn(course);
        when(courseService.addStudentsById(anyLong(), anyList())).thenReturn(course);

        when(studentService.get(2L)).thenReturn(studentCannotAdd);

        mockMvc.perform(post("/courses/1/students")
                .content("[{\"id\": 2}]")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
