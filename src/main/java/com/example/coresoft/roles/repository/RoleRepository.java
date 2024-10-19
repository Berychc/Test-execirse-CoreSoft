package com.example.coresoft.roles.repository;

import com.example.coresoft.roles.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с пользователем в системе.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
}
