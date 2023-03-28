package ru.practicum.public_access.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.public_access.comments.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
