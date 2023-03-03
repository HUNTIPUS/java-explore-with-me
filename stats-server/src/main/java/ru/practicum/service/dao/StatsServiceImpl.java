package ru.practicum.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.dal.StatsService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public StatsDtoOutput hit(StatsDtoInput statsDtoInput) {
        return StatsMapper.toStatsDto(statsRepository.save(StatsMapper.toStats(statsDtoInput)));
    }

    @Override
    public List<StatsDtoOutput> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        SqlParameterSource parameters;
        if (uris == null) {
            String sql;
            parameters = new MapSqlParameterSource()
                    .addValue("start", start)
                    .addValue("end", end);
            if (!unique) {
                sql = "select app, uri, count(ip) hits from stats " +
                        "where time_stamp between :start and :end group by app, uri order by hits desc";
            } else {
                sql = "select app, uri, count(distinct ip) hits from stats " +
                        "where time_stamp between :start and :end group by app, uri order by hits desc";
            }
            return jdbcTemplate.query(sql, parameters, StatsServiceImpl::makeToDto);
        } else {
            String sql;
            parameters = new MapSqlParameterSource()
                    .addValue("start", start)
                    .addValue("end", end)
                    .addValue("uris", uris);
            if (!unique) {
                sql = "select app, uri, count(ip) hits from stats " +
                        "where time_stamp between :start and :end and uri in (:uris) group by app, uri order by hits desc";
            } else {
                sql = "select app, uri, count(distinct ip) hits from stats " +
                        "where time_stamp between :start and :end and uri in (:uris) " +
                        "group by app, uri order by hits desc";
            }
            return jdbcTemplate.query(sql, parameters, StatsServiceImpl::makeToDto);
        }
    }

    private static StatsDtoOutput makeToDto(ResultSet rs, int rowNum) throws SQLException {
        return StatsDtoOutput
                .builder()
                .app(rs.getString("app"))
                .uri(rs.getString("uri"))
                .hits(rs.getLong("hits"))
                .build();
    }
}
