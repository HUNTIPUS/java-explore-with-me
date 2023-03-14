package ru.practicum.private_access.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.private_access.requests.dto.RequestDtoOutput;
import ru.practicum.private_access.requests.dto.RequestsForStatusDtoInput;
import ru.practicum.private_access.requests.dto.RequestsForStatusDtoOutput;
import ru.practicum.private_access.requests.service.dal.RequestService;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}")
@Validated
public class PrivateAccessRequestController {

    private final RequestService service;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDtoOutput create(@PathVariable @Positive Long userId,
                                   @RequestParam @Positive Long eventId) {
        log.info("create request by user with id={}", userId);
        return service.create(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public RequestDtoOutput cancel(@PathVariable @Positive Long userId,
                       @PathVariable @Positive Long requestId) {
        log.info("cancel request with id={} by user with id={}", requestId, userId);
        return service.cancel(userId, requestId);
    }

    @GetMapping("/requests")
    public List<RequestDtoOutput> getRequestsByUser(@PathVariable @Positive Long userId) {
        log.info("requests for participation in other people's events");
        return service.getRequestsByUser(userId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<RequestDtoOutput> getRequestsByInitiator(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId) {
        log.info("requests for participation by initiator with id={} in event with id={}", userId, eventId);
        return service.getRequestsForInitiator(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public RequestsForStatusDtoOutput update(@PathVariable @Positive Long userId,
                                             @PathVariable @Positive Long eventId,
                                             @RequestBody @Validated(Update.class)
                                             RequestsForStatusDtoInput requestsDto) {
        log.info("changing the status of requests for participation");
        return service.update(userId, eventId, requestsDto);
    }
}
