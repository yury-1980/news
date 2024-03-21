package ru.clevertec.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dto.requestDTO.CommentRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.entity.Comment;
import ru.clevertec.entity.News;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.exception.WrongDataException;
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

    final static String USER_NAME = "userName";
    final static String TEXT_COMMENT = "textComment";
    final static int SLOP = 3;

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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUBSCRIBER')")
    public CommentResponseDTO create(CommentRequestDTO commentRequestDTO, Long idNews) {
        News news = newsRepository.findById(idNews)
                                  .orElseThrow(() -> EntityNotFoundException.of(Long.class));

        Comment comment = mapper.toComment(commentRequestDTO);
        comment.setTime(LocalDateTime.now());
        comment.setNews(news);
        comment.setTextComment(commentRequestDTO.getTextComment());
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
                         .orElseThrow(() -> EntityNotFoundException.of(Long.class));
    }

    /**
     * Вывод заданной страницы и количество результатов на ней.
     *
     * @param pageNumber номер страницы результатов, начиная с 0.
     * @param pageSize   количество результатов на странице.
     * @return List, список найденных комментариев
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUBSCRIBER')")
    public CommentResponseDTO updatePatch(CommentRequestDTO commentRequestDTO, Long idComment, UserDetails userDetails) {
        Comment comment = repository.findById(idComment)
                                    .orElseThrow(() -> EntityNotFoundException.of(Long.class));
        if (comment.getUserName().equals(userDetails.getUsername())) {

            if (commentRequestDTO.getTextComment() != null) {
                comment.setTextComment(commentRequestDTO.getTextComment());
            }
        } else {
            log.error("Invalid name");
            throw WrongDataException.of(String.class);
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUBSCRIBER')")
    public void delete(Long idComment, UserDetails userDetails) {
        Comment comment = repository.findById(idComment)
                                    .orElseThrow(() -> EntityNotFoundException.of(Long.class));

        if (comment.getUserName().equals(userDetails.getUsername())) {
            repository.deleteById(idComment);

        } else {
            log.error("Invalid name");
            throw WrongDataException.of(String.class);
        }
    }

    /**
     * Запросы с подстановочными знаками
     *
     * @param predicate  Буква или набор букв из имени
     * @param pageNumber номер страницы результатов, начиная с 0.
     * @param pageSize   количество результатов на странице.
     * @return List<CommentResponseDTO>
     */
    @Override
    public List<CommentResponseDTO> findByAllComentsByPredicateName(String predicate, int pageNumber, int pageSize) {
        SearchSession searchSession = Search.session(entityManager);

        SearchPredicate userNamePredicate = searchSession.scope(Comment.class)
                                                         .predicate()
                                                         .wildcard()
                                                         .field(USER_NAME)
                                                         .matching("*" + predicate + "*")
                                                         .toPredicate();

        return predicateOrphrase(searchSession, userNamePredicate, pageNumber, pageSize);
    }

    /**
     * Фразовые запросы
     *
     * @param phrase     Фраза из комментария.
     * @param pageNumber номер страницы результатов, начиная с 0.
     * @param pageSize   количество результатов на странице.
     * @return List<CommentResponseDTO>
     */
    @Override
    public List<CommentResponseDTO> findByAllTextsByPhrase(String phrase, int pageNumber, int pageSize) {
        SearchSession searchSession = Search.session(entityManager);

        SearchPredicate predicate = searchSession.scope(Comment.class)
                                                 .predicate()
                                                 .phrase()
                                                 .field(TEXT_COMMENT)
                                                 .matching(phrase)
                                                 .slop(SLOP)
                                                 .toPredicate();

        return predicateOrphrase(searchSession, predicate, pageNumber, pageSize);
    }

    /**
     * Выполняет поиск комментариев в индексе с использованием предиката или фразы
     * и возвращает список объектов CommentResponseDTO, содержащих результаты поиска.
     *
     * @param searchSession используется для выполнения поиска.
     * @param predicate     предикат или фраза, определяющая условия поиска.
     * @param pageNumber    номер страницы результатов, начиная с 0.
     * @param pageSize      количество результатов на странице.
     * @return список объектов CommentResponseDTO.
     */
    private List<CommentResponseDTO> predicateOrphrase(SearchSession searchSession, SearchPredicate predicate,
                                                       int pageNumber, int pageSize) {
        int start = pageNumber * pageSize;

        SearchResult<Comment> result = searchSession.search(Comment.class)
                                                    .where(predicate)
                                                    .fetch(start, pageSize);

        long totalHitCount = result.total().hitCount();
        List<Comment> comments = result.hits();

        log.info("Найдено комментариев: " + totalHitCount);

        for (Comment hit : comments) {
            log.info("Комментарий: " + hit.getTextComment());
        }

        return comments.stream()
                       .map(mapper::toCommentResponseDto)
                       .toList();
    }
}
