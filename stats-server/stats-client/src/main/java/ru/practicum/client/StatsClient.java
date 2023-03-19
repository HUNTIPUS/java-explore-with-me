package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Service
public class StatsClient extends BaseClient {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> hit(StatsDtoInput statsDtoInput) {
        return post("/hit", statsDtoInput);
    }

    public Map<String, Long> getStats(LocalDateTime start,
                                      LocalDateTime end,
                                      List<String> uris,
                                      Boolean unique) {

        String startNew = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endNew = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, Object> parameters = Map.of(
                "start", startNew,
                "end", endNew,
                "uris", uris,
                "unique", unique
        );

        ResponseEntity<Object> objects = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                parameters);
        ObjectMapper objectMapper = new ObjectMapper();
        List<StatsDtoOutput> stats = objectMapper.convertValue(objects.getBody(), new TypeReference<>() {
        });
        if (stats == null) {
            return new HashMap<>();
        } else {
            return stats.stream().collect(groupingBy(StatsDtoOutput::getUri, counting()));
        }
    }
}
