package ru.practicum.service.dal;

import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    Stats hit(Stats stats);

    List<Stats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
