package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import ru.clevertec.annotation.MyLogController;
import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.service.CommentService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
@Tag(name = "Контроллер комментариев", description = "Различные манипуляции с комментариями")
public class CommentController {

    private final CommentService service;

    @MyLogController
    @PostMapping("/{id}")
    @Operation(summary = "Создание Comment.")
    public ResponseEntity<CommentResponseDTO> create(@Valid @RequestBody CommentRequestDTO commentRequestDTO,

                                                     @Positive
                                                     @PathVariable("id")
                                                     @Parameter(description = "ID Новость, для которой создаём комментарий")
                                                     Long idNews) {

        return ResponseEntity.ok(service.create(commentRequestDTO, idNews));
    }

    @MyLogController
    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного Comment, по его id.")
    public ResponseEntity<CommentResponseDTO> findById(@Positive @PathVariable("id") Long idComment) {

        return ResponseEntity.ok(service.findById(idComment));
    }

    @GetMapping
    @MyLogController
    @Operation(summary = "Выбор всех Comment из заданной страницы.")
    public ResponseEntity<List<CommentResponseDTO>> findByAll(@PositiveOrZero @RequestParam(defaultValue = "0") int pageNumber,
                                                              @Positive @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAll(pageNumber, pageSize));
    }

    @MyLogController
    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление Comment.")
    public ResponseEntity<CommentResponseDTO> updatePatch(@Valid @RequestBody CommentRequestDTO commentRequestDTO,
                                                          @Positive @PathVariable("id") Long idComment,
                                                          @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(service.updatePatch(commentRequestDTO, idComment, userDetails));
    }

    @MyLogController
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление Comment по его id.")
    public ResponseEntity<Void> delete(@Positive @PathVariable("id") Long idComment,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        service.delete(idComment, userDetails);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    @MyLogController
    @GetMapping("/names/{str}/predicate")
    @Operation(summary = "Запросы с подстановочными знаками")
    public ResponseEntity<List<CommentResponseDTO>> findByAllComentsByPredicateName(@PathVariable("str") String string,

                                                                                    @PositiveOrZero
                                                                                    @RequestParam(defaultValue = "0") int pageNumber,

                                                                                    @Positive
                                                                                    @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAllComentsByPredicateName(string, pageNumber, pageSize));
    }

    @MyLogController
    @GetMapping("/texts/{str}/phrase")
    @Operation(summary = "Фразовые запросы")
    public ResponseEntity<List<CommentResponseDTO>> findByAllTextsByPhrase(@PathVariable("str") String string,

                                                                           @PositiveOrZero
                                                                           @RequestParam(defaultValue = "0") int pageNumber,

                                                                           @Positive
                                                                           @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAllTextsByPhrase(string, pageNumber, pageSize));
    }
}
