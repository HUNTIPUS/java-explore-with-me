package ru.practicum.service.dal;

import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    StatsDtoOutput hit(StatsDtoInput stats);

    List<StatsDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
