package ru.practicum.private_access.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.admin_access.compilations.model.Compilation;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.events.state.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> getAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event e where e.user.id = :userId and e.id = :eventId")
    List<Event> getByInitiatorId(Long userId, Long eventId);

    @Query("select e from Event e where e.category.id = :categoryId")
    List<Event> getByCategory(Long categoryId);

    @Query("select e from Event e where e.compilation.id = :compilationId")
    List<Event> getByCompilation(Long compilationId);

    @Query("select e from Event e where e.user.id in :user and e.state in :states and " +
            "e.category.id in :categories and e.eventDate between :rangeStart and :rangeEnd")
    List<Event> getAllByParam(List<Long> user, List<State> states,
                              List<Long> categories, LocalDateTime rangeStart,
                              LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.compilation in :compilations")
    List<Event> getEventByCompilation(List<Compilation> compilations);

    @Query("select e from Event e where (e.annotation like %:text% or e.description like %:text%) " +
            "and e.category.id in :categories and e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd " +
            "and e.state = 'PUBLISHED'")
    List<Event> getEventsByTextWithoutAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.category.id in :categories " +
            "and e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd and e.state = 'PUBLISHED'")
    List<Event> getEventsWithoutTextWithoutAvailable(List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where (e.annotation like %:text% or e.description like %:text%) " +
            "and e.category.id in :categories and e.paid = :paid and e.eventDate between :rangeStart and :rangeEnd " +
            "and e.state = 'PUBLISHED' and e.participantLimit > (select count(r) from Request r where " +
            "r.event.id = e.id)")
    List<Event> getEventsByTextWithAvailable(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.category.id in :categories and e.paid = :paid " +
            "and e.eventDate between :rangeStart and :rangeEnd " +
            "and e.state = 'PUBLISHED' and e.participantLimit > (select count(r) from Request r where " +
            "r.event.id = e.id)")
    List<Event> getEventsWithoutTextWithAvailable(List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e where e.id = :id and e.state = 'PUBLISHED'")
    Optional<Event> getByIdWithStatePublished(Long id);
}
