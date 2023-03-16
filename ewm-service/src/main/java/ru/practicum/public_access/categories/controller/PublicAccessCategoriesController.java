package ru.practicum.public_access.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.categories.mapper.CategoryMapper;
import ru.practicum.admin_access.categories.service.dal.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/categories")
public class PublicAccessCategoriesController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getByParam(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("categories by from={} and size={}", from, size);
        return service.getByParam(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable @Positive Long id) {
        log.info("category with id={}", id);
        return CategoryMapper.toCategoryDto(service.getById(id));
    }
}
