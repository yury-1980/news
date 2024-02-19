package ru.clevertec.mapper;

import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.entity.Comment;

public interface CommentsMapper {

    CommentResponseDTO toCommentResponseDto(Comment comment);

    Comment toComment(CommentRequestDTO commentRequestDTO);
}
