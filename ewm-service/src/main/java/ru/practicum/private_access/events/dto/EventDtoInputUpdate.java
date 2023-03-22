package ru.practicum.private_access.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.valid.Update;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoInputUpdate extends EventDtoInput {
    @Size(max = 2000, groups = {Update.class})
    String annotation;
    @Size(max = 120, groups = {Update.class})
    String title;
    @Size(max = 7000, groups = {Update.class})
    String description;
    String stateAction;
}
