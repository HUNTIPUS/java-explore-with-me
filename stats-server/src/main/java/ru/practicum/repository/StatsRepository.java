package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("select st from Stats st where st.timestamp between :start and :end")
    List<Stats> getStatsWithoutUri(LocalDateTime start, LocalDateTime end);

    @Query("select st from Stats st where (st.timestamp between :start and :end)" +
            " and (st.uri in :uris)")
    List<Stats> getStatsWithUri(LocalDateTime start, LocalDateTime end, List<String> uris);
}
