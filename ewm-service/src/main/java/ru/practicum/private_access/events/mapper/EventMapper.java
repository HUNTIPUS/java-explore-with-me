package ru.practicum.private_access.events.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.admin_access.categories.mapper.CategoryMapper;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.admin_access.events.state_action.StateAction;
import ru.practicum.admin_access.users.mapper.UserMapper;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.dto.*;
import ru.practicum.private_access.events.location.mapper.LocationMapper;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.state.State;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(EventDtoInput eventDtoInput, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDtoInput.getAnnotation());
        event.setTitle(eventDtoInput.getTitle());
        event.setDescription(eventDtoInput.getDescription());
        event.setCreatedOn(LocalDateTime.now());
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
        event.setState(State.PENDING);
        return event;
    }

    public static Event toEvent(EventDtoInputUpdate eventDtoInput, User user, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDtoInput.getAnnotation());
        event.setDescription(eventDtoInput.getDescription());
        event.setTitle(eventDtoInput.getTitle());
        event.setCreatedOn(LocalDateTime.now());
        event.setParticipantLimit(eventDtoInput.getParticipantLimit());
        event.setEventDate(eventDtoInput.getEventDate());
        event.setPaid(eventDtoInput.getPaid());
        event.setRequestModeration(eventDtoInput.getRequestModeration());
        event.setUser(user);
        if (category != null) {
            event.setCategory(category);
        }
        if (eventDtoInput.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(eventDtoInput.getLocation()));
        }
        event.setState(State.PENDING);
        if (eventDtoInput.getStateAction() != null
                && eventDtoInput.getStateAction().equals(EventDtoInputUpdate.StateAction.CANCEL_REVIEW)) {
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
        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        event.setParticipantLimit(eventDto.getParticipantLimit());
        if (category != null) {
            event.setCategory(category);
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(LocationMapper.toLocation(eventDto.getLocation()));
        }
        if (eventDto.getStateAction().equals(EventDtoForAdminInput.StateAction.PUBLISH_EVENT)) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now().withNano(0));
        } else if (eventDto.getStateAction().name().equals(StateAction.CANCEL_REVIEW.name())) {
            event.setState(State.CANCELED);
        } else if (eventDto.getStateAction().equals(EventDtoForAdminInput.StateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }
        return event;
    }

    public static EventDtoOutputForAdmin toEventDtoOutputForAdmin(Event event) {
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
        return EventDtoOutputForAdmin
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
}
