package ru.practicum.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.dal.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public StatsDtoOutput hit(StatsDtoInput statsDtoInput) {
        return StatsMapper.toStatsDto(statsRepository.save(StatsMapper.toStats(statsDtoInput)));
    }

    @Override
    public List<StatsDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<Stats> stats;
        if (uris == null) {
            if (!unique) {
                stats = statsRepository.getStatsWithoutUri(start, end);
            } else {
                stats = statsRepository.getStatsWithoutUri(start, end)
                        .stream()
                        .distinct()
                        .collect(Collectors.toList());
            }
        } else {
            if (!unique) {
                stats = statsRepository.getStatsWithUri(start, end, uris);
            } else {
                stats = statsRepository.getStatsWithUri(start, end, uris)
                        .stream()
                        .distinct()
                        .collect(Collectors.toList());
            }
        }
        return convertToDtoList(stats);
    }

    private List<StatsDtoOutput> convertToDtoList(List<Stats> stats) {
        Map<String, Map<String, Long>> appAndUri = stats
                .stream()
                .collect(groupingBy(Stats::getApp, groupingBy(Stats::getUri, counting())));
        List<StatsDtoOutput> statsDtoOutputList = new ArrayList<>();

        for (String app : appAndUri.keySet()) {
            for (String uri : appAndUri.get(app).keySet()) {
                statsDtoOutputList.add(StatsDtoOutput
                        .builder()
                        .app(app)
                        .uri(uri)
                        .hits(appAndUri.get(app).get(uri))
                        .build());
            }
        }

        return statsDtoOutputList;
    }
}
