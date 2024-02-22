package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;

public interface CommentService extends Service<CommentResponseDTO, CommentRequestDTO> {

    CommentResponseDTO create(CommentRequestDTO commentRequestDTO, Long idNews);
}
