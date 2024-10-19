package com.example.coresoft.users.sevice;

import com.example.coresoft.users.model.User;
import com.example.coresoft.users.repository.UserRepository;
import com.example.coresoft.users.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Класс JUnit Mockito тестирования user сервиса
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceJUnitTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(ID1);
        user.setUsername(USERNAME1);
        user.setEmail(EMAIL1);
        user.setPassword(PASSWORD1);
    }

    @Test
    @DisplayName("Тест проверка что сервис не пустой")
    void checkServiceTest() {
        Assertions.assertNotNull(service);
    }

    @Test
    @DisplayName("Тест получения всех пользователей")
    void getAllUsersTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(user));

        List<User> users = service.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Тест получения пользователя по id в хорошем случае")
    void getUserByIdTest() {
        Mockito.when(repository.findById(ID1)).thenReturn(Optional.of(user));

        User foundUserById = service.getUserById(ID1);

        assertEquals(user.getId(), foundUserById.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Тест получения пользователя по id в плохом случае")
    void getUserByIdUserNotFoundTest() {
        // Arrange
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.getUserById(ID1));
        assertEquals("Пользователь с 1 не найден!", exception.getMessage());
        verify(repository, times(1)).findById(ID1);
    }

    @Test
    @DisplayName("Тест создания пользователя в хорошем случае")
    void createUserTestTrueTest() {
        Mockito.when(repository.existsByUsername(USERNAME1)).thenReturn(false);
        Mockito.when(repository.existsByEmail(EMAIL1)).thenReturn(false);
        Mockito.when(repository.save(any(User.class))).thenReturn(user);

        User createdUser = service.createUser(user);

        assertNotNull(createdUser);
        assertEquals(USERNAME1, createdUser.getUsername());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Тест создания пользователя если имя занято")
    void createUserTestFalseTest() {
        Mockito.when(repository.existsByUsername(USERNAME1)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.createUser(user));
        assertEquals("Имя пользователя уже занято", exception.getMessage());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Тест обновления пользователя если он есть")
    void UpdateUserByIdUserFoundTest() {
        // Arrange
        Mockito.when(repository.findById(ID1)).thenReturn(Optional.of(user));
        Mockito.when(repository.save(any(User.class))).thenReturn(user);

        User updateUser = new User();
        updateUser.setUsername(USERNAME2);
        updateUser.setEmail(EMAIL2);

        User updatedUser = service.updateUserById(ID1, updateUser);

        assertEquals(USERNAME2, updatedUser.getUsername());
        assertEquals(EMAIL2, updatedUser.getEmail());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Тест удаления пользователя если он есть")
    void DeleteUserByIdUserFoundTest() {
        lenient().when(repository.findById(ID1)).thenReturn(Optional.of(user));

        service.deleteUserById(ID1);

        verify(repository, times(1)).deleteById(ID1);
    }

    @Test
    @DisplayName("Тест удаления пользователя если он есть")
    void DeleteUserByIdUserNotFoundTest() {
        doThrow(new EntityNotFoundException()).when(repository).deleteById(ID1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.deleteUserById(ID1);
        });

        assertEquals("Пользователь с 1 не найден!", exception.getMessage());
    }
}
