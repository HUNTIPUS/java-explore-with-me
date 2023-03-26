package ru.practicum.admin_access.compilation_event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.admin_access.compilation_event.model.CompilationEvent;

import java.util.List;

@Repository
public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {
    @Query("select ce from CompilationEvent ce where ce.compilation.id = :compilationId")
    List<CompilationEvent> getByCompilation(Long compilationId);

    @Query("delete from CompilationEvent ce where ce.compilation.id = :compilationId and ce.event.id = :eventId")
    void deleteByCompilationAndEvent(Long compilationId, Long eventId);

    @Query("select ce from CompilationEvent ce where ce.compilation.pinned = :pinned ")
    List<CompilationEvent> getAllByCompilationPinned(Boolean pinned, Pageable pageable);
}
