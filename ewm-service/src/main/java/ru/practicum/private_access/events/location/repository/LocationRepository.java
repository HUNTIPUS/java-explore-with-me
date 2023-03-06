package ru.practicum.private_access.events.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.private_access.events.location.model.Location;
import ru.practicum.private_access.events.location.model.LocationKey;

@Repository
public interface LocationRepository extends JpaRepository<Location, LocationKey> {
}
