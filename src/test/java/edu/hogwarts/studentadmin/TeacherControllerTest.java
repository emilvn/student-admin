package edu.hogwarts.studentadmin;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.repository.TeacherRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    void getAllTeachersTest() throws Exception {
        // Test with no teachers
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(status().isNoContent());

        // Add mock teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        // Test with teacher
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(status().isOk());

    }

    @Test
    void getTeacherTest() throws Exception {
        // Add mock teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Test with valid teacher id
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1"))
                .andExpect(status().isOk());

        // Test with invalid teacher id
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTeacherTest() throws Exception {
        // Test with valid teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .contentType("application/json")
                        .content("{\"firstName\": \"Emil\", \"middleName\": \"V\", \"lastName\": \"Nielsen\", \"dateOfBirth\": \"1935-10-04\", \"house\": {\"id\": 1}, \"headOfHouse\": true, \"employment\": \"TENURED\", \"employmentStart\": \"1970-09-01\", \"employmentEnd\": null}"))
                .andExpect(status().isOk());

        // Test with invalid teacher
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateTeacherTest() throws Exception {
        // Add mock teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        // Test with valid teacher id
        mockMvc.perform(MockMvcRequestBuilders.put("/teachers/1")
                        .contentType("application/json")
                        .content("{\"id\":1}"))
                .andExpect(status().isOk());

        // Test with invalid teacher id
        mockMvc.perform(MockMvcRequestBuilders.put("/teachers/2")
                        .contentType("application/json")
                        .content("{\"id\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTeacherTest() throws Exception {
        // Add mock teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Test with valid teacher id
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/1"))
                .andExpect(status().isOk());

        // Test with invalid teacher id
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/2"))
                .andExpect(status().isNotFound());
    }
}
