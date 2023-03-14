package ru.practicum.admin_access.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.private_access.events.dto.EventDtoForAdminInput;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/admin/events")
public class EventAdminController {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final EventService service;

    @PatchMapping("/{id}")
    public EventDtoOutput update(@PathVariable @Positive Long id,
                                 @RequestBody @Validated(Update.class) EventDtoForAdminInput eventDtoForAdminInput) {
        log.info("update event with id={} by admin", id);
        return service.updateByAdmin(id, eventDtoForAdminInput);
    }

    @GetMapping
    public List<EventDtoOutput> getAllByParam(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<String> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT)
                                              LocalDateTime rangeStart,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT)
                                              LocalDateTime rangeEnd,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("events by user={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.getAllByParamForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
