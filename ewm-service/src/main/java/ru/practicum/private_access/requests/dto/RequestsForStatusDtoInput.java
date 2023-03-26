package ru.practicum.private_access.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.valid.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestsForStatusDtoInput {
    @NotEmpty(groups = Update.class)
    private List<Long> requestIds;
    @NotNull(groups = Update.class)
    private Status status;

    public enum Status {
        CONFIRMED,
        REJECTED
    }
}
