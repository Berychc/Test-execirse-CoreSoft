package com.example.coresoft.users.controller;

import com.example.coresoft.users.model.User;
import com.example.coresoft.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с пользователем в системе.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователя", description = " Контроллер для работы с пользователями")
public class UserController {

    private final UserService service;

    @Operation(summary = "Получение всех пользователей", description = "Пользователи должны существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительные пользователи"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")})
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(service.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении пользователей: " + e.getMessage());
        }
    }

    @Operation(summary = "Получение пользователя по id", description = "Пользователи должны существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительные пользователи"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании")})
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не найден: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении пользователя: " + e.getMessage());
        }
    }

    @Operation(summary = "Создание нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании")})
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = service.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании пользователя: " + e.getMessage());
        }
    }

    @Operation(summary = "Обновление пользователя по id", description = "Пользователь должен существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительный пользователь"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(service.updateUserById(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь не найден: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении пользователя: " + e.getMessage());
        }
    }

    @Operation(summary = "Удаление пользователя по id", description = "Пользователь должен существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительный пользователь"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            service.deleteUserById(id);
            return ResponseEntity.ok("Пользователь успешно удален с id: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден с id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении пользователя: " + e.getMessage());
        }
    }
}
