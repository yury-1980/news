package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;

import java.util.List;

public interface CommentService extends Service<CommentResponseDTO, CommentRequestDTO> {

    CommentResponseDTO create(CommentRequestDTO commentRequestDTO, Long idNews);

    List<CommentResponseDTO> findByAllComentsByPredicateName(String predicate, int pageNumber, int pageSize);
}
