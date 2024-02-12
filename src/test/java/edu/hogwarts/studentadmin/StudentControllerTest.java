package edu.hogwarts.studentadmin;

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
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(status().isNoContent());

        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findAll()).thenReturn(List.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(status().isOk());

    }

    @Test
    void getStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.save(student)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType("application/json")
                .content("{\"id\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders.put("/students/1")
                .contentType("application/json")
                .content("{\"id\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/students/2")
                .contentType("application/json")
                .content("{\"id\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/2"))
                .andExpect(status().isNotFound());
    }
}
