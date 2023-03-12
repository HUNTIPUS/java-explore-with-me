package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Stats;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long>, CustomDao {
}
