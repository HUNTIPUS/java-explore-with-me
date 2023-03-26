package ru.practicum.private_access.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.users.dto.UserShortDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDtoOutput {
    Long id;
    CategoryDto category;
    String annotation;
    String title;
    String eventDate;
    Boolean paid;
    UserShortDto initiator;
    Integer confirmedRequests;
    Long views;
}
