package ru.clevertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
