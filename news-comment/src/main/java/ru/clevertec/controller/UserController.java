package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;
import ru.clevertec.service.UserService;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Контроллер пользователей", description = "Различные манипуляции с пользователями")
public class UserController {

    private final UserService service;

    @PostMapping
    @Operation(summary = "Создание User.")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody User user) {

        return ResponseEntity.ok(service.create(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного User, по его id.")
    public ResponseEntity<UserResponseDTO> findById(@Positive @PathVariable("id") Long idUser) {

        return ResponseEntity.ok(service.findById(idUser));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление User.")
    public ResponseEntity<UserResponseDTO> updatePatch(@Valid @RequestBody UserRequestDTO userRequestDTO,
                                                       @Positive @PathVariable("id") Long idUser) {

        return ResponseEntity.ok(service.updatePatch(userRequestDTO, idUser));
    }

    @GetMapping()
    @Operation(summary = "Выбор заданного User, по его email.")
    public ResponseEntity<User> findByUsername(@RequestParam String username) {
        return ResponseEntity.ok(service.findByUsername(username));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление User по его id.")
    public ResponseEntity<Void> delete(@Positive @PathVariable("id") Long idUser) {
        service.delete(idUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }
}
