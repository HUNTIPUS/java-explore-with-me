package ru.practicum.private_access.events.service.dal;

import ru.practicum.private_access.events.model.Event;

import java.util.List;

public interface EventService {
    Event create(Long userId, Event event);
    Event update(Long userId, Long eventId, Event event);
    List<Event> getAll(Long userId, Integer from, Integer size);
    Event getForInitiator(Long userId, Long eventId);
    Event getById(Long id);
}
