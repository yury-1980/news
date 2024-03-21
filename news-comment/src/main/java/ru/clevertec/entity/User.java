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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Indexed;
import ru.clevertec.entity.type.Role;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Indexed
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не должен быть пустым")
    @Size(max = 32, message = "Имя пользователя должно превышать 32 символов")
    @Column(name = "user_name")
    private String username;

    @Email(message = "Неверный Email")
    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Schema(description = "Тип допуска User.")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Role role;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(max = 64, message = "Длина пароля не должна превышать 64 символа")
    @Column(name = "password")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
