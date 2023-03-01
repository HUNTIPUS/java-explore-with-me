package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.StatsDtoInput;
import ru.practicum.dto.StatsDtoOutput;
import ru.practicum.service.dal.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping
public class StatsController {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsService service;

    @PostMapping("/hit")
    public StatsDtoOutput hit(@RequestBody StatsDtoInput statsDtoInput) {
        return service.hit(statsDtoInput);
    }

    @GetMapping("/stats")
    public List<StatsDtoOutput> getStats(@RequestParam String start,
                                         @RequestParam String end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return service.getStats(LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8), FORMAT),
                LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8), FORMAT),
                uris,
                unique);
    }
}
