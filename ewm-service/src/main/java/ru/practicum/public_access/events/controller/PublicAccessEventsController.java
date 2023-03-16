package ru.practicum.public_access.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.private_access.events.dto.EventDtoOutput;
import ru.practicum.private_access.events.dto.EventShortDtoOutput;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.public_access.events.sort.Sort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/events")
public class PublicAccessEventsController {

    private final EventService service;
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping
    public List<EventShortDtoOutput> getByParam(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(defaultValue = "false") Boolean paid,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT)
                                                    LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT)
                                                    LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(required = false) Sort sort,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        return service.getAllByParamForPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{id}")
    public EventDtoOutput getById(@PathVariable @Positive Long id, HttpServletRequest request) {
        return service.getByIdForDto(id, request);
    }
}
