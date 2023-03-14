package ru.practicum.admin_access.compilations.service.dal;

import ru.practicum.admin_access.compilations.dto.CompilationDtoInput;
import ru.practicum.admin_access.compilations.dto.CompilationDtoOutput;

import java.util.List;

public interface CompilationService {
    CompilationDtoOutput create(CompilationDtoInput compilationDtoInput);

    CompilationDtoOutput update(Long id, CompilationDtoInput compilationDtoInput);

    void delete(Long id);

    CompilationDtoOutput getById(Long id);

    List<CompilationDtoOutput> getByParams(Boolean pinned, Integer from, Integer size);
}
