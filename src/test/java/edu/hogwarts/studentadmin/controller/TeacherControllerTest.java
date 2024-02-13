package edu.hogwarts.studentadmin.controller;

import edu.hogwarts.studentadmin.model.Teacher;
import edu.hogwarts.studentadmin.service.TeacherService;
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
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        Teacher teacher = createTeacher(1L);
        when(teacherService.getAll()).thenReturn(List.of(teacher));
        when(teacherService.get(1L)).thenReturn(teacher);
        when(teacherService.get(2L)).thenReturn(null);
        when(teacherService.create(any(Teacher.class))).thenReturn(teacher);
        when(teacherService.update(any(Teacher.class), eq(1L))).thenReturn(teacher);
        when(teacherService.update(any(Teacher.class), eq(2L))).thenReturn(null);
    }

    @Test
    void getAllTeachersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void getTeacherTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/teachers/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTeacherTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .contentType("application/json")
                        .content("{\"firstName\": \"Emil\", \"middleName\": \"V\", \"lastName\": \"Nielsen\", \"dateOfBirth\": \"1935-10-04\", \"house\": {\"id\": 1}, \"headOfHouse\": true, \"employment\": \"TENURED\", \"employmentStart\": \"1970-09-01\", \"employmentEnd\": null}"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTeacherTest() throws Exception {
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
    void deleteTeacherTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/1"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/2"))
                .andExpect(status().isNotFound());
    }

    private Teacher createTeacher(Long id) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        return teacher;
    }
}
