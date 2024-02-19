package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.News;

public interface NewsService extends Service<NewsResponseDTO, NewsRequestDTO> {

    News create(NewsRequestDTO newsRequestDTO);
}
