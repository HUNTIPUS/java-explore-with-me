package ru.practicum.private_access.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestsForStatusDtoInput {
    @NotEmpty(groups = Update.class)
    private List<Long> requestIds;
    @NotBlank(groups = Update.class)
    private String status;
}
