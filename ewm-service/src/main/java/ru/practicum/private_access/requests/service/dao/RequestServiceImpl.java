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
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.service.dal.EventService;
import ru.practicum.private_access.requests.Status.Status;
import ru.practicum.private_access.requests.model.Request;
import ru.practicum.private_access.requests.repository.RequestRepository;
import ru.practicum.private_access.requests.service.dal.RequestService;

import java.time.LocalDateTime;

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
    public Request create(Long userId, Long eventId) {
        Event event = eventService.getById(eventId);
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
        return repository.save(request);
    }

    @Transactional
    @Override
    public void cancel(Long userId, Long requestId) {
        getById(requestId);
        userService.getById(userId);
        repository.deleteByIdAndUserId(userId, requestId);
    }

    @Override
    public Request getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjenesisException(String.format("Request with id=%s was not found", id)));
    }
}
