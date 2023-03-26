package ru.practicum.private_access.requests.service.dal;

import ru.practicum.private_access.requests.dto.RequestDtoOutput;
import ru.practicum.private_access.requests.dto.RequestsForStatusDtoInput;
import ru.practicum.private_access.requests.dto.RequestsForStatusDtoOutput;
import ru.practicum.private_access.requests.model.Request;

import java.util.List;

public interface RequestService {
    RequestDtoOutput create(Long userId, Long eventId);

    RequestDtoOutput cancel(Long userId, Long requestId);

    Request getById(Long id);

    List<RequestDtoOutput> getRequestsByUser(Long userId);

    List<RequestDtoOutput> getRequestsForInitiator(Long userId, Long eventId);

    RequestsForStatusDtoOutput update(Long userId, Long eventId, RequestsForStatusDtoInput requestDto);
}
