package com.example.coresoft.role.controller;

import com.example.coresoft.constants.ConstantsTest;
import com.example.coresoft.roles.controller.RoleController;
import com.example.coresoft.roles.model.Role;
import com.example.coresoft.roles.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RoleController.class)
public class RoleControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RoleService service;

    @InjectMocks
    private RoleController controller;

    @Test
    void getAllRoles_ReturnsRoles() throws Exception {
        when(service.getAllRoles()).thenReturn(List.of(new Role(NAME1, DESCRIPTION1, null)));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(ID1))
                .andExpect(jsonPath("$[0].name").value(ConstantsTest.NAME1));
    }

    @Test
    void getRoleById_ReturnsRole() throws Exception {
        when(service.getRoleById(anyLong())).thenReturn(new Role(NAME1, DESCRIPTION1, null));

        mockMvc.perform(get("/api/roles/{id}", ID1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID1))
                .andExpect(jsonPath("$.name").value(ConstantsTest.NAME1));
    }

    @Test
    void createRole_ReturnsCreatedRole() throws Exception {
        Role newRole = new Role(NAME1, DESCRIPTION1, null);
        when(service.createRole(any(Role.class))).thenReturn(new Role(NAME1, DESCRIPTION1, null));

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + ConstantsTest.NAME1 + "\",\"description\":\"" + ConstantsTest.DESCRIPTION1 + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID1))
                .andExpect(jsonPath("$.name").value(ConstantsTest.NAME1));
    }

    @Test
    void updateRoleById_ReturnsUpdatedRole() throws Exception {
        Role updatedRole = new Role(NAME1, DESCRIPTION1, null);
        when(service.updateRoleById(anyLong(), any(Role.class))).thenReturn(updatedRole);

        mockMvc.perform(put("/api/roles/{id}", ID1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + ConstantsTest.NAME1 + "\",\"description\":\"" + ConstantsTest.DESCRIPTION1 + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ID1))
                .andExpect(jsonPath("$.name").value(ConstantsTest.NAME1));
    }

    @Test
    void deleteRoleById_Success() throws Exception {
        mockMvc.perform(delete("/api/roles/{id}", ID1))
                .andExpect(status().isOk())
                .andExpect(content().string("Роль успешно удалена с id: " + ID1));

        verify(service, times(1)).deleteRoleById(ID1);
    }
}