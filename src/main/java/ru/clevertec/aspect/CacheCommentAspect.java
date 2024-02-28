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
import ru.clevertec.dto.responseDTO.CommentResponseDTO;
import ru.clevertec.exeption.EntityNotFoundExeption;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class CacheCommentAspect {

    private Cache<Long, CommentResponseDTO> cache;

    @PostConstruct
    void init() {
        log.info(cache.toString());
    }

    /**
     * Возвращает объект из кеша, если нет, то читает из базы.
     *
     * @param joinPoint Точка внедрения кода в метод CommentServiceImpl.findById()
     * @return CommentResponseDTO.
     * @throws EntityNotFoundExeption если не найден.
     */
    @Around("execution(* ru.clevertec.service.impl.CommentServiceImpl.findById(..))")
    public CommentResponseDTO aroundfindByUUID(ProceedingJoinPoint joinPoint) {
        log.info("Вошли в аспект GET !");
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        CommentResponseDTO responseDTO = cache.get(id);

        if (responseDTO != null) {
            log.info("Объект в кеше найден !");

            return responseDTO;

        } else {
            CommentResponseDTO commentResponseDTO = null;

            try {
                commentResponseDTO = (CommentResponseDTO) joinPoint.proceed();
                log.info("Объект взят из базы !");

            } catch (Throwable e) {
                throw EntityNotFoundExeption.of(Long.class);
            }
            cache.put(id, commentResponseDTO);

            return commentResponseDTO;
        }
    }

    /**
     * Создаёт объект Person из объекта RequestPersonDTO. И помещает в БД, а затем в кеш.
     *
     * @param joinPoint Точка внедрения кода в метод CommentServiceImpl.create()
     * @return Новый CommentResponseDTO.
     */
    @AfterReturning(value = "execution(* ru.clevertec.service.impl.CommentServiceImpl.create(..))", returning = "result")
    public CommentResponseDTO afterCreate(JoinPoint joinPoint, Object result) {
        log.info("Вошли в аспект POST !");

        cache.put(((CommentResponseDTO) result).getId(), (CommentResponseDTO) result);
        log.info("Создание объекта !");

        return (CommentResponseDTO) result;
    }

    /**
     * Обновляет объект в кеше.
     *
     * @param joinPoint Точка внедрения кода в метод CommentServiceImpl.updatePatch()
     */
    @AfterReturning(value = "execution(* ru.clevertec.service.impl.CommentServiceImpl.updatePatch(..))", returning = "result")
    public void updateAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Вошли в аспект PUT !");
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[1];

        if (cache.get(id) != null) {

            CommentResponseDTO commentResponseDTO = cache.get(id);
            commentResponseDTO.setTextComment(((CommentResponseDTO) result).getTextComment());

            log.info("Обновление объекта !");

        } else {
            cache.put(((CommentResponseDTO) result).getId(), (CommentResponseDTO) result);
            log.info("Добавление объекта в cache !");
        }
    }

    /**
     * Удаление объекта из кеша.
     *
     * @param joinPoint Точка внедрения кода в метод CommentServiceImpl.delete()
     */
    @AfterReturning(value = "execution(* ru.clevertec.service.impl.CommentServiceImpl.delete(..))")
    public void deleteAfterReturning(JoinPoint joinPoint) {
        log.info("Вошли в аспект DELETE !");
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        cache.remove(id);
        log.info("Удаление объекта !");
    }
}
