package ru.practicum.private_access.events.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.admin_access.categories.mapper.CategoryMapper;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.admin_access.events.state_action.StateAction;
import ru.practicum.admin_access.users.mapper.UserMapper;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.dto.EventDtoForAdminInput;
import ru.practicum.private_access.events.dto.EventDtoInput;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.location.mapper.LocationMapper;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.state.State;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(EventDtoInput eventDtoInput, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDtoInput.getAnnotation());
        event.setTitle(eventDtoInput.getTitle());
        event.setDescription(eventDtoInput.getDescription());
        event.setCreatedOn(eventDtoInput.getCreatedOn());
        event.setEventDate(eventDtoInput.getEventDate());
        event.setParticipantLimit(eventDtoInput.getParticipantLimit());
        event.setPaid(eventDtoInput.getPaid());
        event.setRequestModeration(eventDtoInput.getRequestModeration());
        if (eventDtoInput.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(eventDtoInput.getLocation()));
        }
        event.setUser(user);
        if (category != null) {
            event.setCategory(category);
        }
        event.setState(eventDtoInput.getState());
        if (eventDtoInput.getStateAction() != null
                && StateAction.valueOf(eventDtoInput.getStateAction()).equals(StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        }
        return event;
    }

    public static Event toEventAdmin(EventDtoForAdminInput eventDto, Category category) {
        Event event = new Event();
        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(eventDto.getLocation()));
        }
        if (StateAction.valueOf(eventDto.getStateAction()).equals(StateAction.PUBLISH_EVENT)) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(eventDto.getPublishedOn());
        } else if (StateAction.valueOf(eventDto.getStateAction()).equals(StateAction.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
        } else if (StateAction.valueOf(eventDto.getStateAction()).equals(StateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }
        return event;
    }

    public static EventShortDtoOutput toEventShortDtoOutput(Event event) {
        String eventDate = null;
        if (event.getEventDate() != null) {
            eventDate = event.getEventDate().format(FORMAT);
        }
        return EventShortDtoOutput
                .builder()
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .id(event.getId())
                .eventDate(eventDate)
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getUser()))
                .confirmedRequests(0)
                .views(0L)
                .build();
    }

    public static EventDtoOutput toEventDtoOutput(Event event) {
        String eventDate = null;
        String createdOn = null;
        String publishedOn = null;
        if (event.getEventDate() != null) {
            eventDate = event.getEventDate().format(FORMAT);
        }
        if (event.getCreatedOn() != null) {
            createdOn = event.getCreatedOn().format(FORMAT);
        }
        if (event.getPublishedOn() != null) {
            publishedOn = event.getPublishedOn().format(FORMAT);
        }
        return EventDtoOutput
                .builder()
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .id(event.getId())
                .eventDate(eventDate)
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getUser()))
                .createdOn(createdOn)
                .description(event.getDescription())
                .state(event.getState())
                .publishedOn(publishedOn)
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(0)
                .views(0L)
                .build();
    }

    public static List<EventShortDtoOutput> toEventShortDtoOutputList(List<Event> events) {
        return events
                .stream()
                .map(EventMapper::toEventShortDtoOutput)
                .collect(Collectors.toList());
    }
}
