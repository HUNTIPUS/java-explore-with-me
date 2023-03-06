package ru.practicum.private_access.events.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.admin_access.categories.mapper.CategoryMapper;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.admin_access.users.mapper.UserMapper;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.dto.EventDtoInput;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.location.mapper.LocationMapper;
import ru.practicum.private_access.events.model.Event;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

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
        event.setLocation(LocationMapper.toLocation(eventDtoInput.getLocation()));
        event.setUser(user);
        event.setCategory(category);
        event.setState(eventDtoInput.getState());
        return event;
    }

    public static EventShortDtoOutput toEventShortDtoOutput(Event event, Integer confirmedRequests, Long views) {
        return EventShortDtoOutput
                .builder()
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .id(event.getId())
                .eventDate(event.getEventDate())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getUser()))
                .confirmedRequests(confirmedRequests)
                .views(views)
                .build();
    }

    public static EventDtoOutput toEventDtoOutput(Event event, Integer confirmedRequests, Long views) {
        return EventDtoOutput
                .builder()
                .annotation(event.getAnnotation())
                .title(event.getTitle())
                .id(event.getId())
                .eventDate(event.getEventDate())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getUser()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .state(event.getState())
                .publishedOn(event.getPublishedOn())
                .confirmedRequests(confirmedRequests)
                .views(views)
                .build();
    }
}
