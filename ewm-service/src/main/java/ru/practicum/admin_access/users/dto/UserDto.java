package ru.practicum.admin_access.users.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.valid.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @Email(groups = {Create.class})
    @NotNull(groups = {Create.class})
    String email;
    @NotBlank(groups = {Create.class})
    String name;
}
