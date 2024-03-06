package ru.clevertec.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.service.NewsService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
@Tag(name = "Контроллер новостей", description = "Различные манипуляции с новостями")
public class NewsController {

    private final NewsService service;

    @PostMapping
    @MyLogController
    @Operation(summary = "Создание News.")
    public ResponseEntity<NewsResponseDTO> create(@Valid @RequestBody NewsRequestDTO newsRequestDTO) {

        return ResponseEntity.ok(service.create(newsRequestDTO));
    }

    @MyLogController
    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного News, по его id.")
    public ResponseEntity<NewsResponseDTO> findById(@Positive @PathVariable("id") Long idNews) {

        return ResponseEntity.ok(service.findById(idNews));
    }

    @GetMapping
    @MyLogController
    @Operation(summary = "Выбор всех News из заданной страницы.")
    public ResponseEntity<List<NewsResponseDTO>> findByAll(@PositiveOrZero @RequestParam(defaultValue = "0") int pageNumber,
                                                           @Positive @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAll(pageNumber, pageSize));
    }

    @MyLogController
    @GetMapping("/{newsId}/comments")
    @Operation(summary = "Выбор заданного News, по его id и его комментариев.")
    public ResponseEntity<List<CommentResponseDTO>> findByIdNewsAndComments(@Positive @PathVariable("newsId") Long idNews,

                                                                            @PositiveOrZero
                                                                            @RequestParam(defaultValue = "0") int pageNumber,

                                                                            @Positive
                                                                            @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByIdNewsAndComments(idNews, pageNumber, pageSize));
    }

    @MyLogController
    @GetMapping("/{newsId}/comments/{commentsId}")
    @Operation(summary = "Выбор заданного News, по его id и комментария этого News по id.")
    public ResponseEntity<CommentResponseDTO> findByIdNewsAndIdComments(@Positive @PathVariable("newsId") Long idNews,
                                                                        @Positive @PathVariable("commentsId") Long idComment) {

        return ResponseEntity.ok(service.findByIdNewsAndIdComments(idNews, idComment));
    }

    @MyLogController
    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление News.")
    public ResponseEntity<NewsResponseDTO> updatePatch(@Valid @RequestBody NewsRequestDTO newsRequestDTO,
                                                       @Positive @PathVariable("id") Long idNews,
                                                       @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(service.updatePatch(newsRequestDTO, idNews, userDetails));
    }

    @MyLogController
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление News по его id.")
    public ResponseEntity<Void> delete(@Positive @PathVariable("id") Long idNews,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        service.delete(idNews, userDetails);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                             .build();
    }

    @MyLogController
    @GetMapping("/titles/{str}/predicate")
    @Operation(summary = "Запросы с подстановочными знаками")
    public ResponseEntity<List<NewsResponseDTO>> findByAllNewsByPredicateTitle(@PathVariable("str") String string,

                                                                               @PositiveOrZero
                                                                               @RequestParam(defaultValue = "0") int pageNumber,

                                                                               @Positive
                                                                               @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAllNewsByPredicateTitle(string, pageNumber, pageSize));
    }

    @MyLogController
    @GetMapping("/texts/{str}/phrase")
    @Operation(summary = "Фразовые запросы")
    public ResponseEntity<List<NewsResponseDTO>> findByAllTextsByPhrase(@PathVariable("str") String string,

                                                                        @PositiveOrZero
                                                                        @RequestParam(defaultValue = "0") int pageNumber,

                                                                        @Positive
                                                                        @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAllTextsByPhrase(string, pageNumber, pageSize));
    }
}
