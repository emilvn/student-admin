package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Student;
import edu.hogwarts.studentadmin.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    void getAllStudentsTest() throws Exception {
        // Test with no students
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(status().isNoContent());

        // Add mock student
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findAll()).thenReturn(List.of(student));

        // Test with student
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(status().isOk());

    }

    @Test
    void getStudentTest() throws Exception {
        // Add mock student
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Test with valid student id
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(status().isOk());

        // Test with invalid student id
        mockMvc.perform(MockMvcRequestBuilders.get("/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudentTest() throws Exception {
        // Add mock student
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.save(student)).thenReturn(student);

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
        // Add mock student
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

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
        // Add mock student
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Test with valid student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andExpect(status().isOk());

        // Test with invalid student id
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/2"))
                .andExpect(status().isNotFound());
    }
}
