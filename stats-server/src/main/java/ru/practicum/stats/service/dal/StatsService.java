package ru.practicum.stats.service.dal;

import ru.practicum.stats.dto.StatsDtoInput;
import ru.practicum.stats.dto.StatsDtoOutput;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    StatsDtoOutput hit(StatsDtoInput stats);

    List<StatsDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
