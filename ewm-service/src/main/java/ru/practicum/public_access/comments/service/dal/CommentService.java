package ru.practicum.public_access.comments.service.dal;

import ru.practicum.public_access.comments.dto.CommentDtoInput;
import ru.practicum.public_access.comments.dto.CommentDtoOutput;
import ru.practicum.public_access.comments.model.Comment;

public interface CommentService {

    CommentDtoOutput create(Long userId, Long eventId, CommentDtoInput commentDtoInput);

    CommentDtoOutput update(Long userId, Long eventId, Long id, CommentDtoInput commentDtoInput);

    void delete(Long userId, Long eventId, Long id);

    void deleteByAdmin(Long id);

    Comment getById(Long id);
}
