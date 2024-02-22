package ru.clevertec.service;

import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;

public interface NewsService extends Service<NewsResponseDTO, NewsRequestDTO> {

    NewsResponseDTO create(NewsRequestDTO newsRequestDTO);
}
