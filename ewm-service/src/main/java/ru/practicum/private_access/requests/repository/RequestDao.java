package ru.practicum.private_access.requests.repository;

import ru.practicum.private_access.requests.model.Request;

import java.util.List;

public interface RequestDao {

    void deleteRequestByIdAndUserId(Long id, Long userId);

    List<Request> updateRequests(List<Long> ids, String status);
}
