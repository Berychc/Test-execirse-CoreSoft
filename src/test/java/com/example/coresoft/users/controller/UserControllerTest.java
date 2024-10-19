package com.example.coresoft.users.controller;

import com.example.coresoft.users.model.User;
import com.example.coresoft.users.repository.UserRepository;
import com.example.coresoft.users.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.coresoft.constants.ConstantsTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

/**
 * Класс тестирования user контроллера
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private UserController controller;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService service;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Тест проверка, что контроллер не пустой")
    void checkControllerTest() {
        assertThat(controller).isNotNull();
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void getAllUsers() {
        ResponseEntity<List> response = restTemplate.getForEntity("/api/users", List.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void getUserById() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/users/1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);

        User user = mapper.readValue(response.getBody(), User.class);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(USERNAME1);
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUser() throws Exception {
        User user1 = new User();
        user1.setId(ID1);
        user1.setUsername(USERNAME1);
        user1.setEmail(EMAIL1);
        user1.setPassword(PASSWORD1);

        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", user1, User.class);

        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(USERNAME1);
    }

    @Test
    @DisplayName("Обновление пользователя по id")
    void updateUserById() {
        User user1 = new User();
        user1.setId(ID1);
        user1.setUsername(USERNAME1);
        user1.setEmail(EMAIL1);
        user1.setPassword(PASSWORD1);

        restTemplate.put("/api/users/1", user1);

        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/1", User.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getUsername()).isEqualTo(USERNAME1);
    }

    @Test
    @DisplayName("Удаление пользователя по id")
    void deleteUserById() {
        restTemplate.delete("/api/users/1");

        ResponseEntity<Object> response = restTemplate.getForEntity("/api/users/1", Object.class);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
