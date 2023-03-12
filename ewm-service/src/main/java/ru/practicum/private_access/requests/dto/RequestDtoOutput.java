package ru.practicum.private_access.requests.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.requests.Status.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RequestDtoOutput {
    Long id;
    Long event;
    Long requester;
    Status status;
    LocalDateTime created;
}
