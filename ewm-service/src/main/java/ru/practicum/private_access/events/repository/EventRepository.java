package ru.practicum.private_access.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.private_access.events.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> getAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event e where e.user.id = :userId and e.id = :eventId")
    Event getByInitiatorId(Long userId, Long eventId);
}
