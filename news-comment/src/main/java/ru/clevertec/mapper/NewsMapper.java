package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsCommentsResponseDto;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.News;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsResponseDTO toNewsResponseDTO(News news);

    News toNews(NewsRequestDTO newsRequestDTO);

    NewsCommentsResponseDto toNewsCommentsResponseDto(News news);
}
