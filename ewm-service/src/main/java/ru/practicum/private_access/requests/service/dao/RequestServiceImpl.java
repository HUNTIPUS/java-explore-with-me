package ru.practicum.private_access.requests.service.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.exceptions.exception.AccessException;
import ru.practicum.exceptions.exception.DuplicateException;
import ru.practicum.exceptions.exception.InvalidRequestException;
import ru.practicum.exceptions.exception.StatusException;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.private_access.events.state.State;
import ru.practicum.private_access.requests.Status.Status;
import ru.practicum.private_access.requests.dto.RequestDtoOutput;
import ru.practicum.private_access.requests.dto.RequestsForStatusDtoInput;
import ru.practicum.private_access.requests.dto.RequestsForStatusDtoOutput;
import ru.practicum.private_access.requests.mapper.RequestMapper;
import ru.practicum.private_access.requests.model.Request;
import ru.practicum.private_access.requests.repository.RequestRepository;
import ru.practicum.private_access.requests.service.dal.RequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestServiceImpl implements RequestService {

    UserService userService;
    EventService eventService;
    RequestRepository repository;

    @Transactional
    @Override
    public RequestDtoOutput create(Long userId, Long eventId) {
        Event event = eventService.getById(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            User user = userService.getById(userId);
            if (event.getUser().getId().equals(userId)) {
                throw new InvalidRequestException(String
                        .format("The user with id=%s is the initiator of the event with id=%s", userId, event.getId()));
            }
            if (repository.getByEventIdAndUserId(eventId, userId) != null) {
                throw new DuplicateException("Request already exists");
            }
            List<Event> events = new ArrayList<>();
            events.add(event);
            List<Request> requests = repository.getPendingRequests(events);
            if (requests.isEmpty() || requests.size() < event.getParticipantLimit()) {
                Request request = new Request();
                request.setUser(user);
                request.setEvent(event);
                request.setStatus(Status.PENDING);
                request.setCreated(LocalDateTime.now().withNano(0));
                return RequestMapper.toRequestDto(repository.save(request));
            } else {
                throw new AccessException(String.format("All seats for the event with id=%s are occupied.", eventId));
            }
        } else {
            throw new AccessException("Event is not published");
        }
    }

    @Transactional
    @Override
    public RequestDtoOutput cancel(Long userId, Long requestId) {
        Request request = getById(requestId);
        userService.getById(userId);
        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(request);
    }

    @Override
    public Request getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjenesisException(String.format("Request with id=%s was not found", id)));
    }

    @Override
    public List<RequestDtoOutput> getRequestsByUser(Long userId) {
        return RequestMapper.toRequestDtoList(repository.getRequestsByUserId(userId));
    }

    @Override
    public List<RequestDtoOutput> getRequestsForInitiator(Long userId, Long eventId) {
        return RequestMapper.toRequestDtoList(repository.getAllRequestsByEvent(userId, eventId));
    }

    @Transactional
    @Override
    public RequestsForStatusDtoOutput update(Long userId, Long eventId, RequestsForStatusDtoInput requestDto) {
        userService.getById(userId);
        eventService.getById(eventId);
        List<Request> requests = repository.getSelectedRequest(userId, eventId, requestDto.getRequestIds());
        for (Request request : requests) {
            if (!request.getStatus().equals(Status.PENDING)) {
                throw new StatusException(String.format("Request with id=%s has status is %s", request.getId(),
                        request.getStatus()));
            }
        }
        List<Long> remainingRequests = repository.getRemainingRequest(userId, eventId, requestDto.getRequestIds())
                .stream().map(Request::getId).collect(Collectors.toList());
        List<Request> selectedRequestsNew = repository.updateRequests(requestDto.getRequestIds(),
                requestDto.getStatus().name());
        RequestsForStatusDtoInput.Status status = RequestsForStatusDtoInput.Status.CONFIRMED;
        if (requestDto.getStatus().equals(status)) {
            status = RequestsForStatusDtoInput.Status.REJECTED;
        }
        List<Request> remainingRequestsNew;
        if (!remainingRequests.isEmpty()) {
            remainingRequestsNew = repository.updateRequests(remainingRequests, status.name());
        } else {
            remainingRequestsNew = List.of();
        }
        if (requestDto.getStatus().equals(RequestsForStatusDtoInput.Status.CONFIRMED)) {
            return RequestsForStatusDtoOutput
                    .builder()
                    .confirmedRequests(RequestMapper.toRequestDtoList(selectedRequestsNew))
                    .rejectedRequests(RequestMapper.toRequestDtoList(remainingRequestsNew))
                    .build();
        } else {
            return RequestsForStatusDtoOutput
                    .builder()
                    .confirmedRequests(RequestMapper.toRequestDtoList(remainingRequestsNew))
                    .rejectedRequests(RequestMapper.toRequestDtoList(selectedRequestsNew))
                    .build();
        }
    }

}
