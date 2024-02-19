package ru.clevertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {

}
