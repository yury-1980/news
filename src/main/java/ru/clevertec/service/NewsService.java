package ru.clevertec.service;

import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;

import java.util.List;

public interface NewsService extends Service<NewsResponseDTO, NewsRequestDTO> {

    NewsResponseDTO create(NewsRequestDTO newsRequestDTO);

    List<CommentResponseDTO> findByIdNewsAndComments(Long idNews, int pageNumber, int pageSize);

    CommentResponseDTO findByIdNewsAndIdComments(Long idNews, Long idComment);

    List<NewsResponseDTO> findByAllNewsByTitle(String string, int pageNumber, int pageSize);
}
