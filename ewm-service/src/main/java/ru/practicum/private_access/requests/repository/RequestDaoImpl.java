package ru.practicum.private_access.requests.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.practicum.admin_access.users.model.User;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.requests.Status.Status;
import ru.practicum.private_access.requests.model.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RequestDaoImpl implements RequestDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void deleteRequestByIdAndUserId(Long id, Long userId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("requestId", id)
                .addValue("userId", userId);
        String sql = "delete from requests where id = :requestId and id_user = :userId";
        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<Request> updateRequests(List<Long> ids, String status) {
        String sql = "update requests set status = :status where id = :id";
        Map<String, Object>[] batchOfInputs = new HashMap[ids.size()];
        int count = 0;
        for (Long id : ids) {
            Map<String, Object> map = new HashMap();
            map.put("id", id);
            map.put("status", status);
            batchOfInputs[count++] = map;
        }
        jdbcTemplate.batchUpdate(sql, batchOfInputs);

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        String sqlGet = "select * from requests r join events e on e.id = r.id_event " +
                "join users u on u.id = r.id_user where r.id in (:ids)";
        return jdbcTemplate.query(sqlGet, parameters, RequestDaoImpl::toMakeRequest);
    }

    private static Request toMakeRequest(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id_user"));
        Event event = new Event();
        event.setId(rs.getLong("id_event"));
        Request request = new Request();
        request.setId(rs.getLong("id"));
        request.setCreated(rs.getTimestamp("created").toLocalDateTime());
        request.setUser(user);
        request.setEvent(event);
        request.setStatus(Status.valueOf(rs.getString("status")));
        return request;
    }
}
