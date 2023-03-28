package ru.practicum.public_access.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.public_access.comments.dto.CommentDtoInput;
import ru.practicum.public_access.comments.dto.CommentDtoOutput;
import ru.practicum.public_access.comments.service.dal.CommentService;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoOutput create(@Positive @PathVariable Long userId,
                                   @Positive @PathVariable Long eventId,
                                   @RequestBody @Validated(Create.class) CommentDtoInput commentDtoInput) {
        log.info("user with id={} create comment under the event with id={}", userId, eventId);
        return commentService.create(userId, eventId, commentDtoInput);
    }

    @PatchMapping("/{id}")
    public CommentDtoOutput update(@Positive @PathVariable Long userId,
                                   @Positive @PathVariable Long eventId,
                                   @Positive @PathVariable Long id,
                                   @RequestBody @Validated(Update.class) CommentDtoInput commentDtoInput) {
        log.info("user with id={} update comment with id ={} under the event with id={}", userId, id, eventId);
        return commentService.update(userId, eventId, id, commentDtoInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable Long userId,
                       @Positive @PathVariable Long eventId,
                       @Positive @PathVariable Long id) {
        commentService.delete(userId, eventId, id);
        log.info("user with id={} delete comment with id ={} under the event with id={}", userId, id, eventId);
    }
}
