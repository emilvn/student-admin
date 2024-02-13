package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        // add mock student
        Student student = new Student();
        student.setId(1L);

        when(studentService.getAll()).thenReturn(List.of(student));
        when(studentService.get(1L)).thenReturn(student);
        when(studentService.get(2L)).thenReturn(null);
        when(studentService.create(any(Student.class))).thenReturn(student);
        when(studentService.update(any(Student.class), eq(1L))).thenReturn(student);
        when(studentService.update(any(Student.class), eq(2L))).thenReturn(null);
    }

    @Test
    void getAllStudentsTest() throws Exception {
        // Test with student
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void getStudentTest() throws Exception {
        // Test with valid student id
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(status().isOk());

        // Test with invalid student id
        mockMvc.perform(MockMvcRequestBuilders.get("/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudentTest() throws Exception {
        // Test with valid student
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType("application/json")
                .content("{\"firstName\": \"Emil\", \"middleName\": \"V\", \"lastName\": \"Nielsen\", \"dateOfBirth\": \"1935-10-04\", \"house\": {\"id\": 1}, \"prefect\": true, \"enrollmentYear\": 1950, \"graduationYear\": 1957, \"graduated\": true}"))
                .andExpect(status().isOk());

        // Test with invalid student
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType("application/json")
                .content("{\"id\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStudentTest() throws Exception {
        // Test with valid student id
        mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                .contentType("application/json")
                .content("{\"id\":1, \"firstName\": \"Test\"}"))
                .andExpect(status().isOk());

        // Test with invalid student id
        mockMvc.perform(MockMvcRequestBuilders.put("/students/2")
                .contentType("application/json")
                .content("{\"id\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentTest() throws Exception {
        // Test with valid student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andExpect(status().isOk());

        // Test with invalid student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/2"))
                .andExpect(status().isNotFound());
    }
}
