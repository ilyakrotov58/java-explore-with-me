package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.dto.StatInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public interface IServerRepository extends JpaRepository<Hit, Long> {

    @Query(value =
            "SELECT new ru.practicum.dto.StatInfoDto(hits.app, hits.uri, COUNT(DISTINCT hits.ip)) FROM Hit hits " +
                    "WHERE ?1 < hits.timestamp AND ?2 > hits.timestamp " +
                    "AND hits.uri IN (?3) " +
                    "GROUP BY hits.uri, hits.app " +
                    "ORDER BY COUNT(DISTINCT hits.ip) DESC")
    List<StatInfoDto> getStatisticOfUriUniqIp(LocalDateTime start, LocalDateTime end, String[] uri);

    @Query(value =
            "SELECT new ru.practicum.dto.StatInfoDto(hits.app, hits.uri, COUNT(hits.ip)) FROM Hit hits " +
                    "WHERE ?1 < hits.timestamp AND ?2 > hits.timestamp " +
                    "AND hits.uri IN (?3)" +
                    "GROUP BY hits.uri, hits.app " +
                    "ORDER BY COUNT(hits.ip) DESC")
    List<StatInfoDto> getStatisticOfUriAllIp(LocalDateTime start, LocalDateTime end, String[] uri);

    @Query(value =
            "SELECT new ru.practicum.dto.StatInfoDto(hits.app, hits.uri, COUNT(hits.ip)) FROM Hit hits " +
                    "WHERE ?1 < hits.timestamp AND ?2 > hits.timestamp " +
                    "GROUP BY hits.uri, hits.app " +
                    "ORDER BY COUNT(hits.ip) DESC")
    List<StatInfoDto> getStatisticWithoutUriAndUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value =
            "SELECT new ru.practicum.dto.StatInfoDto(hits.app, hits.uri, COUNT(DISTINCT hits.ip)) FROM Hit hits " +
                    "WHERE ?1 < hits.timestamp AND ?2 > hits.timestamp " +
                    "GROUP BY hits.uri, hits.app " +
                    "ORDER BY COUNT(DISTINCT hits.ip) DESC"
    )
    List<StatInfoDto> getStatisticWithoutUriAndWithUniqueIp(LocalDateTime start, LocalDateTime end);
}
