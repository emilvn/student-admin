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

    @Test
    void getAllCoursesTest() throws Exception {
        // Test empty course list
        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(status().isNoContent());

        // add mock course
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findAll()).thenReturn(List.of(course));

        // Test valid course list
        mockMvc.perform(MockMvcRequestBuilders.get("/courses"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"subject\": null, \"schoolYear\": 0, \"current\": false, \"teacher\": null, \"students\": []}]"));
    }

    @Test
    void getCourseTest() throws Exception {
        // add mock course
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"subject\": null, \"schoolYear\": 0, \"current\": false, \"teacher\": null, \"students\": []}"));

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTeacherTest() throws Exception {
        // Test course with no teacher
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/teacher"))
                .andExpect(status().isNotFound());

        // add mock course with teacher
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test valid course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/teacher"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2/teacher"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsTest() throws Exception {
        // add mock course without students
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test course with no students
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/1/students"))
                .andExpect(status().isNoContent());

        // add mock course with students
        Course courseWithStudents = new Course();
        courseWithStudents.setId(2L);
        var students = courseWithStudents.getStudents();
        var student = new Student();
        student.setId(1L);
        students.add(student);
        when(courseRepository.findById(2L)).thenReturn(Optional.of(courseWithStudents));

        // Test valid course
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/2/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"firstName\": null, \"middleName\": null, \"lastName\": null, \"dateOfBirth\": null, \"prefect\": false, \"enrollmentYear\": 0, \"graduationYear\": 0, \"graduated\": false, \"house\": null}]"));

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/3/students"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourseTest() throws Exception {
        // add mock teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Test valid course with valid teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":2022,\"current\":true,\"teacher\":{\"id\": 1},\"students\":[]}"))
                .andExpect(status().isOk());

        // Test valid course id with no teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"subject\":\"Subject\",\"schoolYear\":2022,\"current\":true,\"teacher\":null,\"students\":[]}"))
                .andExpect(status().isBadRequest());


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
        // add mock teacher and course
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

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
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/3")
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
        // add mock teacher and course
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

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
        // add mock student and course
        Course course = new Course();
        course.setId(1L);
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test valid course with valid student
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/students/1"))
                .andExpect(status().isOk());

        // Test valid course with non-existing student id
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/1/students/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCourseTest() throws Exception {
        // add mock course
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test valid course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeTeacherTest() throws Exception {
        // add mock teacher and course
        Course course = new Course();
        course.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test valid course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/teacher"))
                .andExpect(status().isOk());

        // Test non-existing course id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/2/teacher"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeStudentTest() throws Exception {
        // add mock student and course
        Course course = new Course();
        course.setId(1L);
        Student student = new Student();
        student.setId(1L);
        course.getStudents().add(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Test valid student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/students/1"))
                .andExpect(status().isOk());

        // Test non-existing student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/1/students/2"))
                .andExpect(status().isNotFound());
    }
}
