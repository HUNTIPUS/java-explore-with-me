package ru.practicum.admin_access.categories.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin_access.categories.dto.CategoryDto;
import ru.practicum.admin_access.categories.mapper.CategoryMapper;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.admin_access.categories.repository.CategoryRepository;
import ru.practicum.admin_access.categories.service.dal.CategoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto newCategoryDto) {
        Category category = get(id);
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void delete(Long id) {
        get(id);
        repository.deleteById(id);
    }

    private Category get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjenesisException(String.format("Category with id=%s was not found", id)));
    }
}
