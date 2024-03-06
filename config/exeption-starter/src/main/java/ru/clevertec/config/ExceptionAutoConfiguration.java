package ru.clevertec.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exception.ExceptionHandler;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "exception", name = "enabled", havingValue = "true")
public class ExceptionAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("Инициализация ExceptionAutoConfiguration");
    }

    @Bean
    public ExceptionHandler exceptionHandler() {
        return new ExceptionHandler();
    }
}