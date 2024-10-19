package com.example.coresoft.userandrole.controller;

import com.example.coresoft.userandrole.model.UserRole;
import com.example.coresoft.userandrole.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для работы с пользователем в системе.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Роли пользователей", description = " Контроллер для работы ролей над пользователя")
public class UserRoleController {

    private final UserRoleService service;

    @Operation(summary = "Получение списка ролей пользователя", description = "Пользователи и роли должны существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительные пользователи"),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")})
    @GetMapping("/{userId}/roles")
    public ResponseEntity<?> getUserRole(@PathVariable Long userId) {
        try {
            List<UserRole> roles = service.getUserRole(userId);
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении списка ролей: " + e.getMessage());
        }
    }

    @Operation(summary = "Назначение роли пользователю", description = "Пользователь и роль должны существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Роль к пользователю примвоена"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании")})
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> appointRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            UserRole userRole = service.appointRoleToUser(userId, roleId);
            return ResponseEntity.status(HttpStatus.CREATED).body(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при присваивании роли к пользователю: " + e.getMessage());
        }
    }

    @Operation(summary = "Удаление пользователя по id", description = "Пользователь должен существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительный пользователь"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")})
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<?> deleteRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            service.deleteRoleFromUser(userId, roleId);
            return ResponseEntity.ok("Удалена роль у пользователя");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь && Роль не найдена");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении роли: " + e.getMessage());
        }
    }
}
