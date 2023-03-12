package ru.practicum.stats.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.StatsDtoInput;
import ru.practicum.stats.dto.StatsDtoOutput;
import ru.practicum.stats.mapper.StatsMapper;
import ru.practicum.stats.repository.StatsRepository;
import ru.practicum.stats.service.dal.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public StatsDtoOutput hit(StatsDtoInput stats) {
        return StatsMapper.toStatsDto(statsRepository.save(StatsMapper.toStats(stats)));
    }

    @Override
    public List<StatsDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return StatsMapper.toStatsDtoList(statsRepository.getStats(start, end, uris, unique));
    }
}
