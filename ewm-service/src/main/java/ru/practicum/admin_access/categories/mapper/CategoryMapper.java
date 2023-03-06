package ru.practicum.admin_access.categories.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.categories.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
