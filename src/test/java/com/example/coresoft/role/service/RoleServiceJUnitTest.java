package com.example.coresoft.role.service;

import com.example.coresoft.roles.model.Role;
import com.example.coresoft.roles.repository.RoleRepository;
import com.example.coresoft.roles.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Класс JUnit Mockito тестирования role сервиса
 */
@ExtendWith(MockitoExtension.class)
public class RoleServiceJUnitTest {

    @Mock
    private RoleRepository repository;

    @InjectMocks
    private RoleService service;

    private Role role;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(ID1);
        role.setName(NAME1);
        role.setDescription(DESCRIPTION1);
    }

    @Test
    @DisplayName("Тест проверка что сервис не пустой")
    void checkServiceTest() {
        Assertions.assertNotNull(service);
    }

    @Test
    @DisplayName("Тест получения всех ролей")
    void getAllRolesTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(role));

        List<Role> roles = service.getAllRoles();

        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Тест получения роли по id в хорошем случае")
    void getRoleByIdTest() {
        Mockito.when(repository.findById(ID1)).thenReturn(Optional.of(role));

        Role foundRoleById = service.getRoleById(ID1);

        assertEquals(role.getId(), foundRoleById.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Тест получения роли по id в плохом случае")
    void getRoleByIdRoleNotFoundTest() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getRoleById(ID1));
        assertEquals("Роль с 1 не найден!", exception.getMessage());
        verify(repository, times(1)).findById(ID1);
    }

    @Test
    @DisplayName("Тест создания роли в хорошем случае")
    void createRoleTestTrueTest() {
        Mockito.when(repository.existsByName(NAME1)).thenReturn(false);
        Mockito.when(repository.save(any(Role.class))).thenReturn(role);

        Role createdRole = service.createRole(role);

        assertNotNull(createdRole);
        assertEquals(NAME1, createdRole.getName());
        verify(repository, times(1)).save(any(Role.class));
    }

    @Test
    @DisplayName("Тест создания роли если имя занято")
    void createRoleTestFalseTest() {
        Mockito.when(repository.existsByName(NAME1)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createRole(role));
        assertEquals("Имя роли уже занято", exception.getMessage());
        verify(repository, never()).save(any(Role.class));
    }

    @Test
    @DisplayName("Тест обновления роли если она есть")
    void UpdateRoleByIdRoleFoundTest() {
        // Arrange
        Mockito.when(repository.findById(ID1)).thenReturn(Optional.of(role));
        Mockito.when(repository.save(any(Role.class))).thenReturn(role);

        Role updateRole = new Role();
        updateRole.setName(NAME2);
        updateRole.setDescription(DESCRIPTION2);

        Role updatedRole = service.updateRoleById(ID1, updateRole);

        assertEquals(NAME2, updatedRole.getName());
        assertEquals(DESCRIPTION2, updatedRole.getDescription());
        verify(repository, times(1)).save(any(Role.class));
    }

    @Test
    @DisplayName("Тест удаления роли если она есть")
    void DeleteRoleByIdRoleFoundTest() {
        lenient().when(repository.findById(ID1)).thenReturn(Optional.of(role));

        service.deleteRoleById(ID1);

        verify(repository, times(1)).deleteById(ID1);
    }

    @Test
    @DisplayName("Тест удаления роли ее его нету")
    void DeleteRoleByIdRoleNotFoundTest() {
        doThrow(new EntityNotFoundException()).when(repository).deleteById(ID1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.deleteRoleById(ID1);
        });

        assertEquals("Роль с 1 не найдена!", exception.getMessage());
    }
}
