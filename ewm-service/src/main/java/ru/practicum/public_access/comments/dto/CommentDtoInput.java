package ru.practicum.public_access.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoInput {

    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 7000, groups = {Create.class, Update.class})
    private String description;
}
