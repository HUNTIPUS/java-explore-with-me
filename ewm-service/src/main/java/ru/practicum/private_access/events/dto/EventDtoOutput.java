package ru.practicum.private_access.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.users.dto.UserShortDto;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.private_access.events.state.State;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoOutput {
    Long id;
    CategoryDto category;
    LocationDto location;
    String annotation;
    String title;
    String description;
    String createdOn;
    String eventDate;
    Integer participantLimit;
    Boolean paid;
    Boolean requestModeration;
    UserShortDto initiator;
    String publishedOn;
    Integer confirmedRequests;
    State state;
    Long views;
}
