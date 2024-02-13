package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    @BeforeEach
    void setUp() {
        // add mock course
        Course course = new Course();
        course.setId(1L);
        Course courseWithNoTeacher = new Course();
        courseWithNoTeacher.setId(3L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Student student = new Student();
        student.setId(1L);
        course.setTeacher(teacher);
        course.getStudents().add(student);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());
        when(courseRepository.findById(3L)).thenReturn(Optional.of(courseWithNoTeacher));
        when(courseRepository.findAll()).thenReturn(List.of(course, courseWithNoTeacher));

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.findById(2L)).thenReturn(Optional.empty());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());
    }

    @Test
    void getAllCoursesTest() throws Exception {
        // Test valid course list
        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(status().isOk());
    }

    @Test
    void getCourseTest() throws Exception {
        // Test existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTeacherTest() throws Exception {
        // Test course with no teacher
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/3/teacher"))
                .andExpect(status().isNoContent());

        // Test valid course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/teacher"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2/teacher"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsTest() throws Exception {
        // Test course with no students
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/3/students"))
                .andExpect(status().isNoContent());

        // Test valid course
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"firstName\": null, \"middleName\": null, \"lastName\": null, \"dateOfBirth\": null, \"prefect\": false, \"enrollmentYear\": 0, \"graduationYear\": 0, \"graduated\": false, \"house\": null}]"));

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/4/students"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourseTest() throws Exception {

        // Test valid course with valid teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":2022,\"current\":true,\"teacher\":{\"id\": 1},\"students\":[]}"))
                .andExpect(status().isOk());

        // Test valid course id with no teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":2022,\"current\":true,\"teacher\":null,\"students\":[]}"))
                .andExpect(status().isOk());


        // Test non-existing teacher id
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":2022,\"current\":true,\"teacher\":{\"id\": 2},\"students\":[]}"))
                .andExpect(status().isBadRequest());

        // Test empty request
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCourseTest() throws Exception {
        // Test valid course with valid teacher
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"New Subject\",\"schoolYear\":2023,\"current\":true,\"teacher\":{\"id\": 1},\"students\":[]}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"subject\":\"New Subject\",\"schoolYear\":2023,\"current\":true,\"teacher\":{\"id\":1},\"students\":[]}"));

        // Test non-existing teacher id
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"New Subject\",\"schoolYear\":2023,\"current\":true,\"teacher\":{\"id\": 2},\"students\":[]}"))
                .andExpect(status().isNotFound());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"New Subject\",\"schoolYear\":2023,\"current\":true,\"teacher\":{\"id\": 1},\"students\":[]}"))
                .andExpect(status().isNotFound());

        // Test empty request
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTeacherTest() throws Exception {
        // Test valid course with valid teacher
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());

        // Test valid course with non-existing teacher id
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/teacher")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addStudentTest() throws Exception {
        // Test valid course with valid student
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/students/1"))
                .andExpect(status().isOk());

        // Test valid course with non-existing student id
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/students/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCourseTest() throws Exception {
        // Test valid course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeTeacherTest() throws Exception {
        // Test valid course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/teacher"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/4/teacher"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeStudentTest() throws Exception {
        // Test valid student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/students/1"))
                .andExpect(status().isOk());

        // Test non-existing student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/students/2"))
                .andExpect(status().isNotFound());
    }
}
