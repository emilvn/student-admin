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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    void getAllStudentsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(status().isNoContent());

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(status().isOk());

    }

    @Test
    void getStudentTest() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudentTest() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .contentType("application/json")
                        .content("{\"id\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStudentTest() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        mockMvc.perform(MockMvcRequestBuilders.put("/teachers/1")
                        .contentType("application/json")
                        .content("{\"id\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.put("/teachers/2")
                        .contentType("application/json")
                        .content("{\"id\":2}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteStudentTest() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/2"))
                .andExpect(status().isNotFound());
    }
}
