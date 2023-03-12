package ru.practicum.private_access.requests.mapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.practicum.private_access.requests.dto.RequestDtoOutput;
import ru.practicum.private_access.requests.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {

    public static RequestDtoOutput toRequestDto(Request request) {
        return RequestDtoOutput
                .builder()
                .id(request.getId())
                .requester(request.getUser().getId())
                .status(request.getStatus())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .build();
    }

    public static List<RequestDtoOutput> toRequestDtoList(List<Request> requests) {
        return requests
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }
}
