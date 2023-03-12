package ru.practicum.private_access.requests.service.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.admin_access.users.service.dal.UserService;
import ru.practicum.exceptions.exceptoin.InvalidRequestException;
import ru.practicum.exceptions.exceptoin.ObjectExistenceException;
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
            Request request = new Request();
            request.setUser(user);
            request.setEvent(event);
            request.setStatus(Status.PENDING);
            request.setCreated(LocalDateTime.now().withNano(0));
            return RequestMapper.toRequestDto(repository.save(request));
        } else {
            throw new ObjectExistenceException("Event is not published");
        }
    }

    @Transactional
    @Override
    public void cancel(Long userId, Long requestId) {
        getById(requestId);
        userService.getById(userId);
        repository.deleteRequestByIdAndUserId(requestId, userId);
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
        List<Long> remainingRequests = repository.getRemainingRequest(userId, eventId, requestDto.getRequestIds())
                .stream().map(Request::getId).collect(Collectors.toList());
        Status statusFromRequest = Status.valueOf(requestDto.getStatus());
        List<Request> selectedRequestsNew = repository.updateRequests(requestDto.getRequestIds(),
                statusFromRequest.name());
        Status status = Status.CONFIRMED;
        if (statusFromRequest.equals(status)) {
            status = Status.REJECTED;
        }
        List<Request> remainingRequestsNew;
        if (!remainingRequests.isEmpty()) {
            remainingRequestsNew = repository.updateRequests(remainingRequests, status.name());
        } else {
            remainingRequestsNew = List.of();
        }
        if (statusFromRequest.equals(Status.CONFIRMED)) {
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
