package ru.clevertec.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.entity.Comment;
import ru.clevertec.entity.News;
import ru.clevertec.exeption.EntityNotFoundExeption;
import ru.clevertec.mapper.CommentsMapper;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final NewsRepository newsRepository;
    private final CommentRepository repository;
    private final CommentsMapper mapper;
    private final EntityManager entityManager;

    /**
     * Создание Comment
     *
     * @param commentRequestDTO commentRequestDTO
     * @param idNews            idNews
     * @return Comment
     */
    @Override
    @Transactional
    public CommentResponseDTO create(CommentRequestDTO commentRequestDTO, Long idNews) {
        News news = newsRepository.findById(idNews)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));

        Comment comment = mapper.toComment(commentRequestDTO);
        comment.setTime(LocalDateTime.now());
        comment.setNews(news);
        comment.getTextComment().setComment(comment);
        news.getComments().add(comment);

        return mapper.toCommentResponseDto(repository.save(comment));
    }

    /**
     * Поиск Comment по его id
     *
     * @param idComment idComment
     * @return CommentResponseDTO
     */
    @Override
    public CommentResponseDTO findById(Long idComment) {
        return repository.findById(idComment)
                .map(mapper::toCommentResponseDto)
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
    public List<CommentResponseDTO> findByAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return repository.findAll(pageRequest)
                .stream()
                .map(mapper::toCommentResponseDto)
                .toList();
    }

    /**
     * Частичное обновление News
     *
     * @param commentRequestDTO commentRequestDTO
     * @param idComment         idComment
     * @return CommentResponseDTO
     */
    @Override
    @Transactional
    public CommentResponseDTO updatePatch(CommentRequestDTO commentRequestDTO, Long idComment) {
        Comment comment = repository.findById(idComment)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));

        if (commentRequestDTO.getTextComment() != null) {
            comment.getTextComment().setText(commentRequestDTO.getTextComment().getText());
        }

        return mapper.toCommentResponseDto(repository.save(comment));
    }

    /**
     * Удаление Comment по его id
     *
     * @param idComment idComment
     */
    @Override
    @Transactional
    public void delete(Long idComment) {
        repository.findById(idComment)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));
        repository.deleteById(idComment);
    }

    @Override
    public List<CommentResponseDTO> findByAllComentsByName(String string, int pageNumber, int pageSize) {
        int start = pageNumber * pageSize;

        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Comment> result = searchSession.search(Comment.class)
                .where(f -> f.match().fields("userName", "textComment").matching(string)
                        .fuzzy(2))
                .fetch(start, pageSize);

        long totalHitCount = result.total().hitCount();
        List<Comment> hits = result.hits();

        log.info("Найдено комментариев: " + totalHitCount);
        for (Comment hit : hits) {
            log.info("Комментарий: " + hit.getTextComment().getText());
        }

        return hits.stream()
                .map(mapper::toCommentResponseDto)
                .toList();
    }
}
