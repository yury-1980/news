package ru.clevertec.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.clevertec.entity.News;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"textNews"})
    Optional<News> findById(@NonNull Long idNews);
}
