package ru.clevertec.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.annotation.MyCreate;
import ru.clevertec.annotation.MyDelete;
import ru.clevertec.annotation.MyFind;
import ru.clevertec.annotation.MyUpdate;
import ru.clevertec.dto.requestDTO.NewsRequestDTO;
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.entity.Comment;
import ru.clevertec.entity.News;
import ru.clevertec.exeption.EntityNotFoundExeption;
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

    final static String TITLE = "title";
    final static String TEXT_NEWS = "textNews";
    final static int SLOP = 3;

    private final NewsRepository repository;
    private final NewsMapper mapper;
    private final CommentRepository commentRepository;
    private final CommentsMapper commentsMapper;
    private final EntityManager entityManager;

    /**
     * Создание News
     *
     * @param newsRequestDTO newsRequestDTO
     * @return News
     */
    @MyCreate
    @Override
    @Transactional
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
                .orElseThrow(() -> EntityNotFoundExeption.of(News.class));

        return commentsMapper.toCommentResponseDto(commentRepository.findCommentByIdAndNews_Id(idNews, idComment)
                                                           .orElseThrow(
                                                                   () -> EntityNotFoundExeption.of(Comment.class)));
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
    public NewsResponseDTO updatePatch(NewsRequestDTO newsRequestDTO, Long idNews) {
        News news = repository.findById(idNews)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));

        if (newsRequestDTO.getTitle() != null) {
            news.setTitle(newsRequestDTO.getTitle());
        }

        if (newsRequestDTO.getTextNews() != null) {
            news.setTextNews(newsRequestDTO.getTextNews());
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
    public void delete(Long idNews) {
        repository.findById(idNews)
                .orElseThrow(() -> EntityNotFoundExeption.of(Long.class));
        repository.deleteById(idNews);
    }

    /**
     * Запросы с подстановочными знаками
     *
     * @param predicate  Буква или набор букв из Title
     * @param pageNumber Номер страницы
     * @param pageSize   размером страницы
     * @return List<NewsResponseDTO>
     */
    @Override
    public List<NewsResponseDTO> findByAllNewsByPredicateTitle(String predicate, int pageNumber, int pageSize) {
        SearchSession searchSession = Search.session(entityManager);

        SearchPredicate userNamePredicate = searchSession.scope(News.class)
                .predicate()
                .wildcard()
                .field(TITLE)
                .matching("*" + predicate + "*")
                .toPredicate();

        return predicateOrphrase(searchSession, userNamePredicate, pageNumber, pageSize);
    }

    /**
     * Фразовые запросы
     *
     * @param phrase     Фраза из текста новости.
     * @param pageNumber Номер страницы
     * @param pageSize   размером страницы
     * @return List<NewsResponseDTO>
     */
    @Override
    public List<NewsResponseDTO> findByAllTextsByPhrase(String phrase, int pageNumber, int pageSize) {
        SearchSession searchSession = Search.session(entityManager);

        SearchPredicate predicate = searchSession.scope(News.class)
                .predicate()
                .phrase()
                .field(TEXT_NEWS)
                .matching(phrase)
                .slop(SLOP)
                .toPredicate();

        return predicateOrphrase(searchSession, predicate, pageNumber, pageSize);
    }

    private List<NewsResponseDTO> predicateOrphrase(SearchSession searchSession, SearchPredicate predicate,
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
