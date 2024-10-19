package com.example.coresoft.users.controller;

import com.example.coresoft.users.model.User;
import com.example.coresoft.users.repository.UserRepository;
import com.example.coresoft.users.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository repository;

    @SpyBean
    private UserService service;

    @InjectMocks
    private UserController controller;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUserTest() throws Exception {
        User user = new User();
    }

    @Test
    @DisplayName("Получение пользователя по ID")
    void getUserById() throws Exception {
        User user = new User();
        user.setId(ID1);
        user.setUsername(USERNAME1);
        user.setEmail(EMAIL1);
        user.setPassword(PASSWORD1);

        when(service.getUserById(ID1)).thenReturn(user);

        mvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(USERNAME1));

        verify(service, times(1)).getUserById(ID1);
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUser() throws Exception {
        User newUser = new User();
        newUser.setId(ID1);
        newUser.setUsername(USERNAME1);
        newUser.setEmail(EMAIL1);
        newUser.setPassword(PASSWORD1);

        User createdUser = new User();
        createdUser.setId(ID2);
        createdUser.setUsername(USERNAME2);
        createdUser.setEmail(EMAIL2);
        createdUser.setPassword(PASSWORD2);

        when(service.createUser(any(User.class))).thenReturn(createdUser);

        mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(USERNAME1));

        verify(service, times(1)).createUser(any(User.class));
    }

    @Test
    @DisplayName("Обновление пользователя по ID")
    void updateUserById() throws Exception {
        User newUser = new User();
        newUser.setId(ID1);
        newUser.setUsername(USERNAME1);
        newUser.setEmail(EMAIL1);
        newUser.setPassword(PASSWORD1);

        when(service.updateUserById(eq(1L), any(User.class))).thenReturn(newUser);

        mvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME1));

        verify(service, times(1)).updateUserById(eq(ID1), any(User.class));
    }

    @Test
    @DisplayName("Удаление пользователя по ID")
    void deleteUserById() throws Exception {
        doNothing().when(service).deleteUserById(ID1);

        mvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удален с id: 1"));

        verify(service, times(1)).deleteUserById(ID1);
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(ID1);
        user1.setUsername(USERNAME1);
        user1.setEmail(EMAIL1);

        User user2 = new User();
        user2.setId(ID2);
        user2.setUsername(USERNAME2);
        user2.setEmail(EMAIL2);

        when(service.getAllUsers()).thenReturn(List.of(user1, user2));

        mvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value(USERNAME1))
                .andExpect(jsonPath("$[1].username").value(USERNAME2));

        verify(service, times(1)).getAllUsers();
    }
}
