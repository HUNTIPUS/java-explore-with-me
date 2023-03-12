package ru.practicum.stats.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stats.dto.StatsDtoInput;
import ru.practicum.stats.dto.StatsDtoOutput;
import ru.practicum.stats.model.Stats;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatsMapper {


    public static Stats toStats(StatsDtoInput statsDtoInput) {
        Stats stats = new Stats();
        stats.setApp(statsDtoInput.getApp());
        stats.setUri(statsDtoInput.getUri());
        stats.setIp(statsDtoInput.getIp());
        stats.setTimestamp(statsDtoInput.getTimestamp());
        return stats;
    }

    public static StatsDtoOutput toStatsDto(Stats stats) {
        return StatsDtoOutput
                .builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }

    public static List<StatsDtoOutput> toStatsDtoList(List<Stats> statsStorages) {
        return statsStorages
                .stream()
                .map(StatsMapper::toStatsDto)
                .collect(Collectors.toList());
    }
}
