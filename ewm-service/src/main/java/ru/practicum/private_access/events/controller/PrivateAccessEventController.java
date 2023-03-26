package ru.practicum.private_access.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.private_access.events.dto.EventDtoInput;
import ru.practicum.private_access.events.dto.EventDtoInputUpdate;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
@Validated
public class PrivateAccessEventController {

    private final EventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoOutput create(@RequestBody @Validated(Create.class) EventDtoInput eventDtoInput,
                                 @PathVariable @Positive Long userId) {
        log.info("create event");
        return service.create(userId, eventDtoInput);
    }

    @PatchMapping("/{eventId}")
    public EventDtoOutput update(@RequestBody @Validated(Update.class) EventDtoInputUpdate eventDtoInput,
                                 @PathVariable @Positive Long userId,
                                 @PathVariable @Positive Long eventId) {
        log.info("update event with id={}", eventId);
        return service.update(userId, eventId, eventDtoInput);
    }

    @GetMapping
    public List<EventShortDtoOutput> getAll(@PathVariable @Positive Long userId,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("get all from={} and size={} events by initiator with id={}", from, size, userId);
        return service.getAll(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDtoOutput getByIdForInitiator(@PathVariable @Positive Long userId,
                                              @PathVariable @Positive Long eventId) {
        log.info("get event with id={} by initiator with id={}", eventId, userId);
        return service.getByIdForInitiator(userId, eventId);
    }

}
