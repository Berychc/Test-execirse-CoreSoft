package com.example.coresoft.userandrole.service;

import com.example.coresoft.userandrole.model.UserRole;
import com.example.coresoft.userandrole.repository.UserRoleRepository;
import com.example.coresoft.users.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы ролей над пользователем в системе.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRoleRepository repository;

    /**
     * Метод получения пользователя по id.
     *
     * @param userId
     * @return userRole
     * @throws RuntimeException Нету пользователя
     * @throws RuntimeException Ошибка при получении пользователя
     */
    public List<UserRole> getUserRole(Long userId) {
        log.info("Вызван метод: getUserRole");
        try {
            List<UserRole> userRole = repository.findByUserId(userId);
            if (userRole.isEmpty()) {
                throw new RuntimeException(("Нету пользователей c " + userId));
            }
            return userRole;
        } catch (Exception e) {
            throw new RuntimeException("Нету пользователя с " + userId);
        }
    }


    /**
     * Метод назначения роли пользователю
     *
     * @param userId
     * @param roleId
     * @return userRole
     * @throws RuntimeException роль уже назначена
     * @throws RuntimeException пользователь не найден
     */
    @Transactional
    public UserRole appointRoleToUser(Long userId, Long roleId) {
        log.info("Вызван метод: appointRoleToUser");
        if (repository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new RuntimeException("Роль уже назначена пользователю");
        }
        try {
            UserRole userRole = new UserRole(null, userId, roleId);
            return repository.save(userRole);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Пользователь с %s и ролью %s , не найдены", userId, roleId));
        }
    }

    /**
     * Метод удаления роли у пользователя
     *
     * @param userId
     * @param roleId
     * @throws RuntimeException роль уже назначена
     * @throws RuntimeException пользователь не найден
     */
    @Transactional
    public void deleteRoleFromUser(Long userId, Long roleId) {
        log.info("Вызван метод: deleteRoleFromUser");
        if (!repository.existsByUserIdAndRoleId(userId, roleId)) {
            throw new RuntimeException("Роль уже назначена пользователю");
        }
        try {
            repository.deleteByUserIdAndRoleId(userId, roleId);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Пользователь с %s и ролью %s , не найдены", userId, roleId), e);
        }
    }
}
