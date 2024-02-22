package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentsMapper {

    CommentResponseDTO toCommentResponseDto(Comment comment);

    Comment toComment(CommentRequestDTO commentRequestDTO);
}
