package ru.clevertec.aspect;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;
import ru.clevertec.dto.responseDTO.NewsResponseDTO;
import ru.clevertec.exeption.EntityNotFoundExeption;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class CacheNewsAspect {

    private Cache<Long, NewsResponseDTO> cache;

    @PostConstruct
    void init() {
        log.info(cache.toString());
    }

    /**
     * Возвращает объект из кеша, если нет, то читает из базы.
     *
     * @param joinPoint Точка внедрения кода в метод NewsServiceImpl.findById()
     * @return NewsResponseDTO.
     * @throws EntityNotFoundExeption если не найден.
     */
    @Around("@annotation(ru.clevertec.annotation.MyFind)")
    public NewsResponseDTO aroundfindByID(ProceedingJoinPoint joinPoint) {
        log.info("Вошли в аспект GET !");
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        NewsResponseDTO responseDTO = cache.get(id);

        if (responseDTO != null) {
            log.info("Объект в кеше найден !");

            return responseDTO;

        } else {
            NewsResponseDTO newsResponseDTO = null;

            try {
                newsResponseDTO = (NewsResponseDTO) joinPoint.proceed();
                log.info("Объект взят из базы !");

            } catch (Throwable e) {
                throw EntityNotFoundExeption.of(Long.class);
            }
            cache.put(id, newsResponseDTO);

            return newsResponseDTO;
        }
    }

    /**
     * Создаёт объект News, NewsResponseDTO. И помещает в БД, а затем в кеш.
     *
     * @param joinPoint Точка внедрения кода в метод NewsServiceImpl.create()
     * @return Новый NewsResponseDTO.
     */
    @AfterReturning(value = "@annotation(ru.clevertec.annotation.MyCreate)", returning = "result")
    public NewsResponseDTO afterCreate(JoinPoint joinPoint, Object result) {
        log.info("Вошли в аспект POST !");

        cache.put(((NewsResponseDTO) result).getId(), (NewsResponseDTO) result);
        log.info("Создание объекта !");

        return (NewsResponseDTO) result;
    }

    /**
     * Обновляет объект в кеше.
     *
     * @param joinPoint Точка внедрения кода в метод NewsServiceImpl.update()
     */
    @AfterReturning(value = "@annotation(ru.clevertec.annotation.MyUpdate)", returning = "result")
    public void updateAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Вошли в аспект PUT !");
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[1];

        if (cache.get(id) != null) {

            NewsResponseDTO newsResponseDTO = cache.get(id);
            newsResponseDTO.setTitle(((NewsResponseDTO) result).getTitle());
            newsResponseDTO.setTextNews(((NewsResponseDTO) result).getTextNews());

            log.info("Обновление объекта !");

        } else {
            cache.put(((NewsResponseDTO) result).getId(), (NewsResponseDTO) result);
            log.info("Добавление объекта в cache !");
        }
    }

    /**
     * Удаление объекта из кеша.
     *
     * @param joinPoint Точка внедрения кода в метод News.delete()
     */
    @AfterReturning("@annotation(ru.clevertec.annotation.MyDelete)")
    public void deleteAfterReturning(JoinPoint joinPoint) {
        log.info("Вошли в аспект DELETE !");
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        cache.remove(id);
        log.info("Удаление объекта !");
    }
}
