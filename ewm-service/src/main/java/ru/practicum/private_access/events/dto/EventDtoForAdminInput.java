package ru.practicum.private_access.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.valid.Update;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoForAdminInput {
    Long category;
    LocationDto location;
    String annotation;
    String title;
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @PositiveOrZero(groups = {Update.class})
    Integer participantLimit;
    Boolean paid;
    Boolean requestModeration;
    StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
