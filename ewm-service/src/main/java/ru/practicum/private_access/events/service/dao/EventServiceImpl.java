package ru.practicum.private_access.events.service.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.admin_access.categories.service.dal.CategoryService;
import ru.practicum.admin_access.events.state_action.StateAction;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;
import ru.practicum.exceptions.exception.DuplicateException;
import ru.practicum.exceptions.exception.StatusException;
import ru.practicum.exceptions.exception.TimeException;
import ru.practicum.private_access.events.dto.EventDtoForAdminInput;
import ru.practicum.private_access.events.dto.EventDtoInput;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.location.service.dal.LocationService;
import ru.practicum.private_access.events.mapper.EventMapper;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.repository.EventRepository;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.private_access.events.state.State;
import ru.practicum.private_access.requests.model.Request;
import ru.practicum.private_access.requests.repository.RequestRepository;
import ru.practicum.public_access.events.sort.Sort;
import ru.practicum.service.dal.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {

    EventRepository repository;
    UserService userService;
    CategoryService categoryService;
    RequestRepository requestRepository;
    StatsService statsService;

    LocationService locationService;

    public static final String APP = "ewm-service";

    @Transactional
    @Override
    public EventDtoOutput create(Long userId, EventDtoInput eventDtoInput) {
        if (!eventDtoInput.getEventDate().isAfter(LocalDateTime.now())) {
            throw new TimeException("Event date not in the future.");
        }
        locationService.create(eventDtoInput.getLocation());
        return EventMapper.toEventDtoOutput(repository.save(EventMapper.toEvent(eventDtoInput,
                userService.getById(userId),
                categoryService.getById(eventDtoInput.getCategory()))));
    }

    @Transactional
    @Override
    public EventDtoOutput update(Long userId, Long eventId, EventDtoInput eventDtoInput) {
        Event event = getById(eventId);
        User user = userService.getById(userId);
        if (eventDtoInput.getEventDate() != null && !eventDtoInput.getEventDate().isAfter(LocalDateTime.now())) {
            throw new TimeException("Event date not in the future.");
        }
        if (eventDtoInput.getStateAction() != null
                && eventDtoInput.getStateAction().equals(StateAction.SEND_TO_REVIEW.name())) {
            event.setState(State.PENDING);
        }
        if (eventDtoInput.getStateAction() == null && event.getState().equals(State.PUBLISHED)) {
            throw new StatusException(String.format("Event has state %s", event.getState()));
        }
        if (eventDtoInput.getLocation() != null) {
            locationService.create(eventDtoInput.getLocation());
        }
        if (eventDtoInput.getCategory() == null) {
            return EventMapper.toEventDtoOutput(updateEvent(event, EventMapper.toEvent(eventDtoInput,
                    user, null)));
        }
        return EventMapper.toEventDtoOutput(updateEvent(event, EventMapper.toEvent(eventDtoInput,
                user,
                categoryService.getById(eventDtoInput.getCategory()))));
    }

    @Override
    public List<EventShortDtoOutput> getAll(Long userId, Integer from, Integer size) {
        userService.getById(userId);
        List<Event> events = repository.getAllByInitiatorId(userId,
                PageRequest.of(from > 0 ? from / size : 0, size));
        return getEventShortDtoOutput(events);
    }

    @Override
    public EventDtoOutput getByIdForInitiator(Long userId, Long eventId) {
        List<Event> events = repository.getByInitiatorId(userId, eventId);
        if (events.isEmpty()) {
            throw new ObjenesisException(String.format("Event with id=%s was not found", eventId));
        } else {
            List<String> uris = new ArrayList<>();
            uris.add(String.format("/events/%s", events.get(0).getId()));
            Map<String, Long> views = getView(uris);
            return appendViewsForLongDto(appendCountConfirmedRequestsToLongDto(EventMapper
                            .toEventDtoOutput(events.get(0)),
                    Objects.requireNonNullElse(getCountConfirmedRequestsForEvent(events).get(events.get(0)),
                            0L)), Objects.requireNonNullElse(views.get(String.format("/events/%s",
                    events.get(0).getId())), 0L));
        }
    }

    @Override
    public Event getById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ObjenesisException(String.format("Event with id=%s was not found", id)));
    }

    @Transactional
    @Override
    public EventDtoOutput updateByAdmin(Long id, EventDtoForAdminInput eventDto) {
        Event event = getById(id);
        if (eventDto.getEventDate() != null && !eventDto.getEventDate().isAfter(LocalDateTime.now())) {
            throw new TimeException("Event date not in the future.");
        }
        if (eventDto.getLocation() != null) {
            locationService.create(eventDto.getLocation());
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new StatusException(String.format("event with id=%s has status %s", id, event.getState()));
        }
        if (eventDto.getCategory() == null) {
            return EventMapper.toEventDtoOutput(updateEvent(event, EventMapper.toEventAdmin(eventDto,
                    null)));
        }
        return EventMapper.toEventDtoOutput(updateEvent(event, EventMapper.toEventAdmin(eventDto,
                categoryService.getById(eventDto.getCategory()))));
    }

    @Override
    public List<EventDtoOutput> getAllByParamForAdmin(List<Long> users, List<String> states,
                                                      List<Long> categories, LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd, Integer from, Integer size) {
        if (users == null) {
            users = new ArrayList<>(userService.getAll().stream().collect(groupingBy(User::getId)).keySet());
        }
        List<State> statesNew = new ArrayList<>();
        if (states == null || states.isEmpty()) {
            statesNew.add(State.PUBLISHED);
            statesNew.add(State.PENDING);
            statesNew.add(State.CANCELED);
        } else {
            for (String state : states) {
                statesNew.add(State.valueOf(state));
            }
        }
        if (categories == null) {
            categories = getIdCategories();
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().withNano(0);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().withNano(0).plusYears(10);
        }
        List<EventDtoOutput> eventDtoOutputList = new ArrayList<>();
        List<Event> events = repository.getAllByParam(users, statesNew, categories, rangeStart, rangeEnd,
                PageRequest.of(from > 0 ? from / size : 0, size));
        Map<Event, Long> confirmedRequests = getCountConfirmedRequestsForEvent(events);
        List<String> uris = new ArrayList<>();
        for (Event event : events) {
            uris.add(String.format("/events/%s", event.getId()));
        }
        Map<String, Long> views = getView(uris);
        for (Event event : events) {
            eventDtoOutputList.add(appendViewsForLongDto(appendCountConfirmedRequestsToLongDto(EventMapper
                    .toEventDtoOutput(event), Objects.requireNonNullElse(confirmedRequests.get(event),
                    0L)), Objects.requireNonNullElse(views.get(String.format("/events/%s",
                    event.getId())), 0L)));
        }
        return eventDtoOutputList;
    }

    @Transactional
    @Override
    public EventDtoOutput getByIdForDto(Long id, HttpServletRequest request) {
        Event event = repository.getByIdWithStatePublished(id)
                .orElseThrow(() -> new ObjenesisException(String.format("Event with id=%s not available", id)));
        String uri = request.getRequestURI();
        StatsDtoInput statsDtoInput = new StatsDtoInput(APP, uri, request.getRemoteAddr(),
                LocalDateTime.now().withNano(0));
        statsService.hit(statsDtoInput);
        List<String> uris = new ArrayList<>();
        uris.add(uri);
        List<Event> events = new ArrayList<>();
        events.add(event);
        Map<String, Long> views = getView(uris);
        Long confirmedRequests = getCountConfirmedRequestsForEvent(events).get(event);
        return appendViewsForLongDto(appendCountConfirmedRequestsToLongDto(EventMapper.toEventDtoOutput(event),
                Objects.requireNonNullElse(confirmedRequests, 0L)), views.get(String.format("/events/%s",
                event.getId())));
    }

    @Transactional
    @Override
    public List<EventShortDtoOutput> getAllByParamForPublic(String text, List<Long> categories, Boolean paid,
                                                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                            Boolean onlyAvailable, Sort sort,
                                                            Integer from, Integer size, HttpServletRequest request) {
        if (categories == null) {
            categories = getIdCategories();
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().withNano(0);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(10);
        }

        List<Event> events;
        if (text == null || text.isBlank()) {
            if (onlyAvailable) {
                events = repository.getEventsWithoutTextWithAvailable(categories, paid, rangeStart,
                        rangeEnd, PageRequest.of(from > 0 ? from / size : 0, size,
                                org.springframework.data.domain
                                        .Sort.by(org.springframework.data.domain
                                                .Sort.Direction.DESC, sort.name().toLowerCase())));
            } else {
                events = repository.getEventsWithoutTextWithoutAvailable(categories, paid, rangeStart,
                        rangeEnd, PageRequest.of(from > 0 ? from / size : 0, size));
            }
        } else {
            if (onlyAvailable) {
                events = repository.getEventsByTextWithAvailable(text, categories, paid, rangeStart,
                        rangeEnd, PageRequest.of(from > 0 ? from / size : 0, size,
                                org.springframework.data.domain
                                        .Sort.by(org.springframework.data.domain
                                                .Sort.Direction.DESC, sort.name().toLowerCase())));
            } else {
                events = repository.getEventsByTextWithoutAvailable(text, categories, paid, rangeStart,
                        rangeEnd, PageRequest.of(from > 0 ? from / size : 0, size));
            }
        }
        for (Event event : events) {
            String uri = String.format("/events/%s", event.getId());
            statsService.hit(new StatsDtoInput(APP, uri, request.getRemoteAddr(),
                    LocalDateTime.now().withNano(0)));
        }
        return getEventShortDtoOutput(events);
    }

    @Override
    public List<EventShortDtoOutput> getEventShortDtoOutput(List<Event> events) {
        List<EventShortDtoOutput> eventShortDtoOutputList = new ArrayList<>();
        List<String> uris = new ArrayList<>();
        Map<Event, Long> confirmedRequests = getCountConfirmedRequestsForEvent(events);
        for (Event event : events) {
            uris.add(String.format("/events/%s", event.getId()));
        }
        Map<String, Long> views = getView(uris);
        for (Event event : events) {
            eventShortDtoOutputList.add(appendViewsForShortDto(appendCountConfirmedRequestsToShortDto(EventMapper
                                    .toEventShortDtoOutput(event),
                            Objects.requireNonNullElse(confirmedRequests.get(event), 0L)),
                    Objects.requireNonNullElse(views.get(String.format("/events/%s", event.getId())), 0L)));
        }
        return eventShortDtoOutputList;
    }

    private Map<String, Long> getView(List<String> uris) {
        List<StatsDtoOutput> stats = statsService.getStats(LocalDateTime.MIN,
                LocalDateTime.now().withNano(0), uris, false);
        return stats.stream().collect(groupingBy(StatsDtoOutput::getUri, counting()));
    }

    private List<Long> getIdCategories() {
        return new ArrayList<>(categoryService.getAll().stream()
                .collect(groupingBy(Category::getId)).keySet());
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
        if (newEvent.getState() != null) {
            if (event.getState().equals(State.PUBLISHED) && newEvent.getState().equals(State.PUBLISHED)
                    || event.getState().equals(State.CANCELED) && newEvent.getState().equals(State.CANCELED)) {
                throw new DuplicateException(String.format("Status: %s already in use event with id=%s",
                        newEvent.getState(), event.getId()));
            }
            event.setState(newEvent.getState());
        }
        if (newEvent.getPublishedOn() != null) {
            event.setPublishedOn(newEvent.getPublishedOn());
        }
        return event;
    }

    private Map<Event, Long> getCountConfirmedRequestsForEvent(List<Event> events) {
        return requestRepository.getConfirmedRequests(events)
                .stream()
                .collect(groupingBy(Request::getEvent, counting()));
    }

    private EventShortDtoOutput appendCountConfirmedRequestsToShortDto(EventShortDtoOutput eventDtoOutput,
                                                                       Long confirmedRequests) {
        eventDtoOutput.setConfirmedRequests(confirmedRequests.intValue());
        return eventDtoOutput;
    }

    private EventDtoOutput appendCountConfirmedRequestsToLongDto(EventDtoOutput eventDtoOutput,
                                                                 Long confirmedRequests) {
        eventDtoOutput.setConfirmedRequests(confirmedRequests.intValue());
        return eventDtoOutput;
    }

    private EventShortDtoOutput appendViewsForShortDto(EventShortDtoOutput eventDtoOutput, Long views) {
        eventDtoOutput.setViews(views);
        return eventDtoOutput;
    }

    private EventDtoOutput appendViewsForLongDto(EventDtoOutput eventDtoOutput, Long views) {
        eventDtoOutput.setViews(views);
        return eventDtoOutput;
    }
}
