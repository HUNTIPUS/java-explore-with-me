package ru.practicum.admin_access.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin_access.compilations.dto.CompilationDtoInput;
import ru.practicum.admin_access.compilations.dto.CompilationDtoOutput;
import ru.practicum.admin_access.compilations.service.dal.CompilationService;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/admin/compilations")
public class CompilationController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDtoOutput create(@RequestBody @Validated(Create.class) CompilationDtoInput compilationDtoInput) {
        log.info("create compilation");
        return service.create(compilationDtoInput);
    }

    @PatchMapping("/{id}")
    public CompilationDtoOutput update(@PathVariable @Positive Long id,
                                       @RequestBody @Validated(Update.class) CompilationDtoInput compilationDtoInput) {
        log.info("update compilation with id={}", id);
        return service.update(id, compilationDtoInput);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
        log.info("delete compilation with id={}", id);
    }

}
