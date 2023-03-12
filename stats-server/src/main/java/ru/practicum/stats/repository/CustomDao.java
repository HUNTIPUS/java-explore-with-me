package ru.practicum.stats.repository;

import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomDao {
    List<Stats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
