package ru.practicum.admin_access.compilations.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.admin_access.compilations.dto.CompilationDtoInput;
import ru.practicum.admin_access.compilations.dto.CompilationDtoOutput;
import ru.practicum.admin_access.compilations.model.Compilation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation toCompilation(CompilationDtoInput compilationDtoInput) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDtoInput.getTitle());
        compilation.setPinned(compilationDtoInput.getPinned());
        return compilation;
    }

    public static CompilationDtoOutput toCompilationDtoOutput(Compilation compilation) {
        return CompilationDtoOutput
                .builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }
}
