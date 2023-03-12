package ru.practicum.admin_access.categories.service.dal;

import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.categories.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void delete(Long id);

    Category getById(Long id);

    List<CategoryDto> getByParam(Integer from, Integer size);

    List<Category> getAll();
}
