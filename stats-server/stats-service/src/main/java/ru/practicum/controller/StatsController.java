package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;
import ru.practicum.service.dal.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class StatsController {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsDtoOutput hit(@RequestBody @Valid StatsDtoInput statsDtoInput) {
        log.info("create hit");
        return service.hit(statsDtoInput);
    }

    @GetMapping("/stats")
    public List<StatsDtoOutput> getStats(@RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get stats by start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique);
    }
}
