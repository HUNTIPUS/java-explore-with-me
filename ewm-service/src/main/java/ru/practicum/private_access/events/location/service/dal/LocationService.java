package ru.practicum.private_access.events.location.service.dal;

import ru.practicum.private_access.events.location.dto.LocationDto;

public interface LocationService {
    LocationDto create(LocationDto locationDto);
}
