package com.example.coresoft.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Этот класс представляет пользователя в системе.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Column(name = "username")
    @Schema(description = "Имя пользователя",
            example = "Tester", required = true)
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email
    @Column(name = "email")
    @Schema(description = "Email пользователя",
            example = "tester@gmail.com", required = true)
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Column(name = "password")
    @Schema(description = "Пароль пользователя",
            required = true)
    private String password;

    @Column(name = "createAt")
    @CreationTimestamp
    @JsonIgnore
    @Schema(description = "Дата и время создания пользователя")
    private LocalDateTime createdAt;

    @Column(name = "updateAt")
    @UpdateTimestamp
    @JsonIgnore
    @Schema(description = "Дата и время последнего обновления пользователя")
    private LocalDateTime updateAt;

    public User() {
    }

    public User(Long id, String username, String email, String password, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updateAt, user.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, createdAt, updateAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
