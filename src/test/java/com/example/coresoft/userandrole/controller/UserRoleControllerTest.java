package com.example.coresoft.userandrole.controller;
import com.example.coresoft.userandrole.model.UserRole;
import com.example.coresoft.userandrole.service.UserRoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRoleController.class)
public class UserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRoleService service;

    @InjectMocks
    private UserRoleController controller;

    @Test
    void getUserRole_ReturnsUserRoles() throws Exception {
        UserRole userRole = new UserRole(ID1, ID2, ID2);
        when(service.getUserRole(anyLong())).thenReturn(List.of(userRole));

        mockMvc.perform(get("/api/users/{userId}/roles", ID1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(ID1))
                .andExpect(jsonPath("$[0].roleId").value(ID2))
                .andExpect(jsonPath("$[0].roleName").value(NAME1));
    }

    @Test
    void appointRoleToUser_ReturnsCreatedUserRole() throws Exception {
        UserRole newUserRole = new UserRole(ID1, ID1, ID2);
        when(service.appointRoleToUser(anyLong(), anyLong())).thenReturn(newUserRole);

        mockMvc.perform(post("/api/users/{userId}/roles/{roleId}", ID1, ID2))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(ID1))
                .andExpect(jsonPath("$.roleId").value(ID2))
                .andExpect(jsonPath("$.roleName").value(NAME1));
    }

    @Test
    void deleteRoleFromUser_Success() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}/roles/{roleId}", ID1, ID1))
                .andExpect(status().isOk())
                .andExpect(content().string("Удалена роль у пользователя"));

        verify(service, times(1)).deleteRoleFromUser(ID1, ID1);
    }
}
