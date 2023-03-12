package ru.practicum.private_access.events.service.dal;

import ru.practicum.private_access.events.dto.EventDtoForAdminInput;
import ru.practicum.private_access.events.dto.EventDtoInput;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.state.State;
import ru.practicum.public_access.events.sort.Sort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDtoOutput create(Long userId, EventDtoInput eventDtoInput);

    EventDtoOutput update(Long userId, Long eventId, EventDtoInput eventDtoInput);

    List<EventShortDtoOutput> getAll(Long userId, Integer from, Integer size);

    EventDtoOutput getByIdForInitiator(Long userId, Long eventId);

    Event getById(Long id);

    EventDtoOutput updateByAdmin(Long id, EventDtoForAdminInput eventDto);

    List<EventDtoOutput> getAllByParamForAdmin(List<Long> user, List<State> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Integer from, Integer size);

    EventDtoOutput getByIdForDto(Long id, HttpServletRequest request);

    List<EventShortDtoOutput> getAllByParamForPublic(String text, List<Long> categories, Boolean paid,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     Boolean onlyAvailable, Sort sort, Integer from, Integer size,
                                                     HttpServletRequest request);

}
