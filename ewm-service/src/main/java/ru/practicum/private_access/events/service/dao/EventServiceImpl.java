package ru.practicum.private_access.events.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.repository.EventRepository;
import ru.practicum.private_access.events.service.dal.EventService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final UserService service;

    @Transactional
    @Override
    public Event create(Long userId, Event event) {
        service.getById(userId);
        return repository.save(event);
    }

    @Transactional
    @Override
    public Event update(Long userId, Long eventId, Event newEvent) {
        service.getById(userId);
        return updateEvent(getById(eventId), newEvent);
    }

    @Override
    public List<Event> getAll(Long userId, Integer from, Integer size) {
        service.getById(userId);
        return repository.getAllByInitiatorId(userId,
                PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(DESC, "created_on")));
    }

    @Override
    public Event getForInitiator(Long userId, Long eventId) {
        return repository.getByInitiatorId(userId, eventId);
    }

    @Override
    public Event getById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ObjenesisException(String.format("Event with id=%s was not found", id)));
    }

    private Event updateEvent(Event event, Event newEvent) {
        if (newEvent.getAnnotation() != null && !newEvent.getAnnotation().isBlank()) {
            event.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getTitle() != null && !newEvent.getTitle().isBlank()) {
            event.setTitle(newEvent.getTitle());
        }
        if (newEvent.getDescription() != null && !newEvent.getDescription().isBlank()) {
            event.setDescription(newEvent.getDescription());
        }
        if (newEvent.getEventDate() != null) {
            event.setEventDate(newEvent.getEventDate());
        }
        if (newEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getPaid() != null) {
            event.setPaid(newEvent.getPaid());
        }
        if (newEvent.getRequestModeration() != null) {
            event.setRequestModeration(newEvent.getRequestModeration());
        }
        if (newEvent.getLocation() != null) {
            event.setLocation(newEvent.getLocation());
        }
        if (newEvent.getCategory() != null) {
            event.setCategory(newEvent.getCategory());
        }
        if (newEvent.getCompilation() != null) {
            event.setCompilation(newEvent.getCompilation());
        }
        return event;
    }
}
