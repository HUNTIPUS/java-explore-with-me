package ru.practicum.admin_access.categories.dto;

import lombok.*;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank(groups = {Create.class, Update.class})
    private String name;
}
