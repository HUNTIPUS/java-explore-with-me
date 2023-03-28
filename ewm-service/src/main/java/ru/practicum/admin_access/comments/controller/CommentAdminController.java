package ru.practicum.admin_access.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.public_access.comments.dto.CommentDtoOutput;
import ru.practicum.public_access.comments.mapper.CommentMapper;
import ru.practicum.public_access.comments.service.dal.CommentService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/admin/comments/{id}")
public class CommentAdminController {

    private final CommentService commentService;

    @GetMapping
    public CommentDtoOutput getById(@Positive @PathVariable Long id) {
        return CommentMapper.toCommentDto(commentService.getById(id));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Positive @PathVariable Long id) {
        commentService.deleteByAdmin(id);
        log.info("delete comment with id ={}", id);
    }
}
