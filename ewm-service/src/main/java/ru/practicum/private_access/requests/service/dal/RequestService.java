package ru.practicum.private_access.requests.service.dal;

import ru.practicum.private_access.requests.model.Request;

public interface RequestService {
    Request create(Long userId, Long eventId);
    void cancel(Long userId, Long requestId);
    Request getById(Long requestId);
}
