package com.example.coresoft.userandrole.service;
import com.example.coresoft.userandrole.model.UserRole;
import com.example.coresoft.userandrole.repository.UserRoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Класс JUnit Mockito тестирования userRole сервиса
 */
@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTest {

    @Mock
    private UserRoleRepository repository;

    @InjectMocks
    private UserRoleService service;

    @Test
    @DisplayName("Тест получения роли пользователя")
    void getUserRoleUserFoundTest() {
        UserRole userRole = new UserRole(null, ID1, ID2);
        when(repository.findByUserId(ID1)).thenReturn(Collections.singletonList(userRole));

        List<UserRole> userRoles = service.getUserRole(ID1);

        assertNotNull(userRoles);
        assertEquals(1, userRoles.size());
        assertEquals(ID1, userRoles.get(0).getUserId());
    }

    @Test
    void getUserRoleUserNotFoundTest() {
        when(repository.findByUserId(ID1)).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getUserRole(ID1);
        });

        assertEquals("Нету пользователя c " + ID1, exception.getMessage());
    }

    @Test
    void appointRoleToUserRoleAssignedTest() {
        when(repository.existsByUserIdAndRoleId(ID1, ID2)).thenReturn(false);
        when(repository.save(any(UserRole.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserRole userRole = service.appointRoleToUser(ID1, ID2);
        assertNotNull(userRole);
        assertEquals(ID1, userRole.getUserId());
        assertEquals(ID2, userRole.getRoleId());
    }

    @Test
    void appointRoleToUserRoleAlreadyAssignedTest() {
        when(repository.existsByUserIdAndRoleId(ID1, ID2)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.appointRoleToUser(ID1, ID2);
        });

        assertEquals("Роль уже назначена пользователю", exception.getMessage());
    }

    @Test
    void deleteRoleFromUserRoleDeletedTest() {
        when(repository.existsByUserIdAndRoleId(ID1, ID2)).thenReturn(true);

        service.deleteRoleFromUser(ID1, ID2);

        verify(repository).deleteByUserIdAndRoleId(ID1, ID2);
    }

    @Test
    void deleteRoleFromUserRoleNotFoundTest() {
        when(repository.existsByUserIdAndRoleId(ID1, ID2)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.deleteRoleFromUser(ID1, ID2);
        });

        assertEquals("Роль уже назначена пользователю", exception.getMessage());
    }
}
