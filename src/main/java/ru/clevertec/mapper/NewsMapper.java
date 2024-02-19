package ru.clevertec.mapper;

import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.News;

public interface NewsMapper {

    NewsResponseDTO toNewsResponseDTO(News news);

    News toNews(NewsRequestDTO newsRequestDTO);
}
