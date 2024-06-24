package com.github.maxxmurugin.ewm.service;

import com.github.maxxmurugin.ewm.dto.EndpointHitDto;
import com.github.maxxmurugin.ewm.dto.StatsDto;

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
}
