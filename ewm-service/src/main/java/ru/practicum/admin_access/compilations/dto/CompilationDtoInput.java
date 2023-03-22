package ru.practicum.admin_access.compilations.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoInput {
    @Size(min = 3, max = 120, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class})
    String title;
    @NotNull(groups = {Create.class})
    Boolean pinned;
    List<Long> events;
}
