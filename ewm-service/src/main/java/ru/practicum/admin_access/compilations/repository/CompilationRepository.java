package ru.practicum.admin_access.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.admin_access.compilations.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select c from Compilation c where c.pinned = :pinned and c not in :compilations")
    List<Compilation> getCompilationByParam(Boolean pinned, List<Compilation> compilations, Pageable pageable);

    @Query("select c from Compilation c where c not in :compilations")
    List<Compilation> getCompilationWithoutParam(List<Compilation> compilations, Pageable pageable);

    @Query("select c from Compilation c where c.pinned = :pinned")
    List<Compilation> getCompilationByParamWithPinned(Boolean pinned, Pageable pageable);

}
