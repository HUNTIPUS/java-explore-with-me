package ru.practicum.public_access.comments.dto;

import lombok.*;
import ru.practicum.admin_access.users.dto.UserShortDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDtoOutput {

    private Long id;
    private UserShortDto creator;
    private String description;
}
