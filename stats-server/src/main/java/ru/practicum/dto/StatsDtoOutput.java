package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsDtoOutput {

    private String app;
    private String uri;
    private long hits;
}
