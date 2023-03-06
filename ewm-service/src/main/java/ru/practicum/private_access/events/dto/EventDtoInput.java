package ru.practicum.private_access.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.private_access.events.state.State;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoInput {
    @NotNull(groups = {Create.class, Update.class})
    Long category;
    @NotNull(groups = {Create.class, Update.class})
    LocationDto location;
    @NotBlank(groups = {Create.class, Update.class})
    String annotation;
    @NotBlank(groups = {Create.class, Update.class})
    String title;
    @NotBlank(groups = {Create.class, Update.class})
    String description;
    LocalDateTime createdOn = LocalDateTime.now().withNano(0);
    @NotNull(groups = {Create.class, Update.class})
    @Future(groups = {Create.class, Update.class})
    LocalDateTime eventDate;
    @PositiveOrZero(groups = {Create.class, Update.class})
    Integer participantLimit;
    @NotNull(groups = {Create.class, Update.class})
    Boolean paid;
    @NotNull(groups = {Create.class, Update.class})
    Boolean requestModeration;
    State state = State.PENDING;
}
