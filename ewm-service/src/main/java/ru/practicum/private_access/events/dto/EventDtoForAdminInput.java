package ru.practicum.private_access.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.valid.Update;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
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
    @Future(groups = {Update.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @PositiveOrZero(groups = {Update.class})
    Integer participantLimit;
    Boolean paid;
    Boolean requestModeration;
    @NotBlank(groups = {Update.class})
    String stateAction;
    LocalDateTime publishedOn = LocalDateTime.now().withNano(0);
}
