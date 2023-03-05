package ru.practicum.admin_access.users.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotBlank
    private String name;
}
