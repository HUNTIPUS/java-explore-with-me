package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
public class StatsController {

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final StatsService service;

    @PostMapping("/hit")
    public StatsDtoOutput hit(@RequestBody @Valid StatsDtoInput statsDtoInput) {
        return service.hit(statsDtoInput);
    }

    @GetMapping("/stats")
    public List<StatsDtoOutput> getStats(@RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = FORMAT) LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
