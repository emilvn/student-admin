package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.service.CourseService;
import edu.hogwarts.studentadmin.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    public void testAddStudents() throws Exception{
        var studentCanAdd = new Student();
        studentCanAdd.setSchoolYear(1);
        var studentCannotAdd = new Student();
        studentCannotAdd.setSchoolYear(2);

        var courseStart = new Course();
        courseStart.setId(1L);
        courseStart.setSchoolYear(1);
        courseStart.setStudents(new ArrayList<>());

        var courseEnd = new Course();
        courseEnd.setId(1L);
        courseEnd.setSchoolYear(1);
        courseEnd.setStudents(List.of(studentCanAdd));

        when(courseService.get(1L)).thenReturn(courseStart);
        when(courseService.addStudentsById(anyLong(), anyList())).thenReturn(courseEnd);

        when(studentService.get(1L)).thenReturn(studentCanAdd);
        when(studentService.get(2L)).thenReturn(studentCannotAdd);

        mockMvc.perform(post("/courses/1/students")
                .content("[{\"id\": 1}]}")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/courses/1/students")
                .content("[{\"id\": 2}]")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
