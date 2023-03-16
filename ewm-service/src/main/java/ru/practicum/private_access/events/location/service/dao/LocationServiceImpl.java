package ru.practicum.private_access.events.location.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.private_access.events.location.dto.LocationDto;
import ru.practicum.private_access.events.location.mapper.LocationMapper;
import ru.practicum.private_access.events.location.repository.LocationRepository;
import ru.practicum.private_access.events.location.service.dal.LocationService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    @Override
    public LocationDto create(LocationDto locationDto) {
        if (locationRepository.getByLatAndLon(locationDto.getLat(), locationDto.getLon()) == null) {
            return LocationMapper.toLocationDto(locationRepository
                    .saveAndFlush(LocationMapper.toLocation(locationDto)));
        }
        return locationDto;
    }
}
