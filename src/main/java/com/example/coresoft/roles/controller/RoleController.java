package com.example.coresoft.roles.controller;

import com.example.coresoft.roles.service.RoleService;
import com.example.coresoft.roles.model.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для работы с ролями в системе.
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Роли", description = " Контроллер для работы с ролями")
public class RoleController {

    private final RoleService service;

    @Operation(summary = "Получение всех ролей", description = "Роли должны существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлен недействительные роли"),
            @ApiResponse(responseCode = "404", description = "Роли не найдены")})
    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        try {
            return ResponseEntity.ok(service.getAllRoles());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении ролей: " + e.getMessage());
        }
    }

    @Operation(summary = "Получение роли по id", description = "Роли должны существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлена недействительная роль"),
            @ApiResponse(responseCode = "404", description = "Роль не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании роли")})
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getRoleById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Роль не найдена: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении роли: " + e.getMessage());
        }
    }

    @Operation(summary = "Создание нового роли")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Роль создана"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании роли")})
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role user) {
        try {
            Role createdRole = service.createRole(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при создании роли: " + e.getMessage());
        }
    }

    @Operation(summary = "Обновление роли по id", description = "Роль должен существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлена недействительная роль"),
            @ApiResponse(responseCode = "404", description = "Роль не найдена")})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoleById(@PathVariable Long id, @RequestBody Role user) {
        try {
            return ResponseEntity.ok(service.updateRoleById(id, user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Роль не найдена: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при получении роли: " + e.getMessage());
        }
    }

    @Operation(summary = "Удаление роли по id", description = "Роль должна существовать")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Предоставлена недействительная роль"),
            @ApiResponse(responseCode = "404", description = "Роль не найдена")})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable Long id) {
        try {
            service.deleteRoleById(id);
            return ResponseEntity.ok("Роль успешно удалена с id: " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Роль не найдена с id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при удалении роли: " + e.getMessage());
        }
    }
}
