package ru.practicum.private_access.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.admin_access.categories.model.Category;
import ru.practicum.private_access.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> getAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event e where e.user.id = :userId and e.id = :eventId")
    List<Event> getByInitiatorId(Long userId, Long eventId);

    @Query("select case when count(e) > 0 then true else false end from Event e where e.category = :category")
    boolean existsByCategory(Category category);

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
