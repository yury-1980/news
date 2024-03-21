package ru.clevertec.clientFeign;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.dto.requestDTO.UserRequestDTO;
import ru.clevertec.dto.responseDTO.UserResponseDTO;
import ru.clevertec.entity.User;

@Component
@FeignClient(url = "${urls.user-role}", name = "user-role")
public interface UserClientFeign {

    @GetMapping
    @Operation(summary = " Выбор заданного User, по его email.")
    ResponseEntity<User> findByUsername(@RequestParam String username);

    @PostMapping
    @Operation(summary = "Создание User.")
    ResponseEntity<UserResponseDTO> create(@RequestBody User user);

    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного User, по его id.")
    ResponseEntity<UserResponseDTO> findById(@PathVariable("id") Long idUser);

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление User.")
    public ResponseEntity<UserResponseDTO> updatePatch(@RequestBody UserRequestDTO userRequestDTO,
                                                       @PathVariable("id") Long idUser);

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление User по его id.")
    ResponseEntity<Void> delete(@PathVariable("id") Long idUser);
}
