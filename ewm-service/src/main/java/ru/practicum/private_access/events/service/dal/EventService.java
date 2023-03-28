package ru.practicum.private_access.events.service.dal;

import ru.practicum.private_access.events.dto.*;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.public_access.events.sort.Sort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDtoOutput create(Long userId, EventDtoInput eventDtoInput);

    EventDtoOutput update(Long userId, Long eventId, EventDtoInputUpdate eventDtoInput);

    List<EventShortDtoOutput> getAll(Long userId, Integer from, Integer size);

    EventDtoOutput getByIdForInitiator(Long userId, Long eventId);

    Event getById(Long id);

    EventDtoOutputForAdmin updateByAdmin(Long id, EventDtoForAdminInput eventDto);

    List<EventDtoOutputForAdmin> getAllByParamForAdmin(List<Long> user, List<String> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Integer from, Integer size);

    EventDtoOutput getByIdForDto(Long id, HttpServletRequest request);

    List<EventShortDtoOutput> getAllByParamForPublic(String text, List<Long> categories, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable, Sort sort, Integer from, Integer size,
                                                     HttpServletRequest request);

    List<EventShortDtoOutput> getEventShortDtoOutput(List<Event> events);

}
