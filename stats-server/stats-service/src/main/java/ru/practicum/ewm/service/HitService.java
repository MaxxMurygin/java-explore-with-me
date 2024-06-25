package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.StatsDto;

import java.util.List;

public interface HitService {
    void create(EndpointHitDto hitDto);

    List<StatsDto> getStats(String start,
                            String end,
                            String[] uris,
                            boolean unique);
    List<StatsDto> getStats(String start,
                            String end,
                            boolean unique);
    List<EndpointHitDto> hitDtos();
}
