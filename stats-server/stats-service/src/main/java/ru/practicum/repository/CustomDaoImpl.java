package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Stats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CustomDaoImpl implements CustomDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Stats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        SqlParameterSource parameters;
        List<Stats> statsStorage;
        if (uris == null || uris.isEmpty()) {
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
            statsStorage = jdbcTemplate.query(sql, parameters, CustomDaoImpl::makeToStats);
        } else {
            String sql;
            StringBuilder newUri = new StringBuilder();
            for (int i = 0; i < uris.size(); i++) {
                newUri.append("'%");
                newUri.append(uris.get(i));
                newUri.append("%'");
                if (i != uris.size() - 1) {
                    newUri.append(" or uri like ");
                }
            }

            parameters = new MapSqlParameterSource()
                    .addValue("start", start)
                    .addValue("end", end)
                    .addValue("newUri", newUri);

            if (!unique) {
                sql = "select app, uri, count(ip) hits from stats " +
                        "where time_stamp between :start and :end and (uri like   " + newUri +
                        ") group by app, uri order by hits desc";
            } else {
                sql = "select app, uri, count(distinct ip) hits from stats " +
                        "where time_stamp between :start and :end and (uri like " + newUri +
                        ") group by app, uri order by hits desc";
            }
            statsStorage = jdbcTemplate.query(sql, parameters, CustomDaoImpl::makeToStats);
        }
        return statsStorage;
    }

    private static Stats makeToStats(ResultSet rs, int rowNum) throws SQLException {
        Stats stats = new Stats();
        stats.setApp(rs.getString("app"));
        stats.setUri(rs.getString("uri"));
        stats.setHits(rs.getLong("hits"));
        return stats;
    }
}
