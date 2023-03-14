package ru.practicum.private_access.requests.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestsForStatusDtoOutput {
    private List<RequestDtoOutput> confirmedRequests;
    private List<RequestDtoOutput> rejectedRequests;
}
