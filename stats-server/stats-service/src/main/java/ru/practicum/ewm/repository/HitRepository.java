package ru.practicum.ewm.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.StatsDto;
import ru.practicum.ewm.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    EndpointHit findByIp(String ip);

    @Query("SELECT new ru.practicum.ewm.dto.StatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<StatsDto> findByStartAndEnd(@Param("start") LocalDateTime start,
                                     @Param("end")LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<StatsDto> findByStartAndEndAndUnique(@Param("start") LocalDateTime start,
                                     @Param("end")LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.dto.StatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE (h.created BETWEEN :start AND :end) AND " +
            "h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<StatsDto> findByStartAndEndAndUriIn(@Param("start") LocalDateTime start,
                                             @Param("end")LocalDateTime end,
                                             @Param("uris") String[] uris);

    @Query("SELECT new ru.practicum.ewm.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit h " +
            "WHERE (h.created BETWEEN :start AND :end) AND " +
            "h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<StatsDto> findByStartAndEndAndUniqueAndUriIn(@Param("start") LocalDateTime start,
                                             @Param("end")LocalDateTime end,
                                             @Param("uris") String[] uris);
}
