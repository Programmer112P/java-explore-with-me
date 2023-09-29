package ru.practicum.mainservice.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.comments.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
