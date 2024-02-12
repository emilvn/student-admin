package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.hogwarts.studentadmin.model.Course;
import edu.hogwarts.studentadmin.repository.CourseRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    void getAllCoursesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(status().isNoContent());

        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findAll()).thenReturn(List.of(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourseTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTeacherTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/teacher"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2/teacher"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/students"))
                .andExpect(status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2/students"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourseTest() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":\"2022\",\"current\":true,\"teacher\":{\"id\": 1},\"students\":[]}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":\"2022\",\"current\":true,\"teacher\":{\"id\": 2},\"students\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCourseTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"New Subject\",\"schoolYear\":\"2023\",\"current\":true,\"teacher\":{\"id\": 2},\"students\":[]}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"New Subject\",\"schoolYear\":\"2023\",\"current\":true,\"teacher\":{\"id\": 2},\"students\":[]}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTeacherTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addStudentTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCourseTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeTeacherTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/teacher"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/2/teacher"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeStudentTest() throws Exception {
        Course course = new Course();
        course.setId(1L);
        Student student = new Student();
        student.setId(1L);
        course.getStudents().add(student);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/students/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/students/2"))
                .andExpect(status().isNotFound());
    }
}
