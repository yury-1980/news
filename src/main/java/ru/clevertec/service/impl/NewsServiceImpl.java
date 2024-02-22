package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.News;
import ru.clevertec.exeption.EntityNotFoundExeption;
import ru.clevertec.mapper.NewsMapper;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository repository;
    private final NewsMapper mapper;

    /**
     * Создание News
     *
     * @param newsRequestDTO newsRequestDTO
     * @return News
     */
    @Override
    @Transactional
    public NewsResponseDTO create(NewsRequestDTO newsRequestDTO) {
        News news = mapper.toNews(newsRequestDTO);
        news.setTime(LocalDateTime.now());
        news.getTextNews().setNews(news);

        return mapper.toNewsResponseDTO(repository.save(news));
    }

    /**
     * Поиск News по его id
     *
     * @param idNews idNews
     * @return NewsResponseDTO
     */
    @Override
    public NewsResponseDTO findById(Long idNews) {

        return repository.findById(idNews)
                .map(mapper::toNewsResponseDTO)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));
    }

    /**
     * Вывод заданной страницы, с размером страницы
     *
     * @param pageNumber Номер страницы
     * @param pageSize   размером страницы
     * @return List, список найденных
     */
    @Override
    public List<NewsResponseDTO> findByAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return repository.findAll(pageRequest)
                .stream()
                .map(mapper::toNewsResponseDTO)
                .toList();
    }

    /**
     * Частичное обновление News
     *
     * @param newsRequestDTO newsRequestDTO
     * @param idNews         idNews
     * @return NewsResponseDTO
     */
    @Override
    @Transactional
    public NewsResponseDTO updatePatch(NewsRequestDTO newsRequestDTO, Long idNews) {
        News news = repository.findById(idNews)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));

        if (newsRequestDTO.getTitle() != null) {
            news.setTitle(newsRequestDTO.getTitle());
        }

        if (newsRequestDTO.getTextNews() != null) {
            news.getTextNews().setText(newsRequestDTO.getTextNews().getText());
        }

        return mapper.toNewsResponseDTO(repository.save(news));
    }

    /**
     * Удаление News по его id
     *
     * @param idNews idNews
     */
    @Override
    @Transactional
    public void delete(Long idNews) {
        repository.findById(idNews)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));
        repository.deleteById(idNews);
    }
}
