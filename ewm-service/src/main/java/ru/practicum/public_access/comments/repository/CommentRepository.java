package ru.practicum.public_access.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.public_access.comments.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
