package madstodolist.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NavbarWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void barraMenu_se_muestra_en_paginas_contenido() throws Exception {
        mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ToDoList"))); // el texto que usa tu navbar
    }

    @Test
    public void barraMenu_no_se_muestra_en_login() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("ToDoList"))));
    }

    @Test
    public void barraMenu_no_se_muestra_en_registro() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("ToDoList"))));
    }
}
