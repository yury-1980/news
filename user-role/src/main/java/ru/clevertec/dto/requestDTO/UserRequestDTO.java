package ru.clevertec.dto.requestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import ru.clevertec.entity.type.Role;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @Schema(description = "Имя User.")
    @Column(name = "user_name")
    private String userName;

    @Column(name = "type")
    @Schema(description = "Тип допуска User.")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Role type;

    @Schema(description = "Пароль User.")
    @Column(name = "password")
    private String password;
}
