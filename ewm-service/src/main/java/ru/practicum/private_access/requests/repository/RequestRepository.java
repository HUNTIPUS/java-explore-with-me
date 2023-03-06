package ru.practicum.private_access.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.private_access.requests.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("delete from Request r where r.id = :requestId and r.user.id = :userId")
    void deleteByIdAndUserId(Long userId, Long requestId);
}
