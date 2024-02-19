package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.entity.Comment;

public interface CommentService extends Service<CommentResponseDTO, CommentRequestDTO> {

    Comment create(CommentRequestDTO commentRequestDTO, Long idNews);
}
