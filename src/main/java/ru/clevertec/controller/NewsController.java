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
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.News;
import ru.clevertec.service.NewsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService service;

    @PostMapping
    @Operation(summary = "Создание News.")
    public ResponseEntity<News> create(@RequestBody NewsRequestDTO newsRequestDTO) {

        return ResponseEntity.ok(service.create(newsRequestDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Выбор заданного News, по его id.")
    public ResponseEntity<NewsResponseDTO> findById(@PathVariable("id") Long idNews) {

        return ResponseEntity.ok(service.findById(idNews));
    }

    @GetMapping
    @Operation(summary = "Выбор всех News из заданной страницы.")
    public ResponseEntity<List<NewsResponseDTO>> findByAll(@RequestParam(defaultValue = "0") int pageNumber,
                                                           @RequestParam(defaultValue = "15") int pageSize) {

        return ResponseEntity.ok(service.findByAll(pageNumber, pageSize));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление News.")
    public ResponseEntity<NewsResponseDTO> updatePatch(@RequestBody NewsRequestDTO newsRequestDTO, Long idNews) {

        return ResponseEntity.ok(service.updatePatch(newsRequestDTO, idNews));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление News по его id.")
    public ResponseEntity<Void> delete(@PathVariable("id") Long idNews) {
        service.delete(idNews);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
