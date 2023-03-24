package ru.practicum.private_access.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDtoInput {

    @NotNull(groups = {Create.class})
    Long category;
    @Valid
    @NotNull(groups = {Create.class})
    LocationDto location;
    @NotBlank(groups = {Create.class})
    @Size(max = 2000, groups = {Create.class, Update.class})
    String annotation;
    @NotBlank(groups = {Create.class})
    @Size(max = 120, groups = {Create.class, Update.class})
    String title;
    @NotBlank(groups = {Create.class})
    @Size(max = 7000, groups = {Create.class, Update.class})
    String description;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @PositiveOrZero(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class})
    Integer participantLimit;
    @NotNull(groups = {Create.class})
    Boolean paid;
    @NotNull(groups = {Create.class})
    Boolean requestModeration;
}
