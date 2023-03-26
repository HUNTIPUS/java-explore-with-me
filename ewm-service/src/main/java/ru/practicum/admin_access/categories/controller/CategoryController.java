package ru.practicum.admin_access.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.categories.service.dal.CategoryService;
import ru.practicum.valid.Create;
import ru.practicum.valid.Update;

import javax.validation.constraints.Positive;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@Slf4j
@Validated
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Validated(Create.class) CategoryDto categoryDto) {
        log.info("create category");
        return service.create(categoryDto);
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable @Positive Long id,
                              @RequestBody @Validated(Update.class) CategoryDto categoryDto) {
        log.info("update category with id={}", id);
        return service.update(id, categoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive Long id) {
        service.delete(id);
        log.info("delete category with id={}", id);
    }
}
