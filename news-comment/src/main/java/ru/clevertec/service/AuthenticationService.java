package ru.clevertec.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.AuthenticationRequest;
import ru.clevertec.auth.AuthenticationResponse;
import ru.clevertec.auth.RegisterRequest;
import ru.clevertec.clientFeign.UserClientFeign;
import ru.clevertec.entity.User;
import ru.clevertec.entity.type.Role;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserClientFeign repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрирует нового пользователя и возвращает ответ аутентификации.
     *
     * @param request Запрос на регистрацию
     * @return Ответ аутентификации
     */
    public AuthenticationResponse register(RegisterRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = User.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .role(Role.ROLE_SUBSCRIBER)
                        .build();

        repository.create(user);
        String jwtToken = jwtService.generateToken(user);
        jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                                     .token(jwtToken)
                                     .build();
    }

    /**
     * Аутентифицирует пользователя и возвращает ответ аутентификации.
     *
     * @param request Запрос на аутентификацию
     * @return Ответ аутентификации
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
                                          );

        User user = repository.findByUsername(request.getUsername()).getBody();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                                     .token(jwtToken)
                                     .build();
    }
}
