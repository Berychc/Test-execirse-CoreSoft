package com.example.coresoft.userandrole.repository;

import com.example.coresoft.userandrole.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы ролей над пользователем в системе.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUserId(Long userId);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}
