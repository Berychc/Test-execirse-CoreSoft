package com.example.coresoft.roles.service;

import com.example.coresoft.roles.repository.RoleRepository;
import com.example.coresoft.roles.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Сервис для работы с пользователем в системе.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository repository;

    /**
     * Метод получения всех роли.
     *
     * @return allRoles
     * @throws RuntimeException Нету роли
     * @throws RuntimeException Ошибка при получении роли
     */
    public List<Role> getAllRoles() {
        log.info("Вызван метод: getAllRoles");
        try {
            List<Role> allRoles = repository.findAll();
            if (allRoles.isEmpty()) {
                throw new RuntimeException("Нету роли для отображения");
            }
            return allRoles;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ошибка при получении роли: %s", e.getMessage()));
        }
    }

    /**
     * Метод поиска роли по уникальному идентификатору
     *
     * @param id
     * @return id
     * @throws RuntimeException Роль не найден!
     */
    public Role getRoleById(Long id) {
        log.info("Вызван метод: getRoleById");
        return repository.findById(id)
                .stream().
                findFirst().
                orElseThrow(() -> new RuntimeException(String.format("Роль с %s не найден!", id)));
    }

    /**
     * Метод создания нового роли
     *
     * @param role
     * @return new role
     * @throws RuntimeException Роль существует
     * @throws RuntimeException Ошибка при создании
     */
    public Role createRole(Role role) {
        log.info("Вызван метод: createRole");

        // Проверяем, существует ли роль с таким ID
        if (role.getId() != null && repository.existsById(role.getId())) {
            throw new RuntimeException("Роль с таким ID уже существует");
        }

        // Проверка уникальности имени роли
        if (repository.existsByName(role.getName())) {
            throw new RuntimeException("Имя роли уже занято");
        }

        try {
            return repository.save(role); // Сохраняем нового роли
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ошибка при создании роли: %s", e.getMessage()));
        }
    }

    /**
     * Метод обновления роли.
     *
     * @param role
     * @return create or update role
     * @throws Exception если не удалось сохранить роли
     */
    @Transactional
    public Role updateRoleById(Long id, Role role) {
        log.info("Вызван метод: updateRoleById");
        try {
            // Загружаем существующей роли
            Role existingRole = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));

            // Обновляем поля существующего роли
            existingRole.setName(role.getName());
            existingRole.setDescription(role.getDescription());

            // Теперь сохраняем обновленного роли
            log.info("Обновление роли: {}", existingRole);
            return repository.save(existingRole); // Hibernate автоматически обновит поле updateAt
        } catch (Exception e) {
            throw new RuntimeException(String.format("Не удалось сохранить роль: %s", e.getMessage()));
        }
    }

    /**
     * Метод удаления роли
     *
     * @param id
     * @throws EntityNotFoundException Роль не найден
     * @throws RuntimeException        Ошибка при удалении роли
     */
    public void deleteRoleById(Long id) {
        try {
            log.info("Вызван метод: deleteRoleById");
            repository.deleteById(id);
        } catch (EntityNotFoundException e) {
            log.warn("Попытка удалить несуществующей роли с : {}", id);
            throw new RuntimeException(String.format("Роль с %s не найдена!", id));
        } catch (Exception e) {
            // Обрабатываем другие потенциальные исключения
            throw new RuntimeException("Ошибка при удалении роли: " + e.getMessage(), e);
        }
    }
}
