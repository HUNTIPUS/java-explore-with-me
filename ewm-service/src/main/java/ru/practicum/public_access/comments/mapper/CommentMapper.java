package ru.practicum.public_access.comments.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.admin_access.users.mapper.UserMapper;
import ru.practicum.public_access.comments.dto.CommentDtoInput;
import ru.practicum.public_access.comments.dto.CommentDtoOutput;
import ru.practicum.public_access.comments.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static Comment toComment(CommentDtoInput commentDtoInput) {
        Comment comment = new Comment();
        comment.setDescription(commentDtoInput.getDescription());
        return comment;
    }

    public static CommentDtoOutput toCommentDto(Comment comment) {
        return CommentDtoOutput
                .builder()
                .id(comment.getId())
                .creator(UserMapper.toUserShortDto(comment.getUser()))
                .description(comment.getDescription())
                .build();
    }

    public static List<CommentDtoOutput> toCommentDtoOutputList(List<Comment> comments) {
        return comments
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
