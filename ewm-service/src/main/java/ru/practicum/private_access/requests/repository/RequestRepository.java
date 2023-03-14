package ru.practicum.private_access.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.private_access.events.model.Event;
import ru.practicum.private_access.requests.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>, RequestDao {

    @Query("select r from Request r " +
            "where r.status = 'CONFIRMED' and r.event in :events")
    List<Request> getConfirmedRequests(List<Event> events);

    @Query("select r from Request r " +
            "where r.status = 'PENDING' and r.event in :events")
    List<Request> getPendingRequests(List<Event> events);

    @Query("select r from Request r where r.user.id = :userId")
    List<Request> getRequestsByUserId(Long userId);

    @Query("select r from Request r join Event e on e.id = r.event.id " +
            "where e.user.id = :userId and r.event.id = :eventId")
    List<Request> getAllRequestsByEvent(Long userId, Long eventId);

    @Query("select r from Request r join Event e on e.id = r.event.id " +
            "where e.id = :eventId and e.user.id = :userId and r.id in :ids")
    List<Request> getSelectedRequest(Long userId, Long eventId, List<Long> ids);

    @Query("select r from Request r join Event e on e.id = r.event.id " +
            "where e.id = :eventId and e.user.id = :userId and r.id not in :ids")
    List<Request> getRemainingRequest(Long userId, Long eventId, List<Long> ids);

    @Query("select r from Request r where r.event.id = :eventId and r.user.id = :userId")
    Request getByEventIdAndUserId(Long eventId, Long userId);

}
