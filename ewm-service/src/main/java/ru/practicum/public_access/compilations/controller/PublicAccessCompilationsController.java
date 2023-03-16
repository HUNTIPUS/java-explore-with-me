package ru.practicum.public_access.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin_access.compilations.dto.CompilationDtoOutput;
import ru.practicum.admin_access.compilations.service.dal.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/compilations")
public class PublicAccessCompilationsController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationDtoOutput> getByParam(@RequestParam(required = false) Boolean pinned,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("compilations by pinned={}, from={}, size={}", pinned, from, size);
        return service.getByParams(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDtoOutput getById(@PathVariable @Positive Long id) {
        log.info("compilation with id={}", id);
        return service.getById(id);
    }
}
