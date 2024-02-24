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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService service;

    @PostMapping("/{id}")
    @Operation(summary = "Создание Comment.")
    public ResponseEntity<CommentResponseDTO> create(@RequestBody CommentRequestDTO commentRequestDTO,
                                                     @PathVariable("id") Long idNews) {

        return ResponseEntity.ok(service.create(commentRequestDTO, idNews));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного Comment, по его id.")
    public ResponseEntity<CommentResponseDTO> findById(@PathVariable("id") Long idComment) {

        return ResponseEntity.ok(service.findById(idComment));
    }

    @GetMapping
    @Operation(summary = "Выбор всех Comment из заданной страницы.")
    public ResponseEntity<List<CommentResponseDTO>> findByAll(@RequestParam(defaultValue = "0") int pageNumber,
                                                              @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAll(pageNumber, pageSize));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление Comment.")
    public ResponseEntity<CommentResponseDTO> updatePatch(@RequestBody CommentRequestDTO commentRequestDTO,
                                                          @PathVariable("id") Long idComment) {

        return ResponseEntity.ok(service.updatePatch(commentRequestDTO, idComment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление Comment по его id.")
    public ResponseEntity<Void> delete(@PathVariable("id") Long idComment) {
        service.delete(idComment);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/names/{str}")
    public ResponseEntity<List<CommentResponseDTO>> findByAllComentsByName(@PathVariable("str") String string,
                                                              @RequestParam(defaultValue = "0") int pageNumber,
                                                              @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAllComentsByName(string, pageNumber, pageSize));
    }
}
