package ru.practicum.private_access.events.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.valid.Create;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LocationDto {
    @NotNull(groups = {Create.class})
    private Float lat;
    @NotNull(groups = {Create.class})
    private Float lon;
}
