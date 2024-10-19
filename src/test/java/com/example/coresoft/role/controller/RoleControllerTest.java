package com.example.coresoft.role.controller;

import com.example.coresoft.roles.controller.RoleController;
import com.example.coresoft.roles.model.Role;
import com.example.coresoft.roles.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RoleControllerTest {

    @Mock
    private RoleService service;

    @InjectMocks
    private RoleController controller;

    @Test
    @DisplayName("Тест проверка, что контроллер не пустой")
    void checkControllerTest() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testGetAllRoles() {
        Role role1 = new Role();
        role1.setId(ID1);
        role1.setName(NAME1);
        role1.setDescription(DESCRIPTION1);
        Role role2 = new Role();
        role2.setId(ID2);
        role2.setName(NAME2);
        role2.setDescription(DESCRIPTION2);

        when(service.getAllRoles()).thenReturn(Arrays.asList(role1, role2));

        ResponseEntity<?> response = controller.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Arrays.asList(role1, role2), response.getBody());
    }

    @Test
    public void testGetRoleById() {
        Role role1 = new Role();
        role1.setId(ID1);
        role1.setName(NAME1);
        role1.setDescription(DESCRIPTION1);

        when(service.getRoleById(1L)).thenReturn(role1);

        ResponseEntity<?> response = controller.getRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role1, response.getBody());
    }

    @Test
    public void testCreateRole() {
        Role role1 = new Role();
        role1.setId(ID1);
        role1.setName(NAME1);
        role1.setDescription(DESCRIPTION1);
        Role role2 = new Role();
        role2.setId(ID2);
        role2.setName(NAME2);
        role2.setDescription(DESCRIPTION2);

        when(service.createRole(role1)).thenReturn(role2);

        ResponseEntity<?> response = controller.createRole(role1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(role2, response.getBody());
    }

    @Test
    public void testUpdateRoleById() {
        Role role1 = new Role();
        role1.setId(ID1);
        role1.setName(NAME1);
        role1.setDescription(DESCRIPTION1);

        when(service.updateRoleById(1L, role1)).thenReturn(role1);

        ResponseEntity<?> response = controller.updateRoleById(1L, role1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(role1, response.getBody());
    }

    @Test
    public void testDeleteRoleById() {
        ResponseEntity<?> response = controller.deleteRoleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Роль успешно удалена с id: 1", response.getBody());

        verify(service, times(1)).deleteRoleById(1L);
    }

    @Test
    public void testGetAllRolesNotFound() {
        when(service.getAllRoles()).thenThrow(new RuntimeException("Роли не найдены"));

        ResponseEntity<?> response = controller.getAllRoles();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ошибка при получении ролей: Роли не найдены", response.getBody());
    }

    @Test
    public void testCreateRoleException() {
        Role role1 = new Role();
        role1.setId(ID1);
        role1.setName(NAME1);
        role1.setDescription(DESCRIPTION1);

        when(service.createRole(role1)).thenThrow(new RuntimeException("Ошибка при создании роли"));

        ResponseEntity<?> response = controller.createRole(role1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ошибка при создании роли: Ошибка при создании роли", response.getBody());
    }
}
