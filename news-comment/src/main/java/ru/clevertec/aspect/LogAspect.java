package ru.clevertec.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LogAspect {

    /**
     * Будет выполнен после успешного возврата значения из методов контроллера, помеченных аннотацией @MyLogController.
     *
     * @param joinPoint точка соединения, представляющая вызов метода в контроллере.
     * @param result    возвращаемое значение из метода контроллера.
     */
    @AfterReturning(value = "@annotation(ru.clevertec.annotation.MyLogController)", returning = "result")
    public void logging(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String parameters = Arrays.toString(joinPoint.getArgs());

        log.info("Method: {}.{} - {} {}", className, methodName, httpMethod, requestURI);
        log.info("Parameters: {}", parameters);
        log.info("Result: {}", result);
    }
}
