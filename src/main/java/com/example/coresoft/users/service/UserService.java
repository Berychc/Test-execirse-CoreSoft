package com.example.coresoft.users.service;

import com.example.coresoft.users.model.User;
import com.example.coresoft.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Метод получения всех пользователей.
     *
     * @return allUsers
     * @throws RuntimeException Нету пользователей
     * @throws RuntimeException Ошибка при получении пользователей
     */
    public List<User> getAllUsers() {
        log.info("Вызван метод: getAllUsers");
        try {
            List<User> allUsers = repository.findAll();
            if (allUsers.isEmpty()) {
                throw new RuntimeException("Нету пользователей для отображения");
            }
            return allUsers;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ошибка при получении пользователей: %s", e.getMessage()));
        }
    }

    /**
     * Метод поиска пользователя по уникальному идентификатору
     *
     * @param id
     * @return id
     * @throws RuntimeException Пользователь не найден!
     */
    public User getUserById(Long id) {
        log.info("Вызван метод: getUserById");
        return repository.findById(id)
                .stream().
                findFirst().
                orElseThrow(() -> new RuntimeException(String.format("Пользователь с %s не найден!", id)));
    }

    /**
     * Метод создания нового пользователя
     *
     * @param user
     * @return new user
     * @throws RuntimeException Пользователь существует
     * @throws RuntimeException Ошибка при создании
     */
    public User createUser(User user) {
        log.info("Вызван метод: createUser");

        // Проверяем, существует ли пользователь с таким ID
        if (user.getId() != null && repository.existsById(user.getId())) {
            throw new RuntimeException("Пользователь с таким ID уже существует");
        }

        // Проверка уникальности имени пользователя
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Имя пользователя уже занято");
        }

        // Проверка уникальности email
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email уже зарегистрирован");
        }

        // Шифрование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            return repository.save(user); // Сохраняем нового пользователя
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ошибка при создании пользователя: %s", e.getMessage()));
        }
    }

    /**
     * Метод обновления пользователя.
     *
     * @param user
     * @return create or update user
     * @throws Exception если не удалось сохранить пользователя
     */
    @Transactional
    public User updateUserById(Long id, User user) {
        log.info("Вызван метод: updateUserById");
        try {
            // Загружаем существующего пользователя
            User existingUser = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            // Обновляем поля существующего пользователя
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());

            // Если вы хотите обновить пароль, не забудьте его зашифровать
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Теперь сохраняем обновленного пользователя
            log.info("Обновление пользователя: {}", existingUser);
            return repository.save(existingUser); // Hibernate автоматически обновит поле updateAt
        } catch (Exception e) {
            throw new RuntimeException(String.format("Не удалось сохранить пользователя: %s", e.getMessage()));
        }
    }

    /**
     * Метод удаления пользователя
     *
     * @param id
     * @throws EntityNotFoundException Пользователь не найден
     * @throws RuntimeException        Ошибка при удалении пользователя
     */
    public void deleteUserById(Long id) {
        try {
            log.info("Вызван метод: deleteUserById");
            repository.deleteById(id);
        } catch (EntityNotFoundException e) {
            log.warn("Попытка удалить несуществующего пользователя с : {}", id);
            throw new RuntimeException(String.format("Пользователь с %s не найден!", id));
        } catch (Exception e) {
            // Обрабатываем другие потенциальные исключения
            throw new RuntimeException("Ошибка при удалении пользователя: " + e.getMessage(), e);
        }
    }
}
