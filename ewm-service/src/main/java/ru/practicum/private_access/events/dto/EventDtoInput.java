package ru.practicum.private_access.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.private_access.events.state.State;
import ru.practicum.valid.Create;

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
    @NotNull(groups = {Create.class})
    Long category;
    @NotNull(groups = {Create.class})
    LocationDto location;
    @NotBlank(groups = {Create.class})
    String annotation;
    @NotBlank(groups = {Create.class})
    String title;
    @NotBlank(groups = {Create.class})
    String description;
    LocalDateTime createdOn = LocalDateTime.now().withNano(0);
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @PositiveOrZero(groups = {Create.class})
    Integer participantLimit;
    Boolean paid;
    Boolean requestModeration;
    State state = State.PENDING;
    String stateAction;
}
