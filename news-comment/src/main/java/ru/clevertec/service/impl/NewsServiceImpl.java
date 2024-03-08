package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.annotation.MyCreate;
import ru.clevertec.annotation.MyDelete;
import ru.clevertec.annotation.MyFind;
import ru.clevertec.annotation.MyUpdate;
import ru.clevertec.service.AuthenticationService;
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.Comment;
import ru.clevertec.entity.News;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.exception.WrongDataException;
import ru.clevertec.mapper.CommentsMapper;
import ru.clevertec.mapper.NewsMapper;
import ru.clevertec.repository.CommentRepository;
import ru.clevertec.repository.NewsRepository;
import ru.clevertec.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final static String TITLE = "title";
    private final static String TEXT_NEWS = "textNews";
    private final static int SLOP = 3;

    private final NewsRepository repository;
    private final NewsMapper mapper;
    private final CommentRepository commentRepository;
    private final CommentsMapper commentsMapper;
    private final SearchSession searchSession;
    private final AuthenticationService authenticationService;

    /**
     * Создание News
     *
     * @param newsRequestDTO newsRequestDTO
     * @return News
     */
    @MyCreate
    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_JOURNALIST')")
    public NewsResponseDTO create(NewsRequestDTO newsRequestDTO) {
        News news = mapper.toNews(newsRequestDTO);
        news.setTime(LocalDateTime.now());
        news.setTextNews(newsRequestDTO.getTextNews());

        return mapper.toNewsResponseDTO(repository.save(news));
    }

    /**
     * Поиск News по его id
     *
     * @param idNews idNews
     * @return NewsResponseDTO
     */
    @MyFind
    @Override
    public NewsResponseDTO findById(Long idNews) {

        return repository.findById(idNews)
                         .map(mapper::toNewsResponseDTO)
                         .orElseThrow(() -> EntityNotFoundException.of(Long.class));
    }

    /**
     * Вывод заданной страницы и количество результатов на ней.
     *
     * @param pageNumber номер страницы результатов, начиная с 0.
     * @param pageSize   количество результатов на странице.
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
     * Поиск News по его id и её комментариев
     *
     * @param idNews idNews
     * @return NewsCommentsRequestDto
     */
    @Override
    public List<CommentResponseDTO> findByIdNewsAndComments(Long idNews, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        return commentRepository.findCommentsByNews_Id(idNews, pageRequest).stream()
                                .map(commentsMapper::toCommentResponseDto)
                                .toList();
    }

    /**
     * Поиск News по его id и его комментария по id
     *
     * @param idNews idNews
     * @return NewsCommentsRequestDto
     */
    @Override
    public CommentResponseDTO findByIdNewsAndIdComments(Long idNews, Long idComment) {
        repository.findById(idNews)
                  .orElseThrow(() -> EntityNotFoundException.of(News.class));

        return commentsMapper.toCommentResponseDto(
                commentRepository.findCommentByIdAndNews_Id(idNews, idComment).orElseThrow(
                        () -> EntityNotFoundException.of(Comment.class))
                                                  );
    }

    /**
     * Частичное обновление News
     *
     * @param newsRequestDTO newsRequestDTO
     * @param idNews         idNews
     * @return NewsResponseDTO
     */
    @Override
    @MyUpdate
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_JOURNALIST')")
    public NewsResponseDTO updatePatch(NewsRequestDTO newsRequestDTO,
                                       Long idNews, UserDetails userDetails) {

        News news = repository.findById(idNews)
                              .orElseThrow(() -> EntityNotFoundException.of(Long.class));

        if (news.getAuthor().equals(userDetails.getUsername())) {

            if (newsRequestDTO.getTitle() != null) {
                news.setTitle(newsRequestDTO.getTitle());
            }

            if (newsRequestDTO.getTextNews() != null) {
                news.setTextNews(newsRequestDTO.getTextNews());
            }
        } else {
            log.error("Invalid name");
            throw WrongDataException.of(String.class);
        }

        return mapper.toNewsResponseDTO(repository.save(news));
    }

    /**
     * Удаление News по его id
     *
     * @param idNews idNews
     */
    @Override
    @MyDelete
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_JOURNALIST')")
    public void delete(Long idNews, UserDetails userDetails) {
        News news = repository.findById(idNews)
                              .orElseThrow(() -> EntityNotFoundException.of(Long.class));

        if (news.getAuthor().equals(userDetails.getUsername())) {
            repository.deleteById(idNews);

        } else {
            log.error("Invalid name");
            throw WrongDataException.of(String.class);
        }
    }

    /**
     * Запросы с подстановочными знаками
     *
     * @param predicate  Буква или набор букв из Title
     * @param pageNumber номер страницы результатов, начиная с 0.
     * @param pageSize   количество результатов на странице.
     * @return List<NewsResponseDTO>
     */
    @Override
    public List<NewsResponseDTO> findByAllNewsByPredicateTitle(String predicate, int pageNumber, int pageSize) {
        SearchPredicate userNamePredicate = searchSession.scope(News.class)
                                                         .predicate()
                                                         .wildcard()
                                                         .field(TITLE)
                                                         .matching("*" + predicate + "*")
                                                         .toPredicate();

        return predicateOrPhrase(searchSession, userNamePredicate, pageNumber, pageSize);
    }

    /**
     * Фразовые запросы
     *
     * @param phrase     Фраза из текста новости.
     * @param pageNumber номер страницы результатов, начиная с 0.
     * @param pageSize   количество результатов на странице.
     * @return List<NewsResponseDTO>
     */
    @Override
    public List<NewsResponseDTO> findByAllTextsByPhrase(String phrase, int pageNumber, int pageSize) {
        SearchPredicate predicate = searchSession.scope(News.class)
                                                 .predicate()
                                                 .phrase()
                                                 .field(TEXT_NEWS)
                                                 .matching(phrase)
                                                 .slop(SLOP)
                                                 .toPredicate();

        return predicateOrPhrase(searchSession, predicate, pageNumber, pageSize);
    }

    /**
     * Выполняет поиск новостей в индексе с использованием предиката или фразы
     * и возвращает список объектов NewsResponseDTO, содержащих результаты поиска.
     *
     * @param searchSession используется для выполнения поиска.
     * @param predicate     предикат или фраза, определяющая условия поиска.
     * @param pageNumber    номер страницы результатов, начиная с 0.
     * @param pageSize      количество результатов на странице.
     * @return список объектов NewsResponseDTO.
     */
    private List<NewsResponseDTO> predicateOrPhrase(SearchSession searchSession, SearchPredicate predicate,
                                                    int pageNumber, int pageSize) {
        int start = pageNumber * pageSize;

        SearchResult<News> result = searchSession.search(News.class)
                                                 .where(predicate)
                                                 .fetch(start, pageSize);

        long totalHitCount = result.total().hitCount();
        List<News> news = result.hits();

        log.info("Найдено новостей: " + totalHitCount);

        for (News hit : news) {
            log.info("Новость: " + hit.getTextNews());
        }

        return news.stream()
                   .map(mapper::toNewsResponseDTO)
                   .toList();
    }
}
