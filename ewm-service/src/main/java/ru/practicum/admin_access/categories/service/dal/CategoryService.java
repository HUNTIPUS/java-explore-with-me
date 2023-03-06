package ru.practicum.admin_access.categories.service.dal;

import ru.practicum.admin_access.categories.dto.CategoryDto;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(Long id, CategoryDto categoryDto);
    void delete(Long id);
}
