package ru.practicum.admin_access.compilations.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDtoOutput {
    Long id;
    String title;
    Boolean pinned;
    List<EventShortDtoOutput> events;
}
