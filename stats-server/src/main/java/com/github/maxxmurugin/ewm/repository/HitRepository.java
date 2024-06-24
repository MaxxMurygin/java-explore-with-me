package com.github.maxxmurugin.ewm.repository;

import com.github.maxxmurugin.ewm.dto.StatsDto;
import com.github.maxxmurugin.ewm.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    EndpointHit findByIp(String ip);

    @Query("SELECT new com.github.maxxmurugin.ewm.dto.StatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "from EndpointHit h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri ")
    List<StatsDto> findByStartAndEnd(@Param("start") LocalDateTime start,
                                     @Param("end")LocalDateTime end);

    @Query("SELECT new com.github.maxxmurugin.ewm.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "from EndpointHit h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri ")
    List<StatsDto> findByStartAndEndAndUnique(@Param("start") LocalDateTime start,
                                     @Param("end")LocalDateTime end);

    @Query("SELECT new com.github.maxxmurugin.ewm.dto.StatsDto(h.app, h.uri, COUNT(h.ip)) " +
            "from EndpointHit h " +
            "WHERE (h.created BETWEEN :start AND :end) AND " +
            "h.uri IN :uris " +
            "GROUP BY h.app, h.uri ")
    List<StatsDto> findByStartAndEndAndUriIn(@Param("start") LocalDateTime start,
                                             @Param("end")LocalDateTime end,
                                             @Param("uris") String[] uris);

    @Query("SELECT new com.github.maxxmurugin.ewm.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "from EndpointHit h " +
            "WHERE (h.created BETWEEN :start AND :end) AND " +
            "h.uri IN :uris " +
            "GROUP BY h.app, h.uri ")
    List<StatsDto> findByStartAndEndAndUniqueAndUriIn(@Param("start") LocalDateTime start,
                                             @Param("end")LocalDateTime end,
                                             @Param("uris") String[] uris);
}
