package ru.practicum.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.dal.StatsService;

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
        List<StatsDtoOutput> statsDtoOutputList = StatsMapper.toStatsDtoList(statsRepository
                .getStats(start, end, uris, unique));
        return statsDtoOutputList;
    }
}
