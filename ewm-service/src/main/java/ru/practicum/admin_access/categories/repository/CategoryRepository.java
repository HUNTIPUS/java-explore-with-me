package ru.practicum.admin_access.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin_access.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
