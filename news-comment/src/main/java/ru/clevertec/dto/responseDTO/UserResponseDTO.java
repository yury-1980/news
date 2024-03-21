package ru.clevertec.dto.responseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import ru.clevertec.entity.type.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    @Schema(description = "Имя User.")
    @NotBlank(message = "Имя не должен быть пустым")
    @Size(max = 32, message = "Имя пользователя должно превышать 32 символов")
    private String username;

    @Email(message = "Неверный Email")
    @Schema(description = " email User")
    private String email;

    @Schema(description = "Пароль User.")
    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(max = 64, message = "Длина пароля не должна превышать 64 символа")
    private String password;

    @Schema(description = "Тип допуска User.")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Role role;
}
