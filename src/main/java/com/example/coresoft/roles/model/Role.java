package com.example.coresoft.roles.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Этот класс представляет роли в системе.
 */
@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    @NotBlank(message = "Роль не может быть пуста")
    @Column(name = "name")
    @Schema(description = "Название роли",
            example = "Test", required = true)
    private String name;

    @NotBlank(message = "Описание не должно быть пустое")
    @Column(name = "description")
    @Schema(description = "описание роли",
            example = "Описание", required = true)
    private String description;

    public Role() {
    }

    public Role(String description, String name, Long id) {
        this.description = description;
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name) && Objects.equals(description, role.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
