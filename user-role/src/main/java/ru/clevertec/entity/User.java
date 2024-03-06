package ru.clevertec.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import org.springframework.stereotype.Indexed;
import ru.clevertec.entity.type.Role;

@Data
@Entity
@Indexed
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    @NotBlank(message = "Имя не должен быть пустым")
    @Size(max = 32, message = "Имя пользователя должно превышать 32 символов")
    private String username;

    @Column(name = "email")
    @Email(message = "Неверный Email")
    private String email;

    @Column(name = "role")
    @Schema(description = "Тип допуска User.")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Role role;

    @Column(name = "password")
    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(max = 64, message = "Длина пароля не должна превышать 64 символа")
    private String password;
}
