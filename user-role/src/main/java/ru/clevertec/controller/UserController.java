package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.service.Service;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final Service service;

    @PostMapping
    @Operation(summary = "Создание User.")
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO userRequestDTO) {

        return ResponseEntity.ok(service.create(userRequestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного User, по его id.")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable("id") Long idUser) {

        return ResponseEntity.ok(service.findById(idUser));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление User.")
    public ResponseEntity<UserResponseDTO> updatePatch(@RequestBody UserRequestDTO userRequestDTO,
                                                       @PathVariable("id") Long idUser) {

        return ResponseEntity.ok(service.updatePatch(userRequestDTO, idUser));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление User по его id.")
    public ResponseEntity<Void> delete(@PathVariable("id") Long idUser) {
        service.delete(idUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
