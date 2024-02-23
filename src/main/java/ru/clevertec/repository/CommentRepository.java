package ru.clevertec.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.clevertec.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"textComment"})
    Optional<Comment> findById(@NonNull Long idComment);

    List<Comment> findCommentsByNews_Id(Long idNews, Pageable pageable);

    Optional<Comment> findCommentByIdAndNews_Id(Long idNews, Long idComment);
}
