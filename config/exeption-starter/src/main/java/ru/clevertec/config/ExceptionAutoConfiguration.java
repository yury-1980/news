package ru.clevertec.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exception.MyExceptionHandler;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "exception", name = "enabled", havingValue = "true")
public class ExceptionAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("Инициализация ExceptionAutoConfiguration");
    }

    @Bean
    public MyExceptionHandler exceptionHandler() {
        return new MyExceptionHandler();
    }
}
