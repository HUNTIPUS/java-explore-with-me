package ru.practicum.public_access.comments.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.exceptions.exception.InvalidRequestException;
import ru.practicum.exceptions.exception.ObjectExistenceException;
import ru.practicum.exceptions.exception.TimeException;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.public_access.comments.dto.CommentDtoInput;
import ru.practicum.public_access.comments.dto.CommentDtoOutput;
import ru.practicum.public_access.comments.mapper.CommentMapper;
import ru.practicum.public_access.comments.model.Comment;
import ru.practicum.public_access.comments.repository.CommentRepository;
import ru.practicum.public_access.comments.service.dal.CommentService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final UserService userService;
    private final EventService eventService;

    @Override
    public CommentDtoOutput create(Long userId, Long eventId, CommentDtoInput commentDtoInput) {
        Comment comment = CommentMapper.toComment(commentDtoInput);
        comment.setUser(userService.getById(userId));
        comment.setEvent(eventService.getById(eventId));
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(repository.save(comment));
    }

    @Override
    public CommentDtoOutput update(Long userId, Long eventId, Long id, CommentDtoInput commentDtoInput) {
        Comment comment = getById(id);
        if (isCreator(comment, userId, eventId)) {
            if (comment.getCreated().plusDays(1).isAfter(LocalDateTime.now())) {
                comment.setDescription(commentDtoInput.getDescription());
            } else {
                throw new TimeException(String.format("Comment with id=%s for more than 24 hours", id));
            }
        }
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public void delete(Long userId, Long eventId, Long id) {
        if (isCreator(getById(id), userId, eventId)) {
            repository.deleteById(id);
        }
    }

    @Override
    public void deleteByAdmin(Long id) {
        getById(id);
        repository.deleteById(id);
    }

    @Override
    public Comment getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectExistenceException(String.format("Comment with id=%s does not exist",
                        id)));
    }

    private Boolean isCreator(Comment comment, Long userId, Long eventId) {
        if (!comment.getUser().equals(userService.getById(userId))
                || !comment.getEvent().equals(eventService.getById(eventId))) {
            throw new InvalidRequestException(String.format("User with id=%s did not leave a comment with id=%s " +
                            "under the event with id=%s",
                    userId, comment.getId(), eventId));
        }
        return true;
    }
}
