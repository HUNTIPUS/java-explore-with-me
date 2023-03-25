package ru.practicum.private_access.events.service.dao;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.categories.service.dal.CategoryService;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.exceptions.exception.DuplicateException;
import ru.practicum.exceptions.exception.StatusException;
import ru.practicum.exceptions.exception.TimeException;
import ru.practicum.private_access.events.dto.*;
import ru.practicum.private_access.events.location.service.dal.LocationService;
import ru.practicum.private_access.events.mapper.EventMapper;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.model.QEvent;
import ru.practicum.private_access.events.repository.EventRepository;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.private_access.events.state.State;
import ru.practicum.private_access.requests.model.QRequest;
import ru.practicum.private_access.requests.model.Request;
import ru.practicum.private_access.requests.repository.RequestRepository;
import ru.practicum.public_access.events.sort.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    LocationService locationService;
    StatsClient client;
    @PersistenceContext
    EntityManager entityManager;

    public static final String APP = "ewm-main-service";

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
    public EventDtoOutput update(Long userId, Long eventId, EventDtoInputUpdate eventDtoInput) {
        Event event = getById(eventId);
        User user = userService.getById(userId);
        if (eventDtoInput.getEventDate() != null && !eventDtoInput.getEventDate().isAfter(LocalDateTime.now())) {
            throw new TimeException("Event date not in the future.");
        }
        if (eventDtoInput.getStateAction() != null
                && eventDtoInput.getStateAction().equals(EventDtoInputUpdate.StateAction.SEND_TO_REVIEW)) {
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

        JPAQuery<Event> query = new JPAQuery<>(entityManager);
        QEvent qEvent = QEvent.event;
        List<Event> events = query.from(qEvent)
                .where(
                        isUsersNotEmpty(users),
                        isCategoriesNotEmpty(categories),
                        isRangeStartNotNull(rangeStart),
                        isRangeEndNotNull(rangeEnd),
                        isStatusNotNull(states),
                        qEvent.id.gt(from)
                )
                .limit(size)
                .fetch();

        Map<Event, Long> confirmedRequests = getCountConfirmedRequestsForEvent(events);
        List<String> uris = new ArrayList<>();

        for (Event event : events) {
            uris.add(String.format("/events/%s", event.getId()));
        }

        Map<String, Long> views = getView(uris);
        List<EventDtoOutput> eventDtoOutputList = new ArrayList<>();

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
        client.hit(statsDtoInput);
        List<String> uris = List.of(uri);
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
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().withNano(0);
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(10);
        }

        JPAQuery<Event> query = new JPAQuery<>(entityManager);
        QEvent qEvent = QEvent.event;

        List<Event> events = query.from(qEvent)
                .where(
                        isTextNotBlank(text),
                        qEvent.state.eq(State.PUBLISHED),
                        isCategoriesNotEmpty(categories),
                        qEvent.paid.eq(paid),
                        isRangeStartNotNull(rangeStart),
                        isRangeEndNotNull(rangeEnd),
                        isOnlyAvailableTrue(onlyAvailable, query),
                        qEvent.id.goe(from)
                )
                .orderBy(qEvent.eventDate.desc())
                .limit(size)
                .fetch();

        for (Event event : events) {
            String uri = String.format("/events/%s", event.getId());
            client.hit(new StatsDtoInput(APP, uri, request.getRemoteAddr(),
                    LocalDateTime.now().withNano(0)));
        }

        if (sort != null && sort.equals(Sort.VIEWS)) {
            return getEventShortDtoOutput(events)
                    .stream()
                    .sorted(Comparator.comparing(EventShortDtoOutput::getViews))
                    .collect(Collectors.toList());
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

    private BooleanExpression isUsersNotEmpty(List<Long> usersIds) {
        return usersIds != null ? QEvent.event.user.id.in(usersIds) : null;
    }

    private BooleanExpression isCategoriesNotEmpty(List<Long> categoriesIds) {
        return categoriesIds != null ? QEvent.event.category.id.in(categoriesIds) : null;
    }

    private BooleanExpression isRangeStartNotNull(LocalDateTime rangeStart) {
        return rangeStart != null ? QEvent.event.eventDate.goe(rangeStart) : null;
    }

    private BooleanExpression isRangeEndNotNull(LocalDateTime rangeEnd) {
        return rangeEnd != null ? QEvent.event.eventDate.loe(rangeEnd) : null;
    }

    private BooleanExpression isStatusNotNull(List<String> states) {
        if (states != null) {
            List<State> statesNew = new ArrayList<>();
            for (String state : states) {
                statesNew.add(State.valueOf(state));
            }
            return QEvent.event.state.in(statesNew);
        } else {
            return null;
        }
    }

    private BooleanExpression isTextNotBlank(String text) {
        return text != null && !text.isBlank() ? QEvent.event.annotation.contains(text)
                .or(QEvent.event.title.contains(text))
                .or(QEvent.event.description.contains(text)) : null;
    }

    private BooleanExpression isOnlyAvailableTrue(Boolean onlyAvailable, JPAQuery<Event> query) {
        return onlyAvailable.equals(true) ? QEvent.event.participantLimit.gt(
                query.select(QRequest.request.count()).from(QRequest.request)
                        .where(QRequest.request.event.id.eq(QEvent.event.id))) : null;
    }

    private Map<String, Long> getView(List<String> uris) {
        return client.getStats(LocalDateTime.now().withNano(0).minusYears(10),
                LocalDateTime.now().withNano(0).plusYears(10), uris, false);
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
